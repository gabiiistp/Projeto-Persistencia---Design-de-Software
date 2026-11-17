package com.banco.pagamento.saldoemconta.service;

import com.banco.pagamento.client.SaldoClient;
import com.banco.pagamento.saldoemconta.model.PagamentoSaldoEmContaModel;
import com.banco.pagamento.saldoemconta.repository.PagamentoSaldoEmContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PagamentoSaldoEmContaService {

    private final PagamentoSaldoEmContaRepository repository;
    private final SaldoClient saldoClient;

    public PagamentoSaldoEmContaService(PagamentoSaldoEmContaRepository repository,
                                        SaldoClient saldoClient) {
        this.repository = repository;
        this.saldoClient = saldoClient;
    }

    @Transactional
    public PagamentoSaldoEmContaModel efetuarPagamento(PagamentoSaldoEmContaModel pagamento) {
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

        return atualizarStatus(pagamento, "CONCLUIDO");
    }

    public List<PagamentoSaldoEmContaModel> listarTodos() {
        return repository.findAll();
    }

    public Optional<PagamentoSaldoEmContaModel> buscarPorId(String id) {
        return repository.findById(id);
    }

    private PagamentoSaldoEmContaModel atualizarStatus(PagamentoSaldoEmContaModel pagamento, String status) {
        pagamento.setStatus(status);
        return repository.save(pagamento);
    }
}
