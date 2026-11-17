package com.banco.pagamento.saldoemconta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento_saldo_em_conta")
public class PagamentoSaldoEmContaModel {

    @Id
    private String id;
    private String contaId;
    private String descricao;        
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataHora;

    public PagamentoSaldoEmContaModel() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContaId() { return contaId; }
    public void setContaId(String contaId) { this.contaId = contaId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
