package com.banco.transferencia.client.dto;

import java.math.BigDecimal;

public record SaldoRequest(String contaId, BigDecimal valor) {
}