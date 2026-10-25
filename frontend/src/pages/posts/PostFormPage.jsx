import { useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { postsApi } from '../../api/posts'
import { useAuth } from '../../auth/AuthContext'
import CategoryPicker from '../../components/CategoryPicker'
import EmptyState from '../../components/EmptyState'
import ErrorMessage from '../../components/ErrorMessage'
import Field from '../../components/Field'
import Loading from '../../components/Loading'
import RecipePhoto from '../../components/RecipePhoto'
import { useFetch } from '../../hooks/useFetch'
import { formErrorsFrom } from '../../utils/errors'
import './PostFormPage.css'

const EMPTY_FORM = {
  title: '',
  photoUrl: '',
  description: '',
  ingredients: '',
  instructions: '',
  categories: [],
}

const URL_PATTERN = /^https?:\/\/\S+$/i

function toForm(post) {
  return {
    title: post.title ?? '',
    photoUrl: post.photoUrl ?? '',
    description: post.description ?? '',
    ingredients: post.ingredients ?? '',
    instructions: post.instructions ?? '',
    categories: post.categories ?? [],
  }
}

function validate(form) {
  const errors = {}
  const title = form.title.trim()
  const photoUrl = form.photoUrl.trim()

  if (!title) errors.title = 'Campo obrigatório'
  else if (title.length > 200) errors.title = 'Máximo de 200 caracteres'

  if (photoUrl.length > 500) errors.photoUrl = 'Máximo de 500 caracteres'
  else if (photoUrl && !URL_PATTERN.test(photoUrl)) errors.photoUrl = 'Informe um endereço começando com http:// ou https://'

  if (!form.ingredients.trim()) errors.ingredients = 'Campo obrigatório'
  if (!form.instructions.trim()) errors.instructions = 'Campo obrigatório'

  return errors
}

// Mesma página para criar (/receitas/nova) e editar (/receitas/:id/editar).
// Na edição, carrega a receita e confere se é do usuário antes de mostrar o formulário.
export default function PostFormPage() {
  const { id } = useParams()
  const isEdit = Boolean(id)
  const { user } = useAuth()

  const { data: post, loading, error, reload } = useFetch(
    () => (isEdit ? postsApi.get(id) : Promise.resolve(null)),
    [id],
  )

  if (!isEdit) return <PostForm />

  if ((loading && !post) || !user) return <Loading text="Carregando receita..." />

  if (error) {
    if (error.status === 404 || error.status === 400) {
      return (
        <EmptyState title="Receita não encontrada">
          <Link to="/" className="btn">Voltar para as receitas</Link>
        </EmptyState>
      )
    }
    return <ErrorMessage error={error} onRetry={reload} />
  }

  if (post.author.id !== user.id) {
    return (
      <EmptyState title="Você não pode editar esta receita" description="Só quem publicou pode fazer alterações.">
        <Link to={`/receitas/${id}`} className="btn">Ver receita</Link>
      </EmptyState>
    )
  }

  // key: se trocar de receita, o formulário recomeça com os dados da nova
  return <PostForm key={post.id} post={post} />
}

function PostForm({ post }) {
  const isEdit = Boolean(post)
  const navigate = useNavigate()
  const [form, setForm] = useState(() => (post ? toForm(post) : EMPTY_FORM))
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

    // PUT apaga o que não for enviado: manda sempre todos os campos
    const body = {
      title: form.title.trim(),
      photoUrl: form.photoUrl.trim() || null,
      description: form.description.trim() || null,
      ingredients: form.ingredients.trim(),
      instructions: form.instructions.trim(),
      categories: form.categories,
    }

    setIsSubmitting(true)
    try {
      const saved = isEdit ? await postsApi.update(post.id, body) : await postsApi.create(body)
      navigate(`/receitas/${saved.id}`, { replace: true })
    } catch (err) {
      const { fields, general } = formErrorsFrom(err)
      setErrors(fields)
      setGeneralError(general)
      setIsSubmitting(false)
    }
  }

  const photoPreview = URL_PATTERN.test(form.photoUrl.trim()) ? form.photoUrl.trim() : ''

  return (
    <div className="post-form">
      <div className="page-header">
        <div>
          <h1>{isEdit ? 'Editar receita' : 'Nova receita'}</h1>
          <p>{isEdit ? 'Atualize os dados da sua receita.' : 'Compartilhe uma receita com a comunidade.'}</p>
        </div>
      </div>

      <form className="card form" onSubmit={handleSubmit} noValidate>
        {generalError && <div className="alert--error" role="alert">{generalError}</div>}

        <Field label="Título" name="title" value={form.title} onChange={handleChange}
          error={errors.title} maxLength={200} placeholder="Ex.: Lasanha da nonna" />

        <Field label="URL da foto" name="photoUrl" type="url" optional value={form.photoUrl}
          onChange={handleChange} error={errors.photoUrl} maxLength={500}
          hint="Cole o endereço de uma imagem da internet" placeholder="https://..." />
        {photoPreview && (
          <div className="post-form__preview">
            <RecipePhoto src={photoPreview} title={form.title} />
          </div>
        )}

        <Field as="textarea" label="Descrição" name="description" optional rows={3}
          value={form.description} onChange={handleChange} error={errors.description}
          placeholder="Conte um pouco sobre a receita" />

        <Field as="textarea" label="Ingredientes" name="ingredients" rows={6}
          value={form.ingredients} onChange={handleChange} error={errors.ingredients}
          hint="Um ingrediente por linha" placeholder={'500 g de farinha\n3 ovos\nSal a gosto'} />

        <Field as="textarea" label="Modo de preparo" name="instructions" rows={8}
          value={form.instructions} onChange={handleChange} error={errors.instructions}
          hint="Um passo por linha" placeholder={'Misture a farinha e os ovos.\nSove até ficar lisa.'} />

        <CategoryPicker
          label="Categorias"
          hint="Ajuda as pessoas a encontrarem a receita no filtro do feed"
          value={form.categories}
          onChange={(categories) => setForm((current) => ({ ...current, categories }))}
        />

        <div className="form__actions">
          <button type="button" className="btn" onClick={() => navigate(-1)} disabled={isSubmitting}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--primary" disabled={isSubmitting}>
            {isSubmitting ? 'Salvando...' : isEdit ? 'Salvar alterações' : 'Publicar'}
          </button>
        </div>
      </form>
    </div>
  )
}
