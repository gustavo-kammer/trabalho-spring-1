# Ricceta Mia — Front-end

React + Vite. Consome a API Spring da raiz do repositório.

## Rodar

1. Suba a API na raiz do projeto: `.\mvnw.cmd spring-boot:run`
2. Aqui em `frontend/`:
   ```powershell
   npm install
   npm run dev
   ```
3. Abra <http://localhost:5173>. A porta é fixa porque é a única liberada no CORS da API.

Para usar outro endereço da API, crie `frontend/.env.local` com `VITE_API_URL=http://...`.

## Comandos

- `npm run dev`: servidor de desenvolvimento
- `npm run build`: gera a versão final em `dist/`
- `npm run lint`: confere o código com o oxlint

## Organização

```
src/
├── api/          uma função por rota da API; só o client.js chama fetch
├── auth/         AuthContext (useAuth) e proteção das rotas
├── components/   peças usadas em várias telas (Field, RecipeCard, Navbar...)
├── constants/    categorias com o nome em português
├── hooks/        useFetch: carregando, erro e dados de cada tela
├── pages/
│   ├── auth/     login e cadastro
│   ├── posts/    feed, detalhe, comentários, nova/editar receita, favoritos
│   └── users/    meu perfil, editar perfil, perfil de outra pessoa, seguidores, pessoas
├── styles/       tokens.css (cores e espaços) e global.css (classes comuns)
└── utils/        formatação de datas e nomes, tratamento de erros da API
```

## Rotas

| Rota | Tela |
|------|------|
| `/login`, `/cadastro` | Entrar e criar conta |
| `/` | Feed de receitas, com filtro por categoria (`?categoria=ITALIAN`) e busca |
| `/receitas/nova`, `/receitas/:id/editar` | Publicar e editar receita |
| `/receitas/:id` | Detalhe com curtir, favoritar e comentários |
| `/favoritos` | Receitas favoritadas |
| `/perfil`, `/perfil/editar` | Meu perfil |
| `/pessoas` | Lista de usuários |
| `/usuarios/:id` | Perfil de outra pessoa, com seguir |
| `/usuarios/:id/seguidores`, `/usuarios/:id/seguindo` | Listas de seguidores |
