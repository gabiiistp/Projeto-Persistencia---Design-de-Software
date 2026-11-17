package com.banco.transferencia.ted.service;

import com.banco.transferencia.client.IntegracaoBcClient;
import com.banco.transferencia.client.SaldoClient;
import com.banco.transferencia.client.dto.ValidacaoSaldoResponse;
import com.banco.transferencia.ted.model.TransferenciaTEDModel;
import com.banco.transferencia.ted.repository.TransferenciaTEDRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferenciaTEDService {

    private static final String TIPO_OPERACAO = "TRANSFERENCIA_TED";

    private final TransferenciaTEDRepository repository;
    private final SaldoClient saldoClient;
    private final IntegracaoBcClient integracaoBcClient;

    public TransferenciaTEDService(TransferenciaTEDRepository repository,
                                   SaldoClient saldoClient,
                                   IntegracaoBcClient integracaoBcClient) {
        this.repository = repository;
        this.saldoClient = saldoClient;
        this.integracaoBcClient = integracaoBcClient;
    }

    // Sem @Transactional: o debito no Saldo (outro servico) nao entra no rollback
    // do nosso banco. Por isso usamos o estorno como compensacao.
    public TransferenciaTEDModel processarTransferencia(TransferenciaTEDModel transferencia) {
        transferencia.setId(UUID.randomUUID().toString());
        transferencia.setDataHora(LocalDateTime.now());

        // 1. Validar saldo
        try {
            ValidacaoSaldoResponse validacao = saldoClient.validarSaldo(
                    transferencia.getContaOrigemId(), transferencia.getValor());
            if (validacao == null || !validacao.saldoSuficiente()) {
                transferencia.setStatus("RECUSADA");
                return repository.save(transferencia);
            }
        } catch (RestClientException e) {
            transferencia.setStatus("FALHA_SALDO");
            return repository.save(transferencia);
        }

        // 2. Debitar
        try {
            saldoClient.debitar(transferencia.getContaOrigemId(), transferencia.getValor());
        } catch (RestClientException e) {
            transferencia.setStatus("FALHA_SALDO");
            return repository.save(transferencia);
        }

        // 3. Gravar como PROCESSANDO
        transferencia.setStatus("PROCESSANDO");
        repository.save(transferencia);

        // 4. Avisar o Banco Central
        try {
            integracaoBcClient.comunicarOperacao(transferencia.getId(), TIPO_OPERACAO);
            return confirmarTransferencia(transferencia.getId());
        } catch (RestClientException e) {
            // 5. Falhou: devolver o dinheiro
            try {
                saldoClient.estornar(transferencia.getContaOrigemId(), transferencia.getValor());
                return estornarTransferencia(transferencia.getId());
            } catch (RestClientException erroEstorno) {
                transferencia.setStatus("ESTORNO_PENDENTE");
                return repository.save(transferencia);
            }
        }
    }

    @Transactional
    public TransferenciaTEDModel confirmarTransferencia(String id) {
        TransferenciaTEDModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("CONFIRMADA");
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaTEDModel estornarTransferencia(String id) {
        TransferenciaTEDModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("ESTORNADA");
        return repository.save(transferencia);
    }
}