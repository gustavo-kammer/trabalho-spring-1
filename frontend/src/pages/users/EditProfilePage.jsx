import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { usersApi } from '../../api/users'
import { useAuth } from '../../auth/AuthContext'
import CategoryPicker from '../../components/CategoryPicker'
import Field from '../../components/Field'
import Loading from '../../components/Loading'
import { formErrorsFrom } from '../../utils/errors'
import './Profile.css'

function toForm(user) {
  return {
    firstName: user.firstName ?? '',
    lastName: user.lastName ?? '',
    nickname: user.nickname ?? '',
    cityRegion: user.cityRegion ?? '',
    bio: user.bio ?? '',
    preferredCategories: user.preferredCategories ?? [],
  }
}

function validate(form) {
  const errors = {}
  const firstName = form.firstName.trim()

  if (!firstName) errors.firstName = 'Campo obrigatório'
  else if (firstName.length > 150) errors.firstName = 'Máximo de 150 caracteres'
  if (form.lastName.trim().length > 150) errors.lastName = 'Máximo de 150 caracteres'
  if (form.nickname.trim().length > 100) errors.nickname = 'Máximo de 100 caracteres'
  if (form.cityRegion.trim().length > 150) errors.cityRegion = 'Máximo de 150 caracteres'

  return errors
}

export default function EditProfilePage() {
  const { user } = useAuth()
  if (!user) return <Loading text="Carregando perfil..." />
  return <EditProfileForm user={user} />
}

function EditProfileForm({ user }) {
  const { setUser, logout } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState(() => toForm(user))
  const [errors, setErrors] = useState({})
  const [generalError, setGeneralError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isDeleting, setIsDeleting] = useState(false)
  const [deleteError, setDeleteError] = useState('')

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

    setIsSubmitting(true)
    try {
      // PUT apaga o que não for enviado: manda sempre todos os campos
      const updated = await usersApi.updateMe({
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim() || null,
        nickname: form.nickname.trim() || null,
        cityRegion: form.cityRegion.trim() || null,
        bio: form.bio.trim() || null,
        preferredCategories: form.preferredCategories,
      })
      setUser(updated)
      navigate('/perfil', { replace: true })
    } catch (err) {
      const { fields, general } = formErrorsFrom(err)
      setErrors(fields)
      setGeneralError(general)
      setIsSubmitting(false)
    }
  }

  async function handleDeleteAccount() {
    if (!window.confirm('Excluir sua conta? Você não poderá mais entrar com ela. Essa ação não pode ser desfeita.')) return

    setIsDeleting(true)
    setDeleteError('')
    try {
      await usersApi.deleteMe()
      logout()
    } catch (err) {
      setDeleteError(err.message)
      setIsDeleting(false)
    }
  }

  return (
    <div className="edit-profile">
      <div className="page-header">
        <div>
          <h1>Editar perfil</h1>
          <p>Essas informações aparecem para quem visita o seu perfil.</p>
        </div>
      </div>

      <form className="card form" onSubmit={handleSubmit} noValidate>
        {generalError && <div className="alert--error" role="alert">{generalError}</div>}

        <div className="edit-profile__row">
          <Field label="Nome" name="firstName" autoComplete="given-name"
            value={form.firstName} onChange={handleChange} error={errors.firstName} />
          <Field label="Sobrenome" name="lastName" optional autoComplete="family-name"
            value={form.lastName} onChange={handleChange} error={errors.lastName} />
        </div>
        <Field label="E-mail" name="email" value={user.email} disabled
          hint="O e-mail não pode ser alterado" />
        <div className="edit-profile__row">
          <Field label="Nickname" name="nickname" optional hint="Ex.: maria_cozinha"
            value={form.nickname} onChange={handleChange} error={errors.nickname} />
          <Field label="Cidade ou região" name="cityRegion" optional placeholder="Ex.: São Paulo, SP"
            value={form.cityRegion} onChange={handleChange} error={errors.cityRegion} />
        </div>
        <Field as="textarea" label="Bio" name="bio" optional rows={4}
          placeholder="Conte um pouco sobre você e o que gosta de cozinhar"
          value={form.bio} onChange={handleChange} error={errors.bio} />
        <CategoryPicker
          label="Categorias preferidas"
          value={form.preferredCategories}
          onChange={(preferredCategories) => setForm((current) => ({ ...current, preferredCategories }))}
        />

        <div className="form__actions">
          <button type="button" className="btn" onClick={() => navigate('/perfil')} disabled={isSubmitting}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--primary" disabled={isSubmitting}>
            {isSubmitting ? 'Salvando...' : 'Salvar alterações'}
          </button>
        </div>
      </form>

      <section className="card danger-zone">
        <h2>Excluir conta</h2>
        <p>Sua conta deixa de existir e você não poderá mais entrar com este e-mail.</p>
        {deleteError && <div className="alert--error" role="alert">{deleteError}</div>}
        <button type="button" className="btn btn--danger" onClick={handleDeleteAccount} disabled={isDeleting}>
          {isDeleting ? 'Excluindo...' : 'Excluir minha conta'}
        </button>
      </section>
    </div>
  )
}
