import { useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { favoritesApi } from '../../api/favorites'
import { likesApi } from '../../api/likes'
import { postsApi } from '../../api/posts'
import { useAuth } from '../../auth/AuthContext'
import Avatar from '../../components/Avatar'
import CategoryTags from '../../components/CategoryTags'
import EmptyState from '../../components/EmptyState'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import RecipePhoto from '../../components/RecipePhoto'
import { useFetch } from '../../hooks/useFetch'
import { formatDate, fullName, splitLines } from '../../utils/format'
import CommentsSection from './CommentsSection'
import './PostDetailPage.css'

export default function PostDetailPage() {
  const { id } = useParams()
  const { user } = useAuth()
  const navigate = useNavigate()
  const { data: post, loading, error, reload, setData } = useFetch(() => postsApi.get(id), [id])
  const [busyAction, setBusyAction] = useState(null)
  const [actionError, setActionError] = useState('')

  // Executa uma ação (curtir, arquivar...) com o botão travado e mostra o erro, se houver.
  async function runAction(name, action) {
    setBusyAction(name)
    setActionError('')
    try {
      await action()
    } catch (err) {
      setActionError(err.message)
    } finally {
      setBusyAction(null)
    }
  }

  function handleLike() {
    runAction('like', async () => {
      const likes = post.likedByMe ? await likesApi.unlike(post.id) : await likesApi.like(post.id)
      setData((current) => ({ ...current, totalLikes: likes.total, likedByMe: likes.likedByMe }))
    })
  }

  function handleFavorite() {
    runAction('favorite', async () => {
      if (post.favoritedByMe) await favoritesApi.remove(post.id)
      else await favoritesApi.add(post.id)
      setData((current) => ({ ...current, favoritedByMe: !current.favoritedByMe }))
    })
  }

  function handleToggleStatus() {
    const archiving = post.status === 'ACTIVE'
    if (archiving && !window.confirm('Arquivar esta receita? Ela deixa de aparecer no feed, nos perfis e nos favoritos.')) return
    runAction('status', async () => {
      setData(await postsApi.setStatus(post.id, archiving ? 'ARCHIVED' : 'ACTIVE'))
    })
  }

  function handleDelete() {
    if (!window.confirm('Excluir esta receita? Essa ação não pode ser desfeita.')) return
    runAction('delete', async () => {
      await postsApi.remove(post.id)
      navigate('/', { replace: true })
    })
  }

  if (loading && !post) return <Loading text="Carregando receita..." />

  if (error) {
    // 400 aparece quando o id da URL não é um número
    if (error.status === 404 || error.status === 400) {
      return (
        <EmptyState title="Receita não encontrada" description="Ela pode ter sido excluída ou arquivada.">
          <Link to="/" className="btn">Voltar para as receitas</Link>
        </EmptyState>
      )
    }
    return <ErrorMessage error={error} onRetry={reload} />
  }

  const isOwner = user?.id === post.author.id
  const isArchived = post.status === 'ARCHIVED'
  const ingredients = splitLines(post.ingredients, /\n|;/)
  const steps = splitLines(post.instructions)

  return (
    <article className="post-detail">
      <Link to="/" className="link-back">← Voltar para as receitas</Link>

      <RecipePhoto src={post.photoUrl} title={post.title} className="post-detail__photo" />

      {isArchived && (
        <div className="alert--info" role="status">
          Esta receita está arquivada e só você pode vê-la.
        </div>
      )}

      <header className="post-detail__header">
        <CategoryTags categories={post.categories} />
        <h1>{post.title}</h1>
        <div className="post-detail__byline">
          <Link to={`/usuarios/${post.author.id}`} className="post-detail__author">
            <Avatar user={post.author} size="sm" />
            {fullName(post.author)}
          </Link>
          <span aria-hidden="true">·</span>
          <time dateTime={post.createdAt}>{formatDate(post.createdAt)}</time>
        </div>
      </header>

      <div className="post-detail__actions">
        <button
          type="button"
          className={`btn${post.likedByMe ? ' btn--active' : ''}`}
          aria-pressed={post.likedByMe}
          onClick={handleLike}
          disabled={busyAction === 'like'}
        >
          ♥ {post.likedByMe ? 'Curtido' : 'Curtir'} · {post.totalLikes}
        </button>
        <button
          type="button"
          className={`btn${post.favoritedByMe ? ' btn--active' : ''}`}
          aria-pressed={post.favoritedByMe}
          onClick={handleFavorite}
          disabled={busyAction === 'favorite'}
        >
          ★ {post.favoritedByMe ? 'Favoritada' : 'Favoritar'}
        </button>

        {isOwner && (
          <div className="post-detail__owner">
            <Link to={`/receitas/${post.id}/editar`} className="btn btn--sm">Editar</Link>
            <button type="button" className="btn btn--sm" onClick={handleToggleStatus} disabled={busyAction === 'status'}>
              {isArchived ? 'Reativar' : 'Arquivar'}
            </button>
            <button type="button" className="btn btn--sm btn--danger" onClick={handleDelete} disabled={busyAction === 'delete'}>
              Excluir
            </button>
          </div>
        )}
      </div>

      {actionError && <ErrorMessage message={actionError} />}

      {post.description && <p className="post-detail__description">{post.description}</p>}

      <div className="post-detail__content">
        <section className="card">
          <h2>Ingredientes</h2>
          <ul className="ingredient-list">
            {ingredients.map((item, index) => (
              <li key={index}>{item}</li>
            ))}
          </ul>
        </section>
        <section className="card">
          <h2>Modo de preparo</h2>
          <ol className="step-list">
            {steps.map((step, index) => (
              <li key={index}>{step}</li>
            ))}
          </ol>
        </section>
      </div>

      <CommentsSection postId={post.id} isPostOwner={isOwner} />
    </article>
  )
}
