package com.banco.saldo.controller;

import com.banco.saldo.dto.CriarContaRequest;
import com.banco.saldo.dto.SaldoRequest;
import com.banco.saldo.dto.ValidacaoSaldoResponse;
import com.banco.saldo.model.SaldoModel;
import com.banco.saldo.service.SaldoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Porta de entrada HTTP do microsserviço Saldo (:8083). */
@RestController
@RequestMapping("/saldos")

public class SaldoController {

    private final SaldoService service;

    public SaldoController(SaldoService service) {
        this.service = service;
    }

    @GetMapping("/{contaId}")
    public ResponseEntity<SaldoModel> consultarSaldo(@PathVariable String contaId) {
        return ResponseEntity.ok(service.consultarSaldo(contaId));
    }

    @PostMapping
    public ResponseEntity<SaldoModel> criarConta(@RequestBody CriarContaRequest requisicao) {
        SaldoModel criada = service.criarConta(requisicao.contaId(), requisicao.saldoInicial());
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PostMapping("/validar")
    public ResponseEntity<ValidacaoSaldoResponse> validarSaldo(@RequestBody SaldoRequest requisicao) {
        boolean suficiente = service.validarSaldo(requisicao.contaId(), requisicao.valor());
        SaldoModel conta = service.consultarSaldo(requisicao.contaId());
        return ResponseEntity.ok(new ValidacaoSaldoResponse(
                requisicao.contaId(), requisicao.valor(), conta.getSaldoDisponivel(), suficiente));
    }

    @PostMapping("/debitar")
    public ResponseEntity<SaldoModel> debitar(@RequestBody SaldoRequest requisicao) {
        return ResponseEntity.ok(service.debitar(requisicao.contaId(), requisicao.valor()));
    }

    @PostMapping("/estornar")
    public ResponseEntity<SaldoModel> estornar(@RequestBody SaldoRequest requisicao) {
        return ResponseEntity.ok(service.estornar(requisicao.contaId(), requisicao.valor()));
    }
}
