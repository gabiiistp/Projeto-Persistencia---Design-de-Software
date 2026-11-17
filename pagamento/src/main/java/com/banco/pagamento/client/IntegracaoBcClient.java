package com.banco.pagamento.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IntegracaoBcClient {

    public record OperacaoRequest(String transacaoId, String tipoOperacao) {
    }

    private final RestClient restClient;

    public IntegracaoBcClient(@Value("${servicos.integracao-bc.url}") String integracaoBcUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(integracaoBcUrl)
                .build();
    }
    
    public void comunicarOperacao(String transacaoId, String tipoOperacao) {
        restClient.post()
                .uri("/integracao-bc/operacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OperacaoRequest(transacaoId, tipoOperacao))
                .retrieve()
                .toBodilessEntity();
    }
}
