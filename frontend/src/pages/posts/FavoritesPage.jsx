import { Link } from 'react-router-dom'
import { usersApi } from '../../api/users'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import RecipeGrid from '../../components/RecipeGrid'
import { useFetch } from '../../hooks/useFetch'

export default function FavoritesPage() {
  const { data: posts, loading, error, reload } = useFetch(() => usersApi.myFavorites(), [])

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Favoritos</h1>
          <p>As receitas que você guardou para fazer depois.</p>
        </div>
      </div>

      {loading && !posts ? (
        <Loading text="Carregando favoritos..." />
      ) : error ? (
        <ErrorMessage error={error} onRetry={reload} />
      ) : (
        <RecipeGrid
          posts={posts}
          emptyTitle="Você ainda não favoritou receitas"
          emptyDescription="Use o botão ★ Favoritar em uma receita para guardá-la aqui."
          emptyAction={<Link to="/" className="btn btn--primary">Explorar receitas</Link>}
        />
      )}
    </>
  )
}
