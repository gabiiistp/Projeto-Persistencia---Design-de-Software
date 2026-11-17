package com.banco.transferencia.doc.controller;

import com.banco.transferencia.doc.model.TransferenciaDOCModel;
import com.banco.transferencia.doc.service.TransferenciaDOCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias/doc")
public class TransferenciaDOCController {

    private final TransferenciaDOCService service;

    public TransferenciaDOCController(TransferenciaDOCService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferenciaDOCModel> efetuarTransferencia(
            @RequestBody TransferenciaDOCModel requisicao) {
        return ResponseEntity.ok(service.processarTransferencia(requisicao));
    }
}