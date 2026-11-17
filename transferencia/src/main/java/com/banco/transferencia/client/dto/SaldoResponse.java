package com.banco.transferencia.client.dto;

import java.math.BigDecimal;

public record SaldoResponse(String contaId, BigDecimal saldoDisponivel) {
}