import { useMemo, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { postsApi } from '../../api/posts'
import { useAuth } from '../../auth/AuthContext'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import RecipeGrid from '../../components/RecipeGrid'
import { CATEGORIES, categoryLabel, isCategory } from '../../constants/categories'
import { useFetch } from '../../hooks/useFetch'
import { normalize } from '../../utils/format'
import './FeedPage.css'

export default function FeedPage() {
  const { user } = useAuth()
  const [searchParams, setSearchParams] = useSearchParams()
  const [search, setSearch] = useState('')

  // a categoria fica na URL (/?categoria=ITALIAN) para sobreviver ao recarregar e ao voltar
  const param = searchParams.get('categoria') ?? ''
  const category = isCategory(param) ? param : ''

  const { data: posts, loading, error, reload } = useFetch(
    () => postsApi.list({ category: category || undefined }),
    [category],
  )

  const visiblePosts = useMemo(() => {
    const term = normalize(search.trim())
    if (!posts || !term) return posts ?? []
    return posts.filter((post) => normalize(post.title).includes(term))
  }, [posts, search])

  function selectCategory(value) {
    setSearchParams(value ? { categoria: value } : {})
  }

  function emptyTitle() {
    if (search.trim()) return `Nenhuma receita encontrada para "${search.trim()}"`
    if (category) return `Nenhuma receita ${categoryLabel(category).toLowerCase()} ainda`
    return 'Ainda não há receitas'
  }

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Receitas</h1>
          <p>{user ? `Olá, ${user.firstName}! ` : ''}Descubra o que a comunidade está cozinhando.</p>
        </div>
        <Link to="/receitas/nova" className="btn btn--primary">Nova receita</Link>
      </div>

      <div className="feed-filters">
        <input
          type="search"
          className="search-input"
          placeholder="Buscar pelo título"
          aria-label="Buscar pelo título"
          value={search}
          onChange={(event) => setSearch(event.target.value)}
        />
        <div className="feed-filters__chips" role="group" aria-label="Filtrar por categoria">
          <button
            type="button"
            className="tag tag--button"
            aria-pressed={category === ''}
            onClick={() => selectCategory('')}
          >
            Todas
          </button>
          {CATEGORIES.map((item) => (
            <button
              key={item.value}
              type="button"
              className="tag tag--button"
              aria-pressed={category === item.value}
              onClick={() => selectCategory(item.value)}
            >
              {item.label}
            </button>
          ))}
        </div>
      </div>

      {loading && !posts ? (
        <Loading text="Carregando receitas..." />
      ) : error ? (
        <ErrorMessage error={error} onRetry={reload} />
      ) : (
        <RecipeGrid
          posts={visiblePosts}
          emptyTitle={emptyTitle()}
          emptyAction={
            !search.trim() && !category && (
              <Link to="/receitas/nova" className="btn btn--primary">Publicar a primeira</Link>
            )
          }
        />
      )}
    </>
  )
}
