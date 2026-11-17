package com.banco.transferencia.client;

import com.banco.transferencia.client.dto.OperacaoRequest;
import com.banco.transferencia.client.dto.OperacaoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IntegracaoBcClient {

    private final RestClient restClient;

    public IntegracaoBcClient(RestClient.Builder builder,
                              @Value("${integracaobc.url}") String integracaoBcUrl) {
        this.restClient = builder.baseUrl(integracaoBcUrl).build();
    }

    public OperacaoResponse comunicarOperacao(String transacaoId, String tipoOperacao) {
        return restClient.post()
                .uri("/integracao-bc/operacoes")
                .body(new OperacaoRequest(transacaoId, tipoOperacao))
                .retrieve()
                .body(OperacaoResponse.class);
    }
}