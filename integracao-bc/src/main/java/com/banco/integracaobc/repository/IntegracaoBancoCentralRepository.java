package com.banco.integracaobc.repository;

import com.banco.integracaobc.model.IntegracaoBancoCentralModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Além dos métodos prontos do JpaRepository, o Spring Data gera as consultas
 * abaixo só pelo NOME do método (findBy + atributo).
 */
@Repository
public interface IntegracaoBancoCentralRepository
        extends JpaRepository<IntegracaoBancoCentralModel, String> {

    List<IntegracaoBancoCentralModel> findByStatus(String status);

    List<IntegracaoBancoCentralModel> findByTransacaoId(String transacaoId);
}
