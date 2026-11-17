package com.banco.integracaobc.service;

import com.banco.integracaobc.dto.OperacaoRequest;
import com.banco.integracaobc.model.IntegracaoBancoCentralModel;
import com.banco.integracaobc.repository.IntegracaoBancoCentralRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Único serviço que "fala" com o Banco Central. Nesta versão acadêmica o
 * Banco Central é simulado: a resposta é montada localmente.
 */
@Service
public class IntegracaoBancoCentralService {

    public static final String STATUS_CONFIRMADA = "CONFIRMADA";

    private final IntegracaoBancoCentralRepository repository;

    public IntegracaoBancoCentralService(IntegracaoBancoCentralRepository repository) {
        this.repository = repository;   // injeção de dependência
    }

    /** Registra e comunica uma operação ao BC. */
    @Transactional
    public IntegracaoBancoCentralModel comunicarOperacao(OperacaoRequest requisicao) {
        if (requisicao == null || vazio(requisicao.transacaoId()) || vazio(requisicao.tipoOperacao())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "transacaoId e tipoOperacao são obrigatórios");
        }
        IntegracaoBancoCentralModel registro = new IntegracaoBancoCentralModel();
        registro.setId(UUID.randomUUID().toString());
        registro.setTransacaoId(requisicao.transacaoId());
        registro.setTipoOperacao(requisicao.tipoOperacao());
        enviarAoBancoCentral(registro);
        return repository.save(registro);   // INSERT no SQLite
    }

    /** Reenvia ao BC uma operação já registrada (reprocessamento). */
    @Transactional
    public IntegracaoBancoCentralModel reprocessar(String id) {
        IntegracaoBancoCentralModel registro = buscarPorId(id);
        enviarAoBancoCentral(registro);
        return repository.save(registro);   // UPDATE
    }

    @Transactional(readOnly = true)
    public IntegracaoBancoCentralModel buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Operação não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<IntegracaoBancoCentralModel> listar(String status) {
        return vazio(status) ? repository.findAll() : repository.findByStatus(status);
    }

    /** Simulação do envio: preenche status, dataHora e respostaBC. */
    private void enviarAoBancoCentral(IntegracaoBancoCentralModel registro) {
        registro.setDataHora(LocalDateTime.now());
        registro.setStatus(STATUS_CONFIRMADA);
        registro.setRespostaBC("Operação " + registro.getTipoOperacao() + " registrada no Banco Central");
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
