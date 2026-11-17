package com.banco.saldo.config;

import com.banco.saldo.model.SaldoModel;
import com.banco.saldo.repository.SaldoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Semeia uma conta de teste (CONTA-001, R$ 1000,00) na primeira execução,
 * para os arquivos .http / Postman funcionarem sem passos manuais.
 */
@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner semearContaDeTeste(SaldoRepository repository) {
        return args -> {
            if (!repository.existsById("CONTA-001")) {
                repository.save(new SaldoModel("CONTA-001", new BigDecimal("1000.00")));
            }
        };
    }
}
