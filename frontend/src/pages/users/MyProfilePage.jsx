import { Link } from 'react-router-dom'
import { followersApi } from '../../api/followers'
import { postsApi } from '../../api/posts'
import { useAuth } from '../../auth/AuthContext'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import RecipeGrid from '../../components/RecipeGrid'
import { useFetch } from '../../hooks/useFetch'
import ProfileHeader from './ProfileHeader'

export default function MyProfilePage() {
  const { user } = useAuth()
  if (!user) return <Loading text="Carregando perfil..." />
  return <MyProfile user={user} />
}

function MyProfile({ user }) {
  const { data, loading, error, reload } = useFetch(
    () =>
      Promise.all([
        postsApi.list({ userId: user.id }),
        followersApi.followers(user.id),
        followersApi.following(user.id),
      ]).then(([posts, followers, following]) => ({ posts, followers, following })),
    [user.id],
  )

  if (loading && !data) return <Loading text="Carregando perfil..." />
  if (error) return <ErrorMessage error={error} onRetry={reload} />

  return (
    <>
      <ProfileHeader
        profile={user}
        email={user.email}
        postsCount={data.posts.length}
        followersCount={data.followers.length}
        followingCount={data.following.length}
        actions={
          <>
            <Link to="/perfil/editar" className="btn">Editar perfil</Link>
            <Link to="/receitas/nova" className="btn btn--primary">Nova receita</Link>
          </>
        }
      />

      <h2 className="section-title">Minhas receitas</h2>
      <RecipeGrid
        posts={data.posts}
        emptyTitle="Você ainda não publicou receitas"
        emptyDescription="Receitas arquivadas não aparecem aqui."
        emptyAction={<Link to="/receitas/nova" className="btn btn--primary">Publicar a primeira</Link>}
      />
    </>
  )
}
