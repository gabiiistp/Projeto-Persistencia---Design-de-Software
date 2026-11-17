package com.banco.pagamento.pix.service;

import com.banco.pagamento.client.IntegracaoBcClient;
import com.banco.pagamento.client.SaldoClient;
import com.banco.pagamento.pix.model.PagamentoPixModel;
import com.banco.pagamento.pix.repository.PagamentoPixRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PagamentoPixService {

    private final PagamentoPixRepository repository;
    private final SaldoClient saldoClient;
    private final IntegracaoBcClient integracaoBcClient;

    public PagamentoPixService(PagamentoPixRepository repository,
                               SaldoClient saldoClient,
                               IntegracaoBcClient integracaoBcClient) {
        this.repository = repository;
        this.saldoClient = saldoClient;
        this.integracaoBcClient = integracaoBcClient;
    }

    @Transactional
    public PagamentoPixModel efetuarPagamento(PagamentoPixModel pagamento) {
        pagamento.setId(UUID.randomUUID().toString());
        pagamento.setStatus("PROCESSANDO");
        pagamento.setDataHora(LocalDateTime.now());
        repository.save(pagamento);

        try {
            if (!saldoClient.validarSaldo(pagamento.getContaId(), pagamento.getValor())) {
                return atualizarStatus(pagamento, "RECUSADO");
            }

            saldoClient.debitar(pagamento.getContaId(), pagamento.getValor());
        } catch (RestClientException e) {
            return atualizarStatus(pagamento, "FALHA");
        }

        try {
            integracaoBcClient.comunicarOperacao(pagamento.getId(), "PAGAMENTO_PIX");
        } catch (RestClientException e) {
            estornarSemFalhar(pagamento);
            return atualizarStatus(pagamento, "ESTORNADO");
        }

        return atualizarStatus(pagamento, "CONCLUIDO");
    }

    public List<PagamentoPixModel> listarTodos() {
        return repository.findAll();
    }

    public Optional<PagamentoPixModel> buscarPorId(String id) {
        return repository.findById(id);
    }

    private PagamentoPixModel atualizarStatus(PagamentoPixModel pagamento, String status) {
        pagamento.setStatus(status);
        return repository.save(pagamento);
    }

    private void estornarSemFalhar(PagamentoPixModel pagamento) {
        try {
            saldoClient.estornar(pagamento.getContaId(), pagamento.getValor());
        } catch (RestClientException e) {
        }
    }
}
