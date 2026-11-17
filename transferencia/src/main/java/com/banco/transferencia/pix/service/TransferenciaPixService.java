package com.banco.transferencia.pix.service;

import com.banco.transferencia.pix.model.TransferenciaPixModel;
import com.banco.transferencia.pix.repository.TransferenciaPixRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferenciaPixService {

    private final TransferenciaPixRepository repository;

    public TransferenciaPixService(TransferenciaPixRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TransferenciaPixModel processarTransferencia(TransferenciaPixModel transferencia) {
        transferencia.setId(UUID.randomUUID().toString());
        transferencia.setStatus("PROCESSANDO");
        transferencia.setDataHora(LocalDateTime.now());
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaPixModel confirmarTransferencia(String id) {
        TransferenciaPixModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("CONFIRMADA");
        return repository.save(transferencia);
    }

    @Transactional
    public TransferenciaPixModel estornarTransferencia(String id) {
        TransferenciaPixModel transferencia = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transferência não encontrada: " + id));
        transferencia.setStatus("ESTORNADA");
        return repository.save(transferencia);
    }
}