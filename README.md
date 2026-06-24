# 📚 Biblioteca Manager - API REST

API REST para gerenciamento de biblioteca desenvolvida em **Java com Spring Boot**.

---

## 📌 Sobre o Projeto

Este projeto é a **evolução de um sistema de biblioteca desenvolvido inicialmente em Java puro (console)**. A versão atual foi completamente **migrada para Spring Boot**, implementando uma **API REST** com **persistência em MySQL** e **arquitetura em camadas** (Controller, Service, Repository).

**Principais melhorias da versão atual:**
- ✅ Migração de Java puro para Spring Boot
- ✅ Persistência de dados com MySQL (anteriormente era em memória com HashMap)
- ✅ API REST com endpoints documentados
- ✅ Arquitetura em camadas (Controller → Service → Repository)
- ✅ Injeção de dependências com Spring
- ✅ JPA/Hibernate para mapeamento objeto-relacional

---

## 🎯 Objetivo

O projeto foi desenvolvido com foco em demonstrar habilidades em:
- Desenvolvimento de APIs REST com Spring Boot
- Persistência de dados com JPA/Hibernate
- Boas práticas de arquitetura e organização de código
- Migração de aplicações Java para Spring Boot

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| **Java** | 17 | Linguagem principal |
| **Spring Boot** | 4.1.0 | Framework web |
| **Spring Data JPA** | - | Persistência de dados |
| **Hibernate** | - | ORM |
| **MySQL** | 8.0.46 | Banco de dados |
| **Lombok** | 1.18.46 | Redução de boilerplate |
| **Maven** | - | Gerenciador de dependências |

---

## 📋 Funcionalidades

### 📌 Usuários
- ✅ Criar usuário
- ✅ Listar todos os usuários
- ✅ Buscar usuário por ID
- ✅ Atualizar usuário (campos opcionais)
- ✅ Deletar usuário

### 📌 Livros
- ✅ Criar livro
- ✅ Listar todos os livros
- ✅ Buscar livro por ID
- ✅ Atualizar livro (campos opcionais)
- ✅ Deletar livro

### 📌 Empréstimos
- ✅ Emprestar livro (verifica disponibilidade)
- ✅ Devolver livro (calcula multa)
- ✅ Listar todos os empréstimos
- ✅ Buscar empréstimo por ID
- ✅ Deletar empréstimo

---

## ⚙️ Regras de Negócio

| Regra | Descrição |
|-------|-----------|
| **Multa por atraso** | Calculada automaticamente na devolução |
| **Cortesia** | 3 dias de cortesia (não cobra multa) |
| **Limite máximo** | Multa limitada a R$ 100,00 |
| **Bloqueio** | Usuário bloqueado após 30 dias de atraso |
| **Disponibilidade** | Livro fica indisponível durante empréstimo |

---

## 📌 Endpoints da API

### 🔹 Usuários (`/user`)

| Método | Endpoint | Descrição | Corpo da Requisição |
|--------|----------|-----------|---------------------|
| POST | `/user` | Criar usuário | `{ "name", "email", "type" }` |
| GET | `/user` | Listar todos | - |
| GET | `/user/{id}` | Buscar por ID | - |
| PUT | `/user/{id}` | Atualizar usuário | Campos opcionais |
| DELETE | `/user/{id}` | Deletar usuário | - |

### 🔹 Livros (`/book`)

| Método | Endpoint | Descrição | Corpo da Requisição |
|--------|----------|-----------|---------------------|
| POST | `/book` | Criar livro | `{ "name", "author", "gender", "publicationYear" }` |
| GET | `/book` | Listar todos | - |
| GET | `/book/{id}` | Buscar por ID | - |
| PUT | `/book/{id}` | Atualizar livro | Campos opcionais |
| DELETE | `/book/{id}` | Deletar livro | - |

### 🔹 Empréstimos (`/loan`)

| Método | Endpoint | Descrição | Corpo da Requisição |
|--------|----------|-----------|---------------------|
| POST | `/loan/borrowbook` | Emprestar livro | `{ "userId", "bookId", "loanDays" }` |
| POST | `/loan/return` | Devolver livro | `{ "loanId", "returnDate" }` |
| GET | `/loan` | Listar todos | - |
| GET | `/loan/{id}` | Buscar por ID | - |
| DELETE | `/loan/{id}` | Deletar empréstimo | - |

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17+
- MySQL 8+
- Maven
- Postman (para testes)

### Passo a passo

**1. Clone o repositório**

```bash
git clone https://github.com/Heitorivonica/biblioteca-api.git
cd biblioteca-api
Criar Usuário
{
    "name": "João Silva",
    "email": "joao@email.com",
    "type": "STUDENT"
}

Emprestar Livro
{
    "userId": 1,
    "bookId": 1,
    "loanDays": 14
}


Devolver Livro
{
    "loanId": 1,
    "returnDate": "2026-07-07"
}

📈 Evolução do Projeto
Versão	Tecnologia	Banco de Dados	Interface
v1.0	Java puro	Em memória (HashMap)	Console
v2.0 (atual)	Spring Boot	MySQL	API REST
Melhorias implementadas:

✅ Persistência real com banco de dados

✅ API REST com endpoints

✅ Arquitetura em camadas

✅ Injeção de dependências

✅ ORM com JPA/Hibernate

✅ Tratamento de exceções
