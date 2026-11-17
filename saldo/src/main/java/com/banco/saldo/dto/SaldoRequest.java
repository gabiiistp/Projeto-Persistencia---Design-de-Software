package com.banco.saldo.dto;

import java.math.BigDecimal;

/** Corpo de validar / debitar / estornar: { "contaId": "...", "valor": 100.00 } */
public record SaldoRequest(String contaId, BigDecimal valor) {
}
