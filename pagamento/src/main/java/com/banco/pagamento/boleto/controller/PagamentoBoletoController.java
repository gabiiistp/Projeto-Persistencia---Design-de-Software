package com.banco.pagamento.boleto.controller;

import com.banco.pagamento.boleto.model.PagamentoBoletoModel;
import com.banco.pagamento.boleto.service.PagamentoBoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/boleto")
public class PagamentoBoletoController {

    private final PagamentoBoletoService service;

    public PagamentoBoletoController(PagamentoBoletoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PagamentoBoletoModel> efetuarPagamento(@RequestBody PagamentoBoletoModel requisicao) {
        return ResponseEntity.ok(service.efetuarPagamento(requisicao));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoBoletoModel>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoBoletoModel> buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
