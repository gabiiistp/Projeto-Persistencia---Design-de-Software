package com.banco.transferencia.ted.service;

import com.banco.transferencia.ted.model.TransferenciaTEDModel;
import com.banco.transferencia.ted.repository.TransferenciaTEDRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferenciaTEDService {

    private final TransferenciaTEDRepository repository;

    public TransferenciaTEDService(TransferenciaTEDRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TransferenciaTEDModel processarTransferencia(TransferenciaTEDModel transferencia) {
        transferencia.setId(UUID.randomUUID().toString());
        transferencia.setStatus("PROCESSANDO");
        transferencia.setDataHora(LocalDateTime.now());
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaTEDModel confirmarTransferencia(String id) {
        TransferenciaTEDModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("CONFIRMADA");
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaTEDModel estornarTransferencia(String id) {
        TransferenciaTEDModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("ESTORNADA");
        return repository.save(transferencia);
    }
}