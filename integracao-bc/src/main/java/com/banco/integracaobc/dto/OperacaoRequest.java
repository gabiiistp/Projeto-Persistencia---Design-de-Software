package com.banco.integracaobc.dto;

/** Corpo de /integracao-bc/operacoes: { "transacaoId": "...", "tipoOperacao": "TRANSFERENCIA_PIX" } */
public record OperacaoRequest(String transacaoId, String tipoOperacao) {
}
