package com.banco.transferencia.ted.repository;

import com.banco.transferencia.ted.model.TransferenciaTEDModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaTEDRepository
        extends JpaRepository<TransferenciaTEDModel, String> {
}