# Sistema de Pessoas – API 1 + API 2

Dois projetos Spring Boot 3 / Java 21 / Maven que integram CRUD de Pessoas,
segurança, persistência MySQL e mensageria RabbitMQ.

---

## Projetos

| Projeto        | Porta | Função                                                             |
|----------------|-------|--------------------------------------------------------------------|
| `api1-pessoa`  | 8080  | CRUD Pessoa Física/Jurídica, Security, Producer RabbitMQ, REST Docs |
| `api2-consumer`| 8081  | Consumer RabbitMQ – imprime eventos da API 1 no terminal           |

---

## Pré-requisitos

| Ferramenta  | Versão mínima |
|-------------|---------------|
| Java        | 21            |
| Maven       | 3.9+          |
| MySQL       | 8.0+          |
| RabbitMQ    | 3.12+         |

### Subindo RabbitMQ e MySQL via Docker (forma mais rápida)

```bash
# RabbitMQ com painel de gerenciamento em http://localhost:15672
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management

# MySQL
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=api1_pessoa \
  -p 3306:3306 \
  mysql:8.0
```

---

## Como executar

### API 1 (porta 8080)

```bash
cd api1-pessoa
mvn spring-boot:run
```

> Ao iniciar, o `DataInitializerRunner` cria automaticamente:
> - Usuário **user** (senha: `user123`) — perfil USER (somente GET)
> - Usuário **admin** (senha: `admin123`) — perfil ADMIN (acesso total)
> - 1 registro mockado de Pessoa Física
> - 1 registro mockado de Pessoa Jurídica

### API 2 (porta 8081)

```bash
cd api2-consumer
mvn spring-boot:run
```

> Mantém a conexão aberta ao RabbitMQ. Toda mensagem publicada pela API 1
> aparece no terminal da API 2 com timestamp.

---

### Testes em ambos repositórios

```bash
cd api1-pessoa // (ou api2-consumer)
mvn test
```

> Roda os testes inseridos na pasta test do projeto.


## Autenticação (API 1)

A API 1 usa **HTTP Basic Authentication**.

| Usuário | Senha      | Permissões                         |
|---------|------------|-------------------------------------|
| `user`  | `user123`  | GET em `/api/fisicas` e `/api/juridicas` |
| `admin` | `admin123` | GET, POST, PUT, DELETE              |

---

## Endpoints – API 1

### Pessoas Físicas `/api/fisicas`

| Método | URL                  | Role mínima | Descrição                  |
|--------|----------------------|-------------|----------------------------|
| GET    | `/api/fisicas`       | USER        | Lista todas                |
| GET    | `/api/fisicas/{id}`  | USER        | Busca por ID               |
| POST   | `/api/fisicas`       | ADMIN       | Cria nova Pessoa Física    |
| PUT    | `/api/fisicas/{id}`  | ADMIN       | Atualiza Pessoa Física     |
| DELETE | `/api/fisicas/{id}`  | ADMIN       | Remove Pessoa Física       |

### Pessoas Jurídicas `/api/juridicas`

| Método | URL                    | Role mínima | Descrição                    |
|--------|------------------------|-------------|------------------------------|
| GET    | `/api/juridicas`       | USER        | Lista todas                  |
| GET    | `/api/juridicas/{id}`  | USER        | Busca por ID                 |
| POST   | `/api/juridicas`       | ADMIN       | Cria nova Pessoa Jurídica    |
| PUT    | `/api/juridicas/{id}`  | ADMIN       | Atualiza Pessoa Jurídica     |
| DELETE | `/api/juridicas/{id}`  | ADMIN       | Remove Pessoa Jurídica       |

---

## Exemplos cURL

### Listar Pessoas Físicas (USER)
```bash
curl -u user:user123 http://localhost:8080/api/fisicas
```

### Criar Pessoa Física (ADMIN)
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/fisicas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Oliveira",
    "genero": "Feminino",
    "idade": 25,
    "cpf": "98765432100",
    "endereco": {
      "rua": "Rua das Acácias",
      "numero": "456",
      "complemento": "Casa 2",
      "bairro": "Jardim",
      "cidade": "Rio de Janeiro",
      "estado": "RJ",
      "cep": "20040-020"
    }
  }'
