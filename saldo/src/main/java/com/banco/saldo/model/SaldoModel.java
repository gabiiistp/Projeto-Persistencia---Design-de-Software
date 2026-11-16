package com.banco.saldo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade persistida do microsserviço Saldo.
 * A anotação @Entity faz o Hibernate mapear esta classe para uma tabela;
 * cada atributo vira uma coluna.
 */
@Entity
@Table(name = "saldo")
public class SaldoModel {

    @Id
    private String contaId;
    private BigDecimal saldoDisponivel;
    private LocalDateTime ultimaAtualizacao;

    public SaldoModel() {
        // construtor vazio exigido pelo JPA
    }

    public SaldoModel(String contaId, BigDecimal saldoDisponivel) {
        this.contaId = contaId;
        this.saldoDisponivel = saldoDisponivel;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public String getContaId() {
        return contaId;
    }

    public void setContaId(String contaId) {
        this.contaId = contaId;
    }

    public BigDecimal getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public void setSaldoDisponivel(BigDecimal saldoDisponivel) {
        this.saldoDisponivel = saldoDisponivel;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
