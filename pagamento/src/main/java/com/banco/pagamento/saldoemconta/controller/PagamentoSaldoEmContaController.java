package com.banco.pagamento.saldoemconta.controller;

import com.banco.pagamento.saldoemconta.model.PagamentoSaldoEmContaModel;
import com.banco.pagamento.saldoemconta.service.PagamentoSaldoEmContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/saldo-em-conta")
public class PagamentoSaldoEmContaController {

    private final PagamentoSaldoEmContaService service;

    public PagamentoSaldoEmContaController(PagamentoSaldoEmContaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PagamentoSaldoEmContaModel> efetuarPagamento(@RequestBody PagamentoSaldoEmContaModel requisicao) {
        return ResponseEntity.ok(service.efetuarPagamento(requisicao));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoSaldoEmContaModel>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoSaldoEmContaModel> buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
