import { request } from './client'

// todas devolvem { postId, total, likedByMe }
export const likesApi = {
  get: (postId) => request(`/posts/${postId}/likes`),
  like: (postId) => request(`/posts/${postId}/likes`, { method: 'POST' }),
  unlike: (postId) => request(`/posts/${postId}/likes`, { method: 'DELETE' }),
}