```

### Atualizar Pessoa Física (ADMIN)
```bash
curl -u admin:admin123 -X PUT http://localhost:8080/api/fisicas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Oliveira Santos",
    "genero": "Feminino",
    "idade": 26,
    "cpf": "98765432100",
    "endereco": {
      "rua": "Rua das Acácias",
      "numero": "456",
      "complemento": "Casa 3",
      "bairro": "Jardim",
      "cidade": "Rio de Janeiro",
      "estado": "RJ",
      "cep": "20040-020"
    }
  }'
```

### Deletar Pessoa Física (ADMIN)
```bash
curl -u admin:admin123 -X DELETE http://localhost:8080/api/fisicas/1
```

### Criar Pessoa Jurídica (ADMIN)
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/juridicas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tech Soluções S.A.",
    "genero": "Empresa",
    "idade": 10,
    "cnpj": "98765432000188",
    "endereco": {
      "rua": "Alameda Santos",
      "numero": "200",
      "complemento": "Andar 5",
      "bairro": "Cerqueira César",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01419-000"
    }
  }'
```

### Tentar criar como USER (deve retornar 403)
```bash
curl -u user:user123 -X POST http://localhost:8080/api/fisicas \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste"}'
```

---

## Documentação gerada (Spring REST Docs)

Execute `mvn package` em `api1-pessoa` para gerar o HTML da documentação.
Após o build, o HTML fica disponível em:

```
api1-pessoa/target/generated-docs/index.html
```

E também acessível pela aplicação em execução em:

```
http://localhost:8080/docs/index.html
```

---

## Mensageria – Fluxo

```
API 1 (Producer)                          API 2 (Consumer)
      │                                         │
      │──POST /api/fisicas──┐                   │
      │                     │ cria Pessoa       │
      │                     ▼                   │
      │            RabbitMQ (pessoa-exchange)   │
      │            routing key: pessoa.routingkey
      │                     │                   │
      │                     └──pessoa-queue────▶│
      │                                         │ @RabbitListener
      │                                         │ imprime no terminal:
      │                                         │ [EVENTO RabbitMQ] [timestamp] <mensagem>
```

---

## Herança de Pessoa (JOINED)

```
           pessoa (tabela)
          /               \
pessoa_fisica         pessoa_juridica
   └── cpf               └── cnpj
```

O campo `endereco` é um `@Embeddable` (colunas físicas em `pessoa`).

---

## Estrutura de pacotes – API 1

```
api1-pessoa/
└── src/main/java/com/example/api1pessoa/
    ├── config/
    │   ├── RabbitMQConfig.java      # Fila, exchange, binding, conversor JSON
    │   └── SecurityConfig.java      # HTTP Basic, regras USER/ADMIN, UserDetailsService
    ├── controller/
    │   ├── FisicaController.java    # CRUD /api/fisicas
    │   └── JuridicaController.java  # CRUD /api/juridicas
    ├── dto/
    │   ├── EnderecoDTO.java
    │   ├── FisicaRequestDTO.java
    │   ├── JuridicaRequestDTO.java
    │   └── response/
    │       ├── FisicaResponseDTO.java
    │       └── JuridicaResponseDTO.java
    ├── exception/
    │   ├── GlobalExceptionHandler.java
    │   └── ResourceNotFoundException.java
    ├── model/
    │   ├── Endereco.java            # @Embeddable
    │   ├── Fisica.java              # extends Pessoa, cpf
    │   ├── Juridica.java            # extends Pessoa, cnpj
    │   ├── Pessoa.java              # @Entity @Inheritance(JOINED)
    │   ├── Role.java                # enum USER | ADMIN
    │   └── Usuario.java             # tabela "usuarios" (credenciais)
    ├── repository/
    │   ├── FisicaRepository.java
    │   ├── JuridicaRepository.java
    │   └── UsuarioRepository.java
    ├── runner/
    │   └── DataInitializerRunner.java  # CommandLineRunner – dados mockados
    └── service/
        ├── FisicaService.java
        ├── JuridicaService.java
        └── MessageProducer.java
```

## Estrutura de pacotes – API 2

```
api2-consumer/
└── src/main/java/com/example/api2consumer/
    ├── config/
    │   └── RabbitMQConfig.java      # Mesma fila/exchange da API 1
    └── listener/
        └── PessoaEventListener.java # @RabbitListener – imprime log com timestamp
```
