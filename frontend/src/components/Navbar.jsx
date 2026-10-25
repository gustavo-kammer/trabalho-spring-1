import { Link, NavLink } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

const LINKS = [
  { to: '/', label: 'Início', end: true },
  { to: '/pessoas', label: 'Pessoas' },
  { to: '/favoritos', label: 'Favoritos' },
  { to: '/perfil', label: 'Meu perfil' },
]

export default function Navbar() {
  const { user, logout } = useAuth()

  return (
    <header className="navbar">
      <div className="container navbar__inner">
        <Link to="/" className="navbar__logo">Ricceta Mia</Link>

        <nav className="navbar__links" aria-label="Principal">
          {LINKS.map((link) => (
            <NavLink key={link.to} to={link.to} end={link.end} className="navbar__link">
              {link.label}
            </NavLink>
          ))}
        </nav>

        <div className="navbar__actions">
          {user && <span className="navbar__user">Olá, {user.firstName}</span>}
          <Link to="/receitas/nova" className="btn btn--primary btn--sm">Nova receita</Link>
          <button type="button" className="btn btn--ghost btn--sm" onClick={logout}>Sair</button>
        </div>
      </div>
    </header>
  )
}
