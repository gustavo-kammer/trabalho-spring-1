import { request } from './client'

export const authApi = {
  // devolve { token }
  login: (email, password) =>
    request('/auth/login', { method: 'POST', body: { email, password } }),

  // devolve { id, firstName, email } (sem token)
  register: (data) => request('/auth/register', { method: 'POST', body: data }),
}
