package com.banco.saldo.dto;

import java.math.BigDecimal;

/** Corpo para abrir uma conta de saldo: { "contaId": "...", "saldoInicial": 500.00 } */
public record CriarContaRequest(String contaId, BigDecimal saldoInicial) {
}
