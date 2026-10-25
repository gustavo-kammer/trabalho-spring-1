import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import Field from '../../components/Field'
import { useAuth } from '../../auth/AuthContext'
import { formErrorsFrom } from '../../utils/errors'
import AuthLayout from './AuthLayout'

export default function LoginPage() {
  const { login, sessionExpired } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [errors, setErrors] = useState({})
  const [generalError, setGeneralError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  async function handleSubmit(event) {
    event.preventDefault()
    setGeneralError('')

    const nextErrors = {}
    if (!email.trim()) nextErrors.email = 'Campo obrigatório'
    if (!password) nextErrors.password = 'Campo obrigatório'
    setErrors(nextErrors)
    if (Object.keys(nextErrors).length > 0) return

    setIsSubmitting(true)
    try {
      await login(email.trim(), password)
      navigate('/', { replace: true })
    } catch (error) {
      const { fields, general } = formErrorsFrom(error)
      setErrors(fields)
      setGeneralError(general || error.message)
      setIsSubmitting(false)
    }
  }

  return (
    <AuthLayout
      title="Entrar"
      footer={<>Ainda não tem conta? <Link to="/cadastro">Cadastre-se</Link></>}
    >
      <form className="form" onSubmit={handleSubmit} noValidate>
        {sessionExpired && !generalError && (
          <div className="alert--info" role="status">Sua sessão expirou. Entre novamente.</div>
        )}
        {generalError && <div className="alert--error" role="alert">{generalError}</div>}

        <Field
          label="E-mail"
          name="email"
          type="email"
          autoComplete="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          error={errors.email}
        />
        <Field
          label="Senha"
          name="password"
          type="password"
          autoComplete="current-password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          error={errors.password}
        />

        <button type="submit" className="btn btn--primary" disabled={isSubmitting}>
          {isSubmitting ? 'Entrando...' : 'Entrar'}
        </button>
      </form>
    </AuthLayout>
  )
}
