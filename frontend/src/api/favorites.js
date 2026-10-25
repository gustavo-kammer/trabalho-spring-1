import { request } from './client'

// as duas devolvem 204 (sem corpo)
export const favoritesApi = {
  add: (postId) => request(`/posts/${postId}/favorites`, { method: 'POST' }),
  remove: (postId) => request(`/posts/${postId}/favorites`, { method: 'DELETE' }),
}
