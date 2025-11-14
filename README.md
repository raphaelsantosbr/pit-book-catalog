# Catálogo de Livros – Projeto PIT

Este é um pequeno sistema Web desenvolvido para a disciplina de Projeto Integrador Transdisciplinar.
O objetivo é manter um catálogo simples de livros, permitindo cadastrar, listar, editar, excluir e buscar itens usando Java e Spring Boot.

## Tecnologias utilizadas
- Java 25
- Spring Boot 3
- Spring Data JPA
- Thymeleaf
- Bootstrap 5
- Banco H2 (em memória)

## Como executar
1. Certifique-se de ter o Java 17+ instalado (projeto foi desenvolvido com Java 25).
2. Baixe ou clone este repositório.
3. Abra o projeto na IDE de sua preferência
4. Execute a classe `BookCatalogPitApplication`

O sistema iniciará em: `http://localhost:8080/books`

## Funcionalidades
O projesto consiste num CRUD básico usando a arquitetura MVC.

## Banco de dados H2
O banco carrega automaticamente em memória.

Um console estará disponível através da URL:
http://localhost:8080/h2-console

### Credenciais:

**JDBC**: jdbc:h2:mem:catalog

**Usuário**: sa

**Senha**: password

*PS: Por se tratar de um banco em memória, os dados são apagados ao reiniciar a aplicação.*
