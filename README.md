<div align="center">

# 🏦 Sistema Bancário em Microsserviços
### Persistência de dados com Spring Boot + SQLite

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-local-003B57?logo=sqlite&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-multi--módulo-C71A36?logo=apachemaven&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IDE-IntelliJ_IDEA-000000?logo=intellijidea&logoColor=white)

**Trabalho em grupo · Defesa de autoria: 18/11/2026**

</div>

---

## 📑 Sumário

1. [Sobre o projeto](#-sobre-o-projeto)
2. [Tecnologias](#-tecnologias)
3. [Arquitetura](#-arquitetura)
4. [Estrutura do repositório](#-estrutura-do-repositório)
5. [Estrutura interna de cada módulo](#-estrutura-interna-de-cada-módulo)
6. [Entidades persistidas](#-entidades-persistidas)
7. [Como a persistência funciona (passo a passo)](#-como-a-persistência-funciona-passo-a-passo)
8. [Endpoints](#-endpoints)
9. [Equipe e responsabilidades](#-equipe-e-responsabilidades)
10. [Fluxo de trabalho no Git](#-fluxo-de-trabalho-no-git)
11. [Como executar](#-como-executar)
12. [Roteiro da defesa](#-roteiro-da-defesa)

---

## 📌 Sobre o projeto

Este projeto implementa a **camada de persistência** do sistema bancário em microsserviços modelado nos diagramas de **pacotes**, **implantação** e **classes** desenvolvidos anteriormente pelo grupo.

O sistema permite ao cliente, por meio do app, **transferir** (Pix, TED, DOC), **pagar** (Pix, Boleto, Saldo em Conta) e **consultar saldo**. Toda operação passa por um **API Gateway** e é tratada por microsserviços independentes, cada um com **seu próprio banco SQLite local**, como previsto no diagrama de pacotes.

### 🎯 Objetivo

Persistir em banco de dados, usando a estrutura do **framework Spring** (Spring Data JPA), as classes que precisam ser armazenadas, e ser capaz de **explicar o processo de persistência passo a passo**.

### ✅ O que é persistido e o que não é

| Persiste (`@Entity`) | Não persiste |
|---|---|
| Classes `*Model` com dados de negócio | `AppCliente`, `ApiGateway`, `BancoCentral` |
| 8 entidades no total (veja [Entidades](#-entidades-persistidas)) | Controllers e Services (apenas lógica) |

---

## 🛠 Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3 (Web, Data JPA) |
| ORM | Hibernate (via Spring Data JPA) |
| Banco de dados | SQLite (um arquivo `.db` por serviço) |
| Dialeto | `hibernate-community-dialects` (`SQLiteDialect`) |
| Driver | `sqlite-jdbc` |
| Gateway | Spring Cloud Gateway |
| Build | Maven multi-módulo |
| IDE | IntelliJ IDEA |
| Testes manuais | Postman / arquivos `.http` |

---

## 🧭 Arquitetura

<img width="853" height="450" alt="image" src="https://github.com/user-attachments/assets/ab9e3824-0c66-4d1a-9e19-82929d4e94eb" />


### 🎨 Legenda de cores (igual aos diagramas)

| Cor | Significado |
|:-:|---|
| ⚪ Cinza | View: app do cliente |
| 🟣 Roxo | API Gateway: porta de entrada |
| 🟢 Verde | Microsserviço de Transferência |
| 🟡 Amarelo | Microsserviço de Pagamento |
| 🔵 Azul | Microsserviço de Saldo |
| 🟠 Laranja | Integração Banco Central (único que fala com o BC) |
| ⚫ Cinza escuro | Banco Central (sistema externo) |

---

## 📂 Estrutura do repositório

```text
banco-microsservicos/
├── 📄 pom.xml                      ← POM pai (módulos + versões)
├── 📄 README.md
├── 📄 .gitignore                   ← ignora target/, .idea/ e *.db
│
├── 🟣 api-gateway/                 → Laura
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/banco/gateway/
│       │   └── ApiGatewayApplication.java
│       └── resources/application.properties
│
├── 🟢 transferencia/               → Isabelle
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/banco/transferencia/...
│       └── resources/application.properties
│
├── 🟡 pagamento/                   → Pedro
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/banco/pagamento/...
│       └── resources/application.properties
│
├── 🔵 saldo/                       → Gabriela
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/banco/saldo/...
│       └── resources/application.properties
│
├── 🟠 integracao-bc/               → Gabriela
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/banco/integracaobc/...
│       └── resources/application.properties
│
└── 🧪 docs/
    ├── diagramas/                  ← pacotes, implantação e classes
    └── testes/                     ← coleção Postman / .http
```

> 💡 Os arquivos `*.db` são gerados automaticamente na primeira execução, dentro da pasta de cada módulo.

---

## 🧱 Estrutura interna de cada módulo

Cada microsserviço segue as camadas **Controller → Service → Repository → Model** dos diagramas (pacotes lilás). O pacote *Repository* é a peça nova que a persistência acrescenta.

### 🔵 Saldo e 🟠 Integração BC (camadas diretas)

```text
com.banco.saldo/
├── SaldoApplication.java
├── controller/   SaldoController.java
├── service/      SaldoService.java
├── repository/   SaldoRepository.java      ← NOVO (persistência)
└── model/        SaldoModel.java           ← @Entity
```

### 🟢 Transferência e 🟡 Pagamento (uma pasta por forma de operação)

As "formas de operação" (pacotes rosa) viram subpacotes:

```text
com.banco.transferencia/
├── TransferenciaApplication.java
├── pix/
│   ├── controller/   TransferenciaPixController.java
│   ├── service/      TransferenciaPixService.java
│   ├── repository/   TransferenciaPixRepository.java
│   └── model/        TransferenciaPixModel.java
├── ted/
│   └── (mesma estrutura: controller, service, repository, model)
├── doc/
│   └── (mesma estrutura: controller, service, repository, model)
└── client/           SaldoClient.java · IntegracaoBcClient.java
```

`pagamento` segue o mesmo desenho, com as pastas `pix/`, `boleto/` e `saldoemconta/`.

---

## 🗃 Entidades persistidas

Os campos vêm direto do **diagrama de classes**.

### 🔵 Saldo → `db_saldo.db`

| Entidade | Tabela | Campos |
|---|---|---|
| `SaldoModel` | `saldo` | `contaId` (PK), `saldoDisponivel`, `ultimaAtualizacao` |

### 🟢 Transferência → `db_transferencia.db`

| Entidade | Campos |
|---|---|
| `TransferenciaPixModel` | `id` (PK), `contaOrigemId`, `chavePixDestino`, `valor`, `status`, `dataHora` |
| `TransferenciaTEDModel` | `id` (PK), `contaOrigemId`, `bancoDestino`, `agenciaDestino`, `contaDestino`, `valor`, `status`, `dataHora` |
| `TransferenciaDOCModel` | `id` (PK), `contaOrigemId`, `bancoDestino`, `agenciaDestino`, `contaDestino`, `valor`, `status`, `dataHora` |

### 🟡 Pagamento → `db_pagamento.db`

| Entidade | Campos |
|---|---|
| `PagamentoPixModel` | `id` (PK), `contaId`, `chavePixDestino`, `valor`, `status`, `dataHora` |
| `PagamentoBoletoModel` | `id` (PK), `contaId`, `codigoBarras`, `vencimento`, `valor`, `status`, `dataHora` |
| `PagamentoSaldoEmContaModel` | `id` (PK), `contaId`, `descricao`, `valor`, `status`, `dataHora` |

### 🟠 Integração BC → `db_integracao_bc.db`

| Entidade | Campos |
|---|---|
| `IntegracaoBancoCentralModel` | `id` (PK), `transacaoId`, `tipoOperacao`, `status`, `dataHora`, `respostaBC` |

> ℹ️ Tipos: `id`, `contaId` e `status` são `String`; `valor` e `saldoDisponivel` são `BigDecimal`; datas são `LocalDateTime` (e `LocalDate` em `vencimento`).

---

## 🔍 Como a persistência funciona (passo a passo)

Esta é a parte central da avaliação. O exemplo usa uma **transferência Pix**, mas o mecanismo é idêntico nos demais módulos.

### 1️⃣ Dependências (`pom.xml`)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-community-dialects</artifactId>
</dependency>
```

### 2️⃣ Configuração (`application.properties`)

```properties
server.port=8081

spring.datasource.url=jdbc:sqlite:db_transferencia.db
spring.datasource.driver-class-name=org.sqlite.JDBC

spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

| Propriedade | O que faz |
|---|---|
| `datasource.url` | Aponta para o arquivo SQLite (criado se não existir) |
| `database-platform` | Ensina o Hibernate a "falar" SQL do SQLite |
| `ddl-auto=update` | Cria/atualiza as tabelas a partir das entidades |
| `show-sql=true` | Mostra no console o SQL gerado (ótimo para a defesa) |

### 3️⃣ Entidade: a classe Model vira tabela

```java
@Entity
@Table(name = "transferencia_pix")
public class TransferenciaPixModel {

    @Id
    private String id;
    private String contaOrigemId;
    private String chavePixDestino;
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataHora;

    // getters e setters
}
```

### 4️⃣ Repository: o acesso ao banco

```java
@Repository
public interface TransferenciaPixRepository
        extends JpaRepository<TransferenciaPixModel, String> {
}
```

É só uma **interface**: o Spring Data gera a implementação com `save`, `findById`, `findAll`, `deleteById` etc.

### 5️⃣ Service: a regra de negócio chama o Repository

```java
@Service
public class TransferenciaPixService {

    private final TransferenciaPixRepository repository;

    public TransferenciaPixService(TransferenciaPixRepository repository) {
        this.repository = repository;   // injeção de dependência
    }

    @Transactional
    public TransferenciaPixModel processarTransferencia(TransferenciaPixModel t) {
        t.setId(UUID.randomUUID().toString());
        t.setStatus("PROCESSANDO");
        t.setDataHora(LocalDateTime.now());
        return repository.save(t);       // INSERT no SQLite
    }
}
```

### 6️⃣ Controller: a porta de entrada HTTP

```java
@RestController
@RequestMapping("/transferencias/pix")
public class TransferenciaPixController {

    private final TransferenciaPixService service;

    public TransferenciaPixController(TransferenciaPixService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferenciaPixModel> efetuarTransferencia(
            @RequestBody TransferenciaPixModel requisicao) {
        return ResponseEntity.ok(service.processarTransferencia(requisicao));
    }
}
```

### 7️⃣ Verificação

1. Subir o módulo e chamar o endpoint no Postman.
2. Ver no console o `insert into transferencia_pix ...` (graças ao `show-sql`).
3. Abrir o arquivo `db_transferencia.db` no **Database do IntelliJ** (ou DB Browser for SQLite) e conferir a linha gravada.

### 🔄 Resumo visual do fluxo

<img width="828" height="402" alt="image" src="https://github.com/user-attachments/assets/6cceca0a-b64e-47a0-82d1-84864b137039" />

---

## 🌐 Endpoints

> Sugestão de rotas, derivadas dos métodos do diagrama de classes. Ajustar se o grupo preferir outros nomes.

### 🔵 Saldo (`:8083`)

| Método | Rota | Operação |
|---|---|---|
| `GET` | `/saldos/{contaId}` | `consultarSaldo` |
| `POST` | `/saldos/validar` | `validarSaldo` |
| `POST` | `/saldos/debitar` | `debitar` |
| `POST` | `/saldos/estornar` | `estornar` |

### 🟢 Transferência (`:8081`)

| Método | Rota | Operação |
|---|---|---|
| `POST` | `/transferencias/pix` | `efetuarTransferencia` (Pix) |
| `POST` | `/transferencias/ted` | `efetuarTransferencia` (TED) |
| `POST` | `/transferencias/doc` | `efetuarTransferencia` (DOC) |

### 🟡 Pagamento (`:8082`)

| Método | Rota | Operação |
|---|---|---|
| `POST` | `/pagamentos/pix` | `efetuarPagamento` (Pix) |
| `POST` | `/pagamentos/boleto` | `efetuarPagamento` (Boleto) |
| `POST` | `/pagamentos/saldo-em-conta` | `efetuarPagamento` (Saldo em Conta) |

### 🟠 Integração Banco Central (`:8084`)

| Método | Rota | Operação |
|---|---|---|
| `POST` | `/integracao-bc/operacoes` | `comunicarOperacao` |

### 🟣 API Gateway (`:8080`)

Recebe as chamadas do app e as encaminha para `/transferencias/**`, `/pagamentos/**` e `/saldos/**`.

### 🔌 Portas

| Serviço | Porta |
|---|:-:|
| 🟣 api-gateway | `8080` |
| 🟢 transferencia | `8081` |
| 🟡 pagamento | `8082` |
| 🔵 saldo | `8083` |
| 🟠 integracao-bc | `8084` |

---

## 👥 Equipe e responsabilidades

> A nota é **individual**. Por isso, todos devem dominar o fluxo completo de persistência, mesmo fora do próprio módulo.

| 👤 Integrante | 📦 Módulo(s) | 🗃 Entidades | 🛠 Responsabilidades |
|---|---|---|---|
| **Gabriela** | 🔵 `saldo`<br>🟠 `integracao-bc` | `SaldoModel`<br>`IntegracaoBancoCentralModel` | • Criar o repositório no GitHub e o POM pai<br>• Implementar o Saldo primeiro (os outros dependem dele)<br>• Implementar a Integração BC (registro e reprocessamento de operações)<br>• Definir o padrão de código dos módulos |
| **Isabelle** | 🟢 `transferencia` | `TransferenciaPixModel`<br>`TransferenciaTEDModel`<br>`TransferenciaDOCModel` | • Persistência das 3 formas de transferência<br>• `SaldoClient` (chama validar, debitar e estornar)<br>• Chamada à Integração BC |
| **Pedro** | 🟡 `pagamento` | `PagamentoPixModel`<br>`PagamentoBoletoModel`<br>`PagamentoSaldoEmContaModel` | • Persistência das 3 formas de pagamento<br>• `SaldoClient` (chama validar, debitar e estornar)<br>• Chamada à Integração BC |
| **Laura** | 🟣 `api-gateway`<br>🧪 `docs/` | nenhuma | • Configurar as rotas do Gateway<br>• Coleção Postman / `.http` com todos os endpoints<br>• Revisar e manter este README<br>• Montar o roteiro de ensaio da defesa |

### ✅ Checklist por integrante

<details>
<summary><b>🔵🟠 Gabriela</b></summary>

- [ ] Repositório criado e colegas adicionados
- [ ] POM pai com os 5 módulos
- [ ] `.gitignore` configurado
- [ ] Saldo: `SaldoModel`, `SaldoRepository`, `SaldoService`, `SaldoController`
- [ ] Saldo: `validar`, `debitar` e `estornar` com `@Transactional`
- [ ] Integração BC: `IntegracaoBancoCentralModel`, Repository, Service, Controller
- [ ] Testado com Postman e conferido no `.db`

</details>

<details>
<summary><b>🟢 Isabelle</b></summary>

- [X] Entidades Pix, TED e DOC
- [X] Repositories, Services e Controllers das 3 formas
- [X] `SaldoClient` integrado ao módulo Saldo
- [X] Cliente da Integração BC
- [X] Testado com Postman e conferido no `.db`

</details>

<details>
<summary><b>🟡 Pedro</b></summary>

- [ ] Entidades Pix, Boleto e SaldoEmConta
- [ ] Repositories, Services e Controllers das 3 formas
- [ ] `SaldoClient` integrado ao módulo Saldo
- [ ] Cliente da Integração BC
- [ ] Testado com Postman e conferido no `.db`

</details>

<details>
<summary><b>🟣🧪 Laura</b></summary>

- [ ] Gateway com as rotas para os 3 serviços
- [ ] Coleção Postman / `.http` completa
- [ ] README revisado
- [ ] Roteiro de ensaio da defesa pronto

</details>

---

## 🌿 Fluxo de trabalho no Git

```text
main            ← versão estável (só recebe via Pull Request)
 ├── feature/saldo
 ├── feature/integracao-bc
 ├── feature/transferencia
 ├── feature/pagamento
 └── feature/gateway-docs
```

**Regras do grupo**

1. Cada pessoa trabalha **somente na sua branch** e no **seu módulo**.
2. Commits pequenos e com mensagem clara, por exemplo `feat(saldo): adiciona SaldoRepository`.
3. Integração via **Pull Request** para a `main`, revisado por pelo menos um colega.
4. Nunca commitar arquivos `*.db`, `target/` ou `.idea/`.

> 📝 Como a defesa é de **autoria**, os commits individuais comprovam quem escreveu cada parte. Façam commits com o próprio usuário.

---

## ▶️ Como executar

### Pré-requisitos

- ☕ JDK 21
- 🧰 Maven (ou o Maven embutido do IntelliJ)
- 💡 IntelliJ IDEA

### Passo a passo

```bash
# 1. Clonar
git clone https://github.com/<usuario>/<repositorio>.git
cd <repositorio>

# 2. Compilar todos os módulos
mvn clean install
```

No **IntelliJ**:

1. `File → Open` e escolher a pasta do projeto (o `pom.xml` pai).
2. Aguardar o Maven importar os 5 módulos.
3. Executar cada `*Application.java` (botão ▶️), **nesta ordem**:

| Ordem | Módulo |
|:-:|---|
| 1 | 🔵 `saldo` |
| 2 | 🟠 `integracao-bc` |
| 3 | 🟢 `transferencia` |
| 4 | 🟡 `pagamento` |
| 5 | 🟣 `api-gateway` |

4. Testar as rotas com a coleção em `docs/testes/`.
5. Abrir os arquivos `.db` pelo painel **Database** do IntelliJ para ver os dados gravados.

---

## 🎤 Roteiro da defesa

**Data:** 18/11/2026 · **Critério:** apresentar o processo de persistência, passo a passo.

Cada integrante deve conseguir explicar, **com o código na tela**:

1. 🧩 Por que cada microsserviço tem o próprio banco (padrão *database per service*).
2. 📦 Quais classes foram persistidas e por quê (e quais não foram).
3. ⚙️ O que cada propriedade do `application.properties` faz.
4. 🏷️ Como `@Entity`, `@Table` e `@Id` transformam a classe em tabela.
5. 🔌 Como o `JpaRepository` gera o acesso ao banco sem implementação manual.
6. 🔁 O caminho completo: **Controller → Service → Repository → Hibernate → SQLite**.
7. 🔒 Para que serve o `@Transactional` (ex.: `debitar` e `estornar`).
8. 👀 Demonstração ao vivo: requisição no Postman, SQL no console e linha no `.db`.

---

<div align="center">

**Equipe:** Isabelle · Gabriela · Pedro · Laura

*Projeto acadêmico, Engenharia de Software, PUCPR*

</div>
