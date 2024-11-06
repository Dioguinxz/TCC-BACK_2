# Sistema de Cadastro de Usuários e Tarefas

Este é o backend da plataforma Simple, um sistema projetado para o cadastro de usuários e gerenciamento de tarefas. Desenvolvido com Java e Spring Boot, o sistema oferece uma interface intuitiva para realizar operações CRUD (Criar, Ler, Atualizar e Deletar) nos dados dos usuários e tarefas.

Os usuários podem se cadastrar, gerenciar suas informações e acompanhar suas tarefas de forma organizada, tornando a experiência de gerenciamento mais simples e eficaz. Com suporte para autenticação e autorização, a plataforma garante criptografia e segurança dos dados dos usuários.

## Tecnologias Utilizadas

- **Java**: Linguagem de programação utilizada.
- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Spring Security**: Módulo para autenticação e autorização de usuários.
- **Spring Email**: Envio de e-mails de notificação.
- **JWT (JSON Web Token)**: Implementação de autenticação baseada em token.
- **MongoDB**: Banco de dados NoSQL para armazenamento de dados.
- **Testes Unitários**: Implementação de testes para garantir a qualidade do código.

## Funcionalidades

- **Cadastro de Usuário**: Os usuários podem se cadastrar com nome, e-mail e senha.
- **Gerenciamento de Tarefas**: Os usuários podem criar e gerenciar suas tarefas, que incluem:
  - Nome da tarefa
  - Descrição da tarefa
  - Data final
  - Status (concluída ou pendente)

## Estrutura do Projeto

O projeto está organizado em pacotes, cada um com uma responsabilidade específica:

- **config**: Configurações da aplicação, incluindo configurações de segurança e envio de e-mails.
- **controller**: Controladores que gerenciam as requisições HTTP e as interações com os serviços.
- **dto**: Objetos de Transferência de Dados utilizados para comunicação entre a API e o cliente.
- **entity**: Entidades que representam as tabelas do banco de dados.
- **repository**: Repositórios que gerenciam a persistência dos dados no MongoDB.
- **security**: Configurações de segurança, incluindo autenticação e autorização.
- **service**: Lógica de negócios da aplicação.

## Uso da API

A API está estruturada seguindo o padrão REST.

## Testes

Testes unitários foram implementados para garantir a funcionalidade e a integridade do código.
