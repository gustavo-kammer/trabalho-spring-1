import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { authApi } from '../../api/auth'
import { useAuth } from '../../auth/AuthContext'
import Field from '../../components/Field'
import { formErrorsFrom } from '../../utils/errors'
import AuthLayout from './AuthLayout'

const EMPTY_FORM = {
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
  nickname: '',
}

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

function validate(form) {
  const errors = {}
  const firstName = form.firstName.trim()
  const email = form.email.trim()

  if (!firstName) errors.firstName = 'Campo obrigatório'
  else if (firstName.length > 150) errors.firstName = 'Máximo de 150 caracteres'

  if (form.lastName.trim().length > 150) errors.lastName = 'Máximo de 150 caracteres'

  if (!email) errors.email = 'Campo obrigatório'
  else if (!EMAIL_PATTERN.test(email)) errors.email = 'E-mail inválido'
  else if (email.length > 150) errors.email = 'Máximo de 150 caracteres'

  if (!form.password) errors.password = 'Campo obrigatório'
  else if (form.password.length < 8 || form.password.length > 72)
    errors.password = 'A senha deve ter de 8 a 72 caracteres'

  if (!form.confirmPassword) errors.confirmPassword = 'Campo obrigatório'
  else if (form.confirmPassword !== form.password)
    errors.confirmPassword = 'As senhas não conferem'

  if (form.nickname.trim().length > 100) errors.nickname = 'Máximo de 100 caracteres'

  return errors
}

export default function RegisterPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState(EMPTY_FORM)
  const [errors, setErrors] = useState({})
  const [generalError, setGeneralError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  function handleChange(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setGeneralError('')

    const nextErrors = validate(form)
    setErrors(nextErrors)
    if (Object.keys(nextErrors).length > 0) return

    const email = form.email.trim()
    setIsSubmitting(true)
    try {
      await authApi.register({
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim() || null,
        email,
        password: form.password,
        nickname: form.nickname.trim() || null,
      })
    } catch (error) {
      const { fields, general } = formErrorsFrom(error)
      setErrors(fields)
      setGeneralError(general)
      setIsSubmitting(false)
      return
    }

    // Cadastro não devolve token: entra com o mesmo e-mail e senha.
    try {
      await login(email, form.password)
      navigate('/', { replace: true })
    } catch {
      navigate('/login', { replace: true })
    }
  }

  return (
    <AuthLayout
      title="Criar conta"
      footer={<>Já tem conta? <Link to="/login">Entrar</Link></>}
    >
      <form className="form" onSubmit={handleSubmit} noValidate>
        {generalError && <div className="alert--error" role="alert">{generalError}</div>}

        <div className="auth__row">
          <Field label="Nome" name="firstName" autoComplete="given-name"
            value={form.firstName} onChange={handleChange} error={errors.firstName} />
          <Field label="Sobrenome" name="lastName" optional autoComplete="family-name"
            value={form.lastName} onChange={handleChange} error={errors.lastName} />
        </div>
        <Field label="E-mail" name="email" type="email" autoComplete="email"
          value={form.email} onChange={handleChange} error={errors.email} />
        <Field label="Senha" name="password" type="password" autoComplete="new-password"
          hint="De 8 a 72 caracteres"
          value={form.password} onChange={handleChange} error={errors.password} />
        <Field label="Confirmar senha" name="confirmPassword" type="password"
          autoComplete="new-password"
          value={form.confirmPassword} onChange={handleChange} error={errors.confirmPassword} />
        <Field label="Nickname" name="nickname" optional hint="Ex.: maria_cozinha"
          value={form.nickname} onChange={handleChange} error={errors.nickname} />

        <button type="submit" className="btn btn--primary" disabled={isSubmitting}>
          {isSubmitting ? 'Criando conta...' : 'Criar conta'}
        </button>
      </form>
    </AuthLayout>
  )
}
