package com.banco.transferencia.client;

import com.banco.transferencia.client.dto.SaldoRequest;
import com.banco.transferencia.client.dto.SaldoResponse;
import com.banco.transferencia.client.dto.ValidacaoSaldoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class SaldoClient {

    private final RestClient restClient;

    public SaldoClient(RestClient.Builder builder,
                       @Value("${saldo.url}") String saldoUrl) {
        this.restClient = builder.baseUrl(saldoUrl).build();
    }

    public ValidacaoSaldoResponse validarSaldo(String contaId, BigDecimal valor) {
        return restClient.post()
                .uri("/saldos/validar")
                .body(new SaldoRequest(contaId, valor))
                .retrieve()
                .body(ValidacaoSaldoResponse.class);
    }

    public SaldoResponse debitar(String contaId, BigDecimal valor) {
        return restClient.post()
                .uri("/saldos/debitar")
                .body(new SaldoRequest(contaId, valor))
                .retrieve()
                .body(SaldoResponse.class);
    }

    public SaldoResponse estornar(String contaId, BigDecimal valor) {
        return restClient.post()
                .uri("/saldos/estornar")
                .body(new SaldoRequest(contaId, valor))
                .retrieve()
                .body(SaldoResponse.class);
    }
}