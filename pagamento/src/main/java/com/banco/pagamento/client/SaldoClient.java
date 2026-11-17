package com.banco.pagamento.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class SaldoClient {

    public record SaldoRequest(String contaId, BigDecimal valor) {
    }

    private final RestClient restClient;

    public SaldoClient(@Value("${servicos.saldo.url}") String saldoUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(saldoUrl)
                .build();
    }

    public boolean validarSaldo(String contaId, BigDecimal valor) {
        Boolean resposta = restClient.post()
                .uri("/saldos/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SaldoRequest(contaId, valor))
                .retrieve()
                .body(Boolean.class);
        return Boolean.TRUE.equals(resposta);
    }

    public void debitar(String contaId, BigDecimal valor) {
        restClient.post()
                .uri("/saldos/debitar")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SaldoRequest(contaId, valor))
                .retrieve()
                .toBodilessEntity();
    }

    public void estornar(String contaId, BigDecimal valor) {
        restClient.post()
                .uri("/saldos/estornar")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SaldoRequest(contaId, valor))
                .retrieve()
                .toBodilessEntity();
    }
}
