package com.banco.transferencia.client.dto;

import java.math.BigDecimal;

public record ValidacaoSaldoResponse(String contaId,
                                     BigDecimal valor,
                                     BigDecimal saldoDisponivel,
                                     boolean saldoSuficiente) {
}