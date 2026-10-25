import { request, toQuery } from './client'

export const postsApi = {
  // só receitas ativas, mais novas primeiro; filtros opcionais
  list: ({ userId, category } = {}) => request(`/posts${toQuery({ userId, category })}`),

  get: (id) => request(`/posts/${id}`),

  create: (data) => request('/posts', { method: 'POST', body: data }),

  // Envie todos os campos: categories omitido deixa a receita sem categorias
  update: (id, data) => request(`/posts/${id}`, { method: 'PUT', body: data }),

  // 'ACTIVE' ou 'ARCHIVED'
  setStatus: (id, status) => request(`/posts/${id}/status`, { method: 'PATCH', body: { status } }),

  remove: (id) => request(`/posts/${id}`, { method: 'DELETE' }),
}
