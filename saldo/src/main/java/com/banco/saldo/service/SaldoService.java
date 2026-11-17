package com.banco.saldo.service;

import com.banco.saldo.model.SaldoModel;
import com.banco.saldo.repository.SaldoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Regras de negócio do Saldo. Chama o Repository para ler/gravar no SQLite.
 */
@Service
public class SaldoService {

    private final SaldoRepository repository;

    public SaldoService(SaldoRepository repository) {
        this.repository = repository;   // injeção de dependência
    }

    @Transactional(readOnly = true)
    public SaldoModel consultarSaldo(String contaId) {
        return buscarConta(contaId);
    }

    /** Abre uma conta de saldo (necessário para ter com o que testar o débito). */
    @Transactional
    public SaldoModel criarConta(String contaId, BigDecimal saldoInicial) {
        if (contaId == null || contaId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId é obrigatório");
        }
        if (saldoInicial == null || saldoInicial.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "saldoInicial não pode ser negativo");
        }
        if (repository.existsById(contaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conta já existe: " + contaId);
        }
        return repository.save(new SaldoModel(contaId, saldoInicial));   // INSERT
    }

    /** Apenas confere se o saldo cobre o valor; não altera nada. */
    @Transactional(readOnly = true)
    public boolean validarSaldo(String contaId, BigDecimal valor) {
        exigirValorPositivo(valor);
        return buscarConta(contaId).getSaldoDisponivel().compareTo(valor) >= 0;
    }

    /**
     * Debita o valor da conta. @Transactional: se algo falhar no meio,
     * o Spring desfaz (rollback) e o saldo não fica pela metade.
     */
    @Transactional
    public SaldoModel debitar(String contaId, BigDecimal valor) {
        exigirValorPositivo(valor);
        SaldoModel conta = buscarConta(contaId);
        if (conta.getSaldoDisponivel().compareTo(valor) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Saldo insuficiente na conta " + contaId);
        }
        conta.setSaldoDisponivel(conta.getSaldoDisponivel().subtract(valor));
        conta.setUltimaAtualizacao(LocalDateTime.now());
        return repository.save(conta);   // UPDATE
    }

    /** Devolve o valor para a conta (desfaz um débito). */
    @Transactional
    public SaldoModel estornar(String contaId, BigDecimal valor) {
        exigirValorPositivo(valor);
        SaldoModel conta = buscarConta(contaId);
        conta.setSaldoDisponivel(conta.getSaldoDisponivel().add(valor));
        conta.setUltimaAtualizacao(LocalDateTime.now());
        return repository.save(conta);   // UPDATE
    }

    private SaldoModel buscarConta(String contaId) {
        return repository.findById(contaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta não encontrada: " + contaId));
    }

    private void exigirValorPositivo(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "valor deve ser maior que zero");
        }
    }
}
