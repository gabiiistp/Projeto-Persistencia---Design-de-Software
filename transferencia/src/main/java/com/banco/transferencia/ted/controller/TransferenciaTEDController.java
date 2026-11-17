package com.banco.transferencia.ted.controller;

import com.banco.transferencia.ted.model.TransferenciaTEDModel;
import com.banco.transferencia.ted.service.TransferenciaTEDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias/ted")
public class TransferenciaTEDController {

    private final TransferenciaTEDService service;

    public TransferenciaTEDController(TransferenciaTEDService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferenciaTEDModel> efetuarTransferencia(
            @RequestBody TransferenciaTEDModel requisicao) {
        return ResponseEntity.ok(service.processarTransferencia(requisicao));
    }
}