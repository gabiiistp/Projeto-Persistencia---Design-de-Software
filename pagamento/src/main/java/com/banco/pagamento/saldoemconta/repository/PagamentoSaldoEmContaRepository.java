package com.banco.pagamento.saldoemconta.repository;

import com.banco.pagamento.saldoemconta.model.PagamentoSaldoEmContaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoSaldoEmContaRepository extends JpaRepository<PagamentoSaldoEmContaModel, String> {
}
