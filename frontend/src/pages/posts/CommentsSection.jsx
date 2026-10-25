import { useState } from 'react'
import { Link } from 'react-router-dom'
import { commentsApi } from '../../api/comments'
import { useAuth } from '../../auth/AuthContext'
import Avatar from '../../components/Avatar'
import ErrorMessage from '../../components/ErrorMessage'
import Field from '../../components/Field'
import Loading from '../../components/Loading'
import { useFetch } from '../../hooks/useFetch'
import { formatDate, fullName } from '../../utils/format'
import './CommentsSection.css'

// createdAt e updatedAt saem com alguns milissegundos de diferença mesmo sem edição
function wasEdited(comment) {
  return new Date(comment.updatedAt) - new Date(comment.createdAt) > 1000
}

// Fica no fim do detalhe da receita. O dono da receita pode apagar qualquer comentário.
export default function CommentsSection({ postId, isPostOwner }) {
  const { user } = useAuth()
  const { data: comments, loading, error, reload, setData } = useFetch(
    () => commentsApi.list(postId),
    [postId],
  )

  const [content, setContent] = useState('')
  const [formError, setFormError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const [editingId, setEditingId] = useState(null)
  const [editContent, setEditContent] = useState('')
  const [busyId, setBusyId] = useState(null)
  const [itemError, setItemError] = useState({ id: null, message: '' })

  async function handleSubmit(event) {
    event.preventDefault()
    const text = content.trim()
    if (!text) {
      setFormError('Escreva algo antes de comentar')
      return
    }

    setIsSubmitting(true)
    setFormError('')
    try {
      const created = await commentsApi.create(postId, text)
      setData((current) => [...current, created])
      setContent('')
    } catch (err) {
      setFormError(err.message)
    } finally {
      setIsSubmitting(false)
    }
  }

  function startEdit(comment) {
    setEditingId(comment.id)
    setEditContent(comment.content)
    setItemError({ id: null, message: '' })
  }

  async function handleSaveEdit(comment) {
    const text = editContent.trim()
    if (!text) {
      setItemError({ id: comment.id, message: 'O comentário não pode ficar vazio' })
      return
    }

    setBusyId(comment.id)
    try {
      const updated = await commentsApi.update(comment.id, text)
      setData((current) => current.map((item) => (item.id === updated.id ? updated : item)))
      setEditingId(null)
      setItemError({ id: null, message: '' })
    } catch (err) {
      setItemError({ id: comment.id, message: err.message })
    } finally {
      setBusyId(null)
    }
  }

  async function handleDelete(comment) {
    if (!window.confirm('Excluir este comentário?')) return

    setBusyId(comment.id)
    try {
      await commentsApi.remove(comment.id)
      setData((current) => current.filter((item) => item.id !== comment.id))
    } catch (err) {
      setItemError({ id: comment.id, message: err.message })
    } finally {
      setBusyId(null)
    }
  }

  return (
    <section className="comments" aria-labelledby="comments-title">
      <h2 id="comments-title" className="section-title">
        Comentários{comments ? ` (${comments.length})` : ''}
      </h2>

      <form className="card form comments__form" onSubmit={handleSubmit} noValidate>
        <Field as="textarea" label="Deixe um comentário" name="content" rows={3}
          value={content} onChange={(event) => setContent(event.target.value)}
          error={formError} placeholder="Fez a receita? Conte como ficou!" />
        <div className="form__actions">
          <button type="submit" className="btn btn--primary" disabled={isSubmitting}>
            {isSubmitting ? 'Enviando...' : 'Comentar'}
          </button>
        </div>
      </form>

      {loading && !comments ? (
        <Loading text="Carregando comentários..." />
      ) : error ? (
        <ErrorMessage error={error} onRetry={reload} />
      ) : comments.length === 0 ? (
        <p className="comments__empty">Seja o primeiro a comentar.</p>
      ) : (
        <ul className="comment-list">
          {comments.map((comment) => {
            const isAuthor = comment.author.id === user?.id
            const isEditing = editingId === comment.id
            const isBusy = busyId === comment.id

            return (
              <li key={comment.id} className="comment">
                <Avatar user={comment.author} size="sm" />
                <div className="comment__body">
                  <div className="comment__header">
                    <Link to={`/usuarios/${comment.author.id}`} className="comment__author">
                      {fullName(comment.author)}
                    </Link>
                    <time className="comment__date" dateTime={comment.createdAt}>
                      {formatDate(comment.createdAt)}
                      {wasEdited(comment) && ' · editado'}
                    </time>
                  </div>

                  {isEditing ? (
                    <div className="form comment__edit">
                      <Field as="textarea" label="Editar comentário" name={`edit-${comment.id}`} rows={3}
                        value={editContent} onChange={(event) => setEditContent(event.target.value)} />
                      <div className="form__actions">
                        <button type="button" className="btn btn--sm" onClick={() => setEditingId(null)} disabled={isBusy}>
                          Cancelar
                        </button>
                        <button type="button" className="btn btn--sm btn--primary" onClick={() => handleSaveEdit(comment)} disabled={isBusy}>
                          {isBusy ? 'Salvando...' : 'Salvar'}
                        </button>
                      </div>
                    </div>
                  ) : (
                    <p className="comment__text">{comment.content}</p>
                  )}

                  {itemError.id === comment.id && <p className="field__error">{itemError.message}</p>}

                  {!isEditing && (isAuthor || isPostOwner) && (
                    <div className="comment__actions">
                      {isAuthor && (
                        <button type="button" className="btn btn--ghost btn--sm" onClick={() => startEdit(comment)} disabled={isBusy}>
                          Editar
                        </button>
                      )}
                      <button type="button" className="btn btn--ghost btn--sm btn--danger" onClick={() => handleDelete(comment)} disabled={isBusy}>
                        Excluir
                      </button>
                    </div>
                  )}
                </div>
              </li>
            )
          })}
        </ul>
      )}
    </section>
  )
}
