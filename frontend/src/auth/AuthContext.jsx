import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { authApi } from '../api/auth'
import { TOKEN_KEY, setUnauthorizedHandler } from '../api/client'
import { usersApi } from '../api/users'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY))
  const [user, setUser] = useState(null)
  const [sessionExpired, setSessionExpired] = useState(false)

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY)
    setToken(null)
    setUser(null)
  }, [])

  // Sessão caiu sozinha (401 com token): desloga e avisa.
  useEffect(() => {
    setUnauthorizedHandler(() => {
      logout()
      setSessionExpired(true)
    })
    return () => setUnauthorizedHandler(null)
  }, [logout])

  // Com token, busca os dados de quem está logado.
  useEffect(() => {
    if (!token) return undefined
    let cancelled = false
    usersApi
      .me()
      .then((data) => {
        if (!cancelled) setUser(data)
      })
      .catch((error) => {
        // conta excluída: o token ainda é válido, mas o usuário não existe mais
        if (!cancelled && error.status === 404) logout()
      })
    return () => {
      cancelled = true
    }
  }, [token, logout])

  const login = useCallback(async (email, password) => {
    const { token: newToken } = await authApi.login(email, password)
    localStorage.setItem(TOKEN_KEY, newToken)
    setSessionExpired(false)
    setToken(newToken)
  }, [])

  const value = useMemo(
    // setUser: usado depois de editar o perfil, para a tela toda ver os dados novos
    () => ({ user, setUser, login, logout, isAuthenticated: Boolean(token), sessionExpired }),
    [user, login, logout, token, sessionExpired],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth precisa estar dentro de <AuthProvider>')
  return context
}
