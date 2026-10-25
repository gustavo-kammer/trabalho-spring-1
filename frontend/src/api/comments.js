import { request } from './client'

export const commentsApi = {
  // mais antigos primeiro
  list: (postId) => request(`/posts/${postId}/comments`),

  create: (postId, content) =>
    request(`/posts/${postId}/comments`, { method: 'POST', body: { content } }),

  // só o autor do comentário
  update: (id, content) => request(`/comments/${id}`, { method: 'PUT', body: { content } }),

  // autor do comentário ou autor da receita
  remove: (id) => request(`/comments/${id}`, { method: 'DELETE' }),
}
