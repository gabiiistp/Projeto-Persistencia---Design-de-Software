package com.banco.saldo.repository;

import com.banco.saldo.model.SaldoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Só uma interface: o Spring Data JPA gera a implementação em tempo de execução
 * (save, findById, findAll, existsById, deleteById...).
 */
@Repository
public interface SaldoRepository extends JpaRepository<SaldoModel, String> {
}
