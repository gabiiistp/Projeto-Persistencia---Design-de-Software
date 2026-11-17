package com.banco.transferencia.doc.service;

import com.banco.transferencia.doc.model.TransferenciaDOCModel;
import com.banco.transferencia.doc.repository.TransferenciaDOCRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferenciaDOCService {

    private final TransferenciaDOCRepository repository;

    public TransferenciaDOCService(TransferenciaDOCRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TransferenciaDOCModel processarTransferencia(TransferenciaDOCModel transferencia) {
        transferencia.setId(UUID.randomUUID().toString());
        transferencia.setStatus("PROCESSANDO");
        transferencia.setDataHora(LocalDateTime.now());
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaDOCModel confirmarTransferencia(String id) {
        TransferenciaDOCModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("CONFIRMADA");
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaDOCModel estornarTransferencia(String id) {
        TransferenciaDOCModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("ESTORNADA");
        return repository.save(transferencia);
    }
}