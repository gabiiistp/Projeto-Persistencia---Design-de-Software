package com.banco.transferencia.doc.repository;

import com.banco.transferencia.doc.model.TransferenciaDOCModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaDOCRepository
        extends JpaRepository<TransferenciaDOCModel, String> {
}