package com.banco.transferencia.pix.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencia_pix")
public class TransferenciaPixModel {

    @Id
    private String id;
    private String contaOrigemId;
    private String chavePixDestino;
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataHora;

    // Construtor vazio (o Hibernate precisa dele para montar os objetos lidos do banco)
    public TransferenciaPixModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(String contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public String getChavePixDestino() {
        return chavePixDestino;
    }

    public void setChavePixDestino(String chavePixDestino) {
        this.chavePixDestino = chavePixDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}