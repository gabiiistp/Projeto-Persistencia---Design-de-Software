package com.banco.integracaobc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Registro de cada operação comunicada ao Banco Central.
 * Vira a tabela "integracao_banco_central" no db_integracao_bc.db.
 */
@Entity
@Table(name = "integracao_banco_central")
public class IntegracaoBancoCentralModel {

    @Id
    private String id;
    private String transacaoId;
    private String tipoOperacao;
    private String status;
    private LocalDateTime dataHora;
    private String respostaBC;

    public IntegracaoBancoCentralModel() {
        // construtor vazio exigido pelo JPA
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(String transacaoId) {
        this.transacaoId = transacaoId;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
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

    public String getRespostaBC() {
        return respostaBC;
    }

    public void setRespostaBC(String respostaBC) {
        this.respostaBC = respostaBC;
    }
}
