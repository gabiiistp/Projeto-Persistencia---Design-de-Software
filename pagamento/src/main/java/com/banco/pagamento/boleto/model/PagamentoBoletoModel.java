package com.banco.pagamento.boleto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento_boleto")
public class PagamentoBoletoModel {

    @Id
    private String id;
    private String contaId;
    private String codigoBarras;
    private LocalDate vencimento;
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataHora;

    public PagamentoBoletoModel() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContaId() { return contaId; }
    public void setContaId(String contaId) { this.contaId = contaId; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public LocalDate getVencimento() { return vencimento; }
    public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
