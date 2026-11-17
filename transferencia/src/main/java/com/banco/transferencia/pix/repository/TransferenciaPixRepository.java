package com.banco.transferencia.pix.repository;

import com.banco.transferencia.pix.model.TransferenciaPixModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaPixRepository
        extends JpaRepository<TransferenciaPixModel, String> {
}