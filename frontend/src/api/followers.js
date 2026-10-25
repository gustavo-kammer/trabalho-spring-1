import { request } from './client'

export const followersApi = {
  follow: (userId) => request(`/users/${userId}/follow`, { method: 'POST' }),
  unfollow: (userId) => request(`/users/${userId}/follow`, { method: 'DELETE' }),

  // quem segue o usuário
  followers: (userId) => request(`/users/${userId}/followers`),

  // quem o usuário segue
  following: (userId) => request(`/users/${userId}/following`),
}
