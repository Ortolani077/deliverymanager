# 🍕 GerenciamentoDelivery API

API RESTful desenvolvida com **Java 17** e **Spring Boot 3** para gerenciamento de pedidos, produtos e usuários em uma aplicação de delivery.  
Pensada para pequenos comércios que desejam digitalizar o processo de atendimento sem burocracia: sem login obrigatório, com pedidos rápidos, entrega simples e pagamento presencial.

---

## 🚀 Funcionalidades

- Cadastro e listagem de produtos
- Criação de pedidos com múltiplos itens
- Cálculo automático do total com taxa de entrega (opcional)
- Escolha entre entrega ou retirada
- Estimativa de tempo de entrega por dia da semana
- Cadastro simplificado de usuários (sem senha)
- Cadastro de endereço completo por pedido
- Segurança com JWT e roles customizadas (Spring Security)
- API pronta para integração com frontend (ex: React, Vue, etc.)

---

## 🧑‍💻 Tecnologias Utilizadas

- Java 17  
- Spring Boot 3.2.4  
- Spring Data JPA  
- Spring Security + JWT (com chaves RSA)  
- PostgreSQL  
- Maven  
- Docker  
- JUnit 5 + Mockito

---

## 📦 Como Executar

### ✅ Requisitos

- Java 17
- Maven
- PostgreSQL
- Docker (opcional)

### 🔧 Passos

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/GerenciamentoDelivery.git
cd GerenciamentoDelivery
