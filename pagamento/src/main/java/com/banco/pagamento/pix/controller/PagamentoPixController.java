package com.banco.pagamento.pix.controller;

import com.banco.pagamento.pix.model.PagamentoPixModel;
import com.banco.pagamento.pix.service.PagamentoPixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos/pix")
public class PagamentoPixController {

    private final PagamentoPixService service;

    public PagamentoPixController(PagamentoPixService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PagamentoPixModel> efetuarPagamento(@RequestBody PagamentoPixModel requisicao) {
        return ResponseEntity.ok(service.efetuarPagamento(requisicao));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoPixModel>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoPixModel> buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
