package com.banco.pagamento.pix.repository;

import com.banco.pagamento.pix.model.PagamentoPixModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoPixRepository extends JpaRepository<PagamentoPixModel, String> {
}
