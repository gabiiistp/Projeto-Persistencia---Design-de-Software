package com.banco.integracaobc.controller;

import com.banco.integracaobc.dto.OperacaoRequest;
import com.banco.integracaobc.model.IntegracaoBancoCentralModel;
import com.banco.integracaobc.service.IntegracaoBancoCentralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Porta de entrada HTTP da Integração Banco Central (:8084). */
@RestController
@RequestMapping("/integracao-bc/operacoes")
public class IntegracaoBancoCentralController {

    private final IntegracaoBancoCentralService service;

    public IntegracaoBancoCentralController(IntegracaoBancoCentralService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IntegracaoBancoCentralModel> comunicarOperacao(
            @RequestBody OperacaoRequest requisicao) {
        return ResponseEntity.ok(service.comunicarOperacao(requisicao));
    }

    @GetMapping
    public ResponseEntity<List<IntegracaoBancoCentralModel>> listar(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.listar(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntegracaoBancoCentralModel> buscar(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/{id}/reprocessar")
    public ResponseEntity<IntegracaoBancoCentralModel> reprocessar(@PathVariable String id) {
        return ResponseEntity.ok(service.reprocessar(id));
    }
}
