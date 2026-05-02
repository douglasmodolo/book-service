# 📚 Microsserviços com Spring Cloud, Kubernetes e Docker

> Projeto desenvolvido durante o curso **Microsserviços 2026 com Spring Cloud Boot, Kubernetes e Docker**.

Este repositório faz parte de um ecossistema de microsserviços interligados. A arquitetura é composta por 4 serviços que trabalham juntos para demonstrar os principais padrões de microsserviços com o ecossistema Spring Cloud.

---

## 🏗️ Arquitetura

```
                        ┌─────────────────────┐
                        │   naming-server-     │
                        │      eureka          │
                        │  (Service Registry)  │
                        └──────────┬──────────┘
                                   │ registra/descobre
          ┌────────────────────────┼────────────────────────┐
          │                        │                        │
 ┌────────▼────────┐    ┌──────────▼──────┐    ┌───────────▼──────┐
 │   api-gateway-  │    │  book-service   │    │ exchange-service  │
 │      book       │───▶│  (porta 8100)   │───▶│  (porta 8000)    │
 │  (porta 8765)   │    │                 │    │                  │
 └─────────────────┘    └─────────────────┘    └──────────────────┘
```

---

## 📦 Repositórios

| Serviço | Repositório | Responsabilidade |
|---|---|---|
| 🔀 **API Gateway** | [api-gateway-book](https://github.com/douglasmodolo/api-gateway-book) | Ponto único de entrada, roteamento e documentação agregada |
| 📖 **Book Service** | [book-service](https://github.com/douglasmodolo/book-service) | CRUD de livros, integração com Exchange via Feign |
| 💱 **Exchange Service** | [exchange-service](https://github.com/douglasmodolo/exchange-service) | Serviço de câmbio/conversão de moedas |
| 🗂️ **Naming Server** | [naming-server-eureka](https://github.com/douglasmodolo/naming-server-eureka) | Service Registry com Netflix Eureka |

---

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Cloud 2025.0.2**
- **Spring Cloud Gateway (WebFlux)**
- **Netflix Eureka** — Service Discovery
- **OpenFeign** — Comunicação entre serviços
- **Resilience4j** — Circuit Breaker / Tolerância a falhas
- **Spring Data JPA + MySQL** — Persistência (book-service)
- **Flyway** — Migrations de banco de dados
- **SpringDoc OpenAPI 2.8.9** — Documentação Swagger UI
- **Spring Boot Actuator** — Monitoramento e health check
- **Maven** — Gerenciamento de dependências

---

## 🔍 Detalhes dos Serviços

### 🗂️ Naming Server (Eureka)
Servidor de registro e descoberta de serviços. Todos os demais microsserviços se registram aqui, permitindo comunicação por nome lógico ao invés de endereço fixo.

- **Dependências principais:** `spring-cloud-starter-netflix-eureka-server`, `spring-boot-starter-actuator`

### 🔀 API Gateway
Porta de entrada única para toda a aplicação. Roteia requisições para os serviços corretos com base no caminho da URL. Inclui documentação Swagger agregada dos serviços.

- **Dependências principais:** `spring-cloud-starter-gateway-server-webflux`, `spring-cloud-starter-netflix-eureka-client`, `springdoc-openapi-starter-webflux-ui`

### 📖 Book Service
Serviço responsável pelo gerenciamento de livros. Consome o Exchange Service via OpenFeign para obter preços convertidos. Possui resiliência com Resilience4j e banco de dados MySQL com migrations via Flyway.

- **Dependências principais:** `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-cloud-starter-openfeign`, `resilience4j-spring-boot3`, `flyway-core`, `mysql-connector-j`, `springdoc-openapi-starter-webmvc-ui`

### 💱 Exchange Service
Serviço responsável por fornecer taxas de câmbio/conversão de moedas para o Book Service.

---

## 🚀 Como executar

### Pré-requisitos
- Java 21+
- Maven 3.8+
- Docker (opcional, para banco de dados)
- MySQL (para o book-service)

### Ordem de inicialização

> ⚠️ É importante respeitar a ordem abaixo para que o Service Discovery funcione corretamente.

**1. Naming Server (Eureka)**
```bash
git clone https://github.com/douglasmodolo/naming-server-eureka
cd naming-server-eureka
mvn spring-boot:run
```
Acesse o dashboard Eureka em: `http://localhost:8761`

**2. Exchange Service**
```bash
git clone https://github.com/douglasmodolo/exchange-service
cd exchange-service
mvn spring-boot:run
```

**3. Book Service**
```bash
git clone https://github.com/douglasmodolo/book-service
cd book-service
mvn spring-boot:run
```

**4. API Gateway**
```bash
git clone https://github.com/douglasmodolo/api-gateway-book
cd api-gateway-book
mvn spring-boot:run
```

---

## 📖 Documentação (Swagger)

Após subir todos os serviços, a documentação estará disponível via API Gateway:

- **Swagger UI (Gateway):** `http://localhost:8765/swagger-ui.html`
- **Eureka Dashboard:** `http://localhost:8761`

---

## 👤 Autor

**Douglas Modolo**  
[github.com/douglasmodolo](https://github.com/douglasmodolo)
