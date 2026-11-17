# 🧪 Testes dos endpoints

Coleção de testes manuais dos microsserviços — responsável: **Laura** (🟣 api-gateway + 🧪 docs).

Há duas formas de rodar: arquivos **`.http`** (nativos do IntelliJ, recomendado) ou a coleção **Postman**.

## 📁 Arquivos

| Arquivo | Para quê |
|---|---|
| `saldo.http` | 🔵 Endpoints do Saldo (consultar, validar, debitar, estornar) |
| `transferencia.http` | 🟢 Pix, TED e DOC |
| `pagamento.http` | 🟡 Pix, Boleto e Saldo em Conta |
| `integracao-bc.http` | 🟠 Comunicar operação ao BC |
| `gateway.http` | 🟣 As mesmas chamadas, mas passando pelo gateway (:8080) |
| `fluxo-completo.http` | 🎬 Roteiro ponta a ponta para a **demo da defesa** |
| `http-client.env.json` | Variáveis de ambiente dos `.http` (hosts, contaId, chavePix) |
| `Banco-Microsservicos.postman_collection.json` | Coleção Postman equivalente |
| `Banco-Microsservicos-Local.postman_environment.json` | Environment Postman (hosts e variáveis) |

## ▶️ Como rodar os `.http` no IntelliJ

1. Suba os serviços **nesta ordem**: `saldo` → `integracao-bc` → `transferencia` → `pagamento` → `api-gateway`.
2. Abra qualquer arquivo `.http`.
3. No canto superior direito do editor, selecione o ambiente **`local`**.
4. Clique no ▶️ ao lado de cada requisição.
5. Veja o **SQL no console** do serviço (`spring.jpa.show-sql=true`) e confira a linha no `.db` pelo painel **Database** do IntelliJ.

## ▶️ Como rodar no Postman

1. **Import** → selecione os dois arquivos `Banco-Microsservicos*.json`.
2. Escolha o environment **Banco Microsservicos - Local** (canto superior direito).
3. Rode as requisições (ou a pasta inteira com o Collection Runner).

## 🔌 Portas

| Serviço | Porta |
|---|:-:|
| 🟣 api-gateway | 8080 |
| 🟢 transferencia | 8081 |
| 🟡 pagamento | 8082 |
| 🔵 saldo | 8083 |
| 🟠 integracao-bc | 8084 |

## ⚠️ Premissas a confirmar com o grupo

Os corpos das requisições foram derivados do **diagrama de classes** / README. Confirmar com quem implementou cada módulo:

- **Saldo** — o contrato de `validar` / `debitar` / `estornar` está como `{ "contaId", "valor" }`. Alinhar com a Gabriela (é o combinado citado no grupo).
- **Saldo inicial** — o README não define um endpoint para *criar* uma conta/saldo. Para testar débito, pode ser preciso semear um registro inicial no `db_saldo.db` (ou a Gabriela expõe um POST de criação). Confirmar.
- **Transferência / Pagamento** — o cliente envia só os dados de negócio; `id`, `status` e `dataHora` são gerados no Service.
- **Integração BC** — cliente envia `transacaoId` e `tipoOperacao`; o resto é gerado no Service.

Ao fechar os contratos, basta ajustar os corpos aqui e na coleção Postman.
