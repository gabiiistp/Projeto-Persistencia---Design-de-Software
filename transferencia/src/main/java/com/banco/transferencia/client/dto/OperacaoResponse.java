package com.banco.transferencia.client.dto;

public record OperacaoResponse(String id,
                               String transacaoId,
                               String tipoOperacao,
                               String status,
                               String respostaBC) {
}