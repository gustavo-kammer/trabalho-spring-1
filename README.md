# Ricetta Mia

Rede social de registro de atividades culinárias, inspirada no modelo de
registro de atividades do Strava, aplicada à culinária doméstica. Cada preparo
funciona como uma "atividade": o usuário escolhe uma receita, registra foto,
tempo, nota e observações, e compartilha o resultado com quem segue.

## Propósito

Permitir que cozinheiros domésticos registrem o que prepararam, acompanhem sua
evolução na cozinha e compartilhem experiências com outras pessoas que também
cozinham. O foco central é o **preparo realizado**, não apenas a receita
publicada isoladamente.

## Equipe e responsabilidades

| Integrante                     | Área               | Atividades                                                        |
| ------------------------------ | ------------------ | ----------------------------------------------------------------- |
| Giovana Olivo Cittadin         | Contas e perfis    | Cadastro, login, JWT, autorização por papel, manutenção de perfil |
| Gustavo de Almeida Kammer      | Receitas           | CRUD de receitas, ingredientes, categorias, validações            |
| Maria Fernanda Esteves Machado | Preparos e feed    | Registro de preparos, ordenação do feed, paginação, histórico     |
| Nick Kuerten da Silva          | Catálogo e busca   | Administração de categorias/ingredientes, filtros de pesquisa     |
| Rafaela Bez Nicoski            | Interações sociais | Comentários, curtidas, favoritos, seguir/deixar de seguir         |

## Tecnologias

- **Backend:** Spring Boot 3+, Spring Data JPA, Spring Security, JWT, Lombok
- **Banco de dados:** PostgreSQL 12–17
- **Versionamento de schema:** Flyway
- **Documentação da API:** Swagger / OpenAPI
- **Build:** Maven

## Arquitetura

Organizado em camadas por domínio funcional:
