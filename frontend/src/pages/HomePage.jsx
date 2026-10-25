import { useAuth } from '../auth/AuthContext'

export default function HomePage() {
  const { user, logout } = useAuth()
  return (
    <main style={{ maxWidth: 480, margin: '4rem auto', padding: '0 1rem' }}>
      <div className="card form">
        <h1>Em construção</h1>
        <p>{user ? `Olá, ${user.firstName}!` : 'Carregando...'}</p>
        <button type="button" className="btn" onClick={logout}>Sair</button>
      </div>
    </main>
  )
}
