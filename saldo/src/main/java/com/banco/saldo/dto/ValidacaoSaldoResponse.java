package com.banco.saldo.dto;

import java.math.BigDecimal;

/** Resposta de /saldos/validar: informa se o saldo cobre o valor solicitado. */
public record ValidacaoSaldoResponse(String contaId,
                                     BigDecimal valor,
                                     BigDecimal saldoDisponivel,
                                     boolean saldoSuficiente) {
}
