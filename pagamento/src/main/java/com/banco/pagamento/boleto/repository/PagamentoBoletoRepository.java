package com.banco.pagamento.boleto.repository;

import com.banco.pagamento.boleto.model.PagamentoBoletoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoBoletoRepository extends JpaRepository<PagamentoBoletoModel, String> {
}
