import './AuthPages.css'

// Visual compartilhado entre login e cadastro.
export default function AuthLayout({ title, children, footer }) {
  return (
    <div className="auth">
      <aside className="auth__aside">
        <span className="auth__logo">Ricceta Mia</span>
        <p className="auth__tagline">
          Receitas de casa, compartilhadas com quem gosta de cozinhar.
        </p>
      </aside>
      <main className="auth__main">
        <span className="auth__logo auth__mobile-logo">Ricceta Mia</span>
        <div className="card auth__card">
          <h1 className="auth__title">{title}</h1>
          {children}
          <p className="auth__footer">{footer}</p>
        </div>
      </main>
    </div>
  )
}
