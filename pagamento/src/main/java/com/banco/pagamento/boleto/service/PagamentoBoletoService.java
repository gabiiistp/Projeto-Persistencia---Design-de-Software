package com.banco.pagamento.boleto.service;

import com.banco.pagamento.boleto.model.PagamentoBoletoModel;
import com.banco.pagamento.boleto.repository.PagamentoBoletoRepository;
import com.banco.pagamento.client.IntegracaoBcClient;
import com.banco.pagamento.client.SaldoClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PagamentoBoletoService {

    private final PagamentoBoletoRepository repository;
    private final SaldoClient saldoClient;
    private final IntegracaoBcClient integracaoBcClient;

    public PagamentoBoletoService(PagamentoBoletoRepository repository,
                                  SaldoClient saldoClient,
                                  IntegracaoBcClient integracaoBcClient) {
        this.repository = repository;
        this.saldoClient = saldoClient;
        this.integracaoBcClient = integracaoBcClient;
    }

    @Transactional
    public PagamentoBoletoModel efetuarPagamento(PagamentoBoletoModel pagamento) {
        pagamento.setId(UUID.randomUUID().toString());
        pagamento.setStatus("PROCESSANDO");
        pagamento.setDataHora(LocalDateTime.now());
        repository.save(pagamento);

        if (pagamento.getVencimento() != null
                && pagamento.getVencimento().isBefore(LocalDate.now())) {
            return atualizarStatus(pagamento, "RECUSADO");
        }

        try {
            if (!saldoClient.validarSaldo(pagamento.getContaId(), pagamento.getValor())) {
                return atualizarStatus(pagamento, "RECUSADO");
            }
            saldoClient.debitar(pagamento.getContaId(), pagamento.getValor());
        } catch (RestClientException e) {
            return atualizarStatus(pagamento, "FALHA");
        }

        try {
            integracaoBcClient.comunicarOperacao(pagamento.getId(), "PAGAMENTO_BOLETO");
        } catch (RestClientException e) {
            estornarSemFalhar(pagamento);
            return atualizarStatus(pagamento, "ESTORNADO");
        }

        return atualizarStatus(pagamento, "CONCLUIDO");
    }

    public List<PagamentoBoletoModel> listarTodos() {
        return repository.findAll();
    }

    public Optional<PagamentoBoletoModel> buscarPorId(String id) {
        return repository.findById(id);
    }

    private PagamentoBoletoModel atualizarStatus(PagamentoBoletoModel pagamento, String status) {
        pagamento.setStatus(status);
        return repository.save(pagamento);
    }

    private void estornarSemFalhar(PagamentoBoletoModel pagamento) {
        try {
            saldoClient.estornar(pagamento.getContaId(), pagamento.getValor());
        } catch (RestClientException e) {
            // status ESTORNADO fica gravado para reprocessamento
        }
    }
}
