import { request } from './client'

export const usersApi = {
  me: () => request('/users/me'),
}
