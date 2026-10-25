import { request } from './client'

export const usersApi = {
  // UserResponse (com e-mail): só o próprio perfil
  me: () => request('/users/me'),

  // Envie todos os campos: o que for null é apagado
  updateMe: (data) => request('/users/me', { method: 'PUT', body: data }),

  deleteMe: () => request('/users/me', { method: 'DELETE' }),

  myFavorites: () => request('/users/me/favorites'),

  // PublicUserResponse (sem e-mail)
  list: () => request('/users'),
  get: (id) => request(`/users/${id}`),
}
