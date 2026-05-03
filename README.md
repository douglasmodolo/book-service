# Microsserviços com Spring Cloud, Docker e Kubernetes

Projeto desenvolvido durante o curso **Microsserviços 2026 com Spring Cloud Boot, Kubernetes e Docker**.

A arquitetura é composta por microsserviços independentes que se comunicam via Service Discovery, roteamento centralizado por API Gateway, rastreamento distribuído com Zipkin e orquestração local com Docker Compose.

---

## Repositórios

| Repositório | Descrição |
|---|---|
| [naming-server-eureka](https://github.com/douglasmodolo/naming-server-eureka) | Servidor de descoberta de serviços (Eureka Server) |
| [api-gateway-book](https://github.com/douglasmodolo/api-gateway-book) | API Gateway centralizado para roteamento das requisições |
| [exchange-service](https://github.com/douglasmodolo/exchange-service) | Microsserviço responsável pela conversão de moedas |
| [book-service](https://github.com/douglasmodolo/book-service) | Microsserviço responsável pelo gerenciamento de livros |
| [curso-microservicos-compose](https://github.com/douglasmodolo/curso-microservicos-compose) | Docker Compose para orquestração local de todos os serviços |

---

## Arquitetura

```
Cliente
   │
   ▼
API Gateway (8765)
   │
   ├──▶ book-service (8100)
   │
   └──▶ exchange-service (8000)
          │
          └──▶ book-service (via Feign Client)

Todos os serviços se registram no Eureka (8761)
Todos os serviços enviam traces para o Zipkin (9411)
```

---

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Cloud 2025.0.2**
- **Spring Cloud Netflix Eureka** — Service Discovery
- **Spring Cloud Gateway** — API Gateway
- **Spring Cloud OpenFeign** — Comunicação entre microsserviços
- **Spring Data JPA** — Persistência de dados
- **Flyway** — Migrations de banco de dados
- **MySQL 9.3.0** — Banco de dados relacional
- **Resilience4j** — Circuit Breaker e resiliência
- **Micrometer + Zipkin** — Rastreamento distribuído
- **SpringDoc OpenAPI (Swagger)** — Documentação de APIs
- **Docker** — Containerização
- **Docker Compose** — Orquestração local
- **GitHub Actions** — CI/CD (Continuous Deployment)

---

## Serviços e Portas

| Serviço | Porta |
|---|---|
| Eureka Server | 8761 |
| API Gateway | 8765 |
| Exchange Service | 8000 |
| Book Service | 8100 |
| Zipkin | 9411 |
| MySQL (exchange-db) | 3308 |
| MySQL (book-db) | 3310 |

---

## Como executar localmente

### Pré-requisitos

- Docker e Docker Compose instalados

### Subindo o ambiente

```bash
# Clone o repositório do compose
git clone https://github.com/douglasmodolo/curso-microservicos-compose.git
cd curso-microservicos-compose

# Baixa as imagens mais recentes e sobe os containers
docker-compose pull && docker-compose up -d
```

### Verificando os serviços

Após subir, acesse:

- **Eureka Dashboard:** http://localhost:8761
- **Zipkin:** http://localhost:9411
- **API Gateway:** http://localhost:8765
- **Swagger Book Service:** http://localhost:8765/book-service/swagger-ui.html
- **Swagger Exchange Service:** http://localhost:8765/exchange-service/swagger-ui.html

---

## CI/CD

Cada microsserviço possui um workflow de **Continuous Deployment** configurado no GitHub Actions (`.github/workflows/continuous-deployment.yml`).

A cada push na branch `main`, o pipeline executa automaticamente:

1. Checkout do código
2. Build do JAR com Maven (`mvn clean package -DskipTests`)
3. Build da imagem Docker com duas tags:
   - `:latest` — aponta sempre para o build mais recente
   - `:<git-sha>` — tag rastreável vinculada ao commit exato
4. Push de ambas as tags para o Docker Hub

Para atualizar o ambiente local com as imagens mais recentes:

```bash
docker-compose pull && docker-compose up -d
```

### Secrets necessários nos repositórios

| Secret | Descrição |
|---|---|
| `DOCKER_USERNAME` | Usuário do Docker Hub |
| `DOCKER_ACCESS_TOKEN` | Token de acesso do Docker Hub |

---

## Imagens Docker

As imagens são publicadas automaticamente no Docker Hub a cada push na `main`:

| Serviço | Imagem |
|---|---|
| Naming Server | `douglasmodolo/naming-server:latest` |
| API Gateway | `douglasmodolo/api-gateway:latest` |
| Exchange Service | `douglasmodolo/exchange-service:latest` |
| Book Service | `douglasmodolo/book-service:latest` |
