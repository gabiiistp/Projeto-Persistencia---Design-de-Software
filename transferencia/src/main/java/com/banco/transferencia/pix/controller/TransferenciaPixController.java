package com.banco.transferencia.pix.controller;

import com.banco.transferencia.pix.model.TransferenciaPixModel;
import com.banco.transferencia.pix.service.TransferenciaPixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias/pix")
public class TransferenciaPixController {

    private final TransferenciaPixService service;

    public TransferenciaPixController(TransferenciaPixService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferenciaPixModel> efetuarTransferencia(
            @RequestBody TransferenciaPixModel requisicao) {
        TransferenciaPixModel salva = service.processarTransferencia(requisicao);
        return ResponseEntity.ok(salva);
    }
}