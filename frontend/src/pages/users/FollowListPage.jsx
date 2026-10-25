import { Link, NavLink, useParams } from 'react-router-dom'
import { followersApi } from '../../api/followers'
import { usersApi } from '../../api/users'
import { useAuth } from '../../auth/AuthContext'
import EmptyState from '../../components/EmptyState'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import UserCard from '../../components/UserCard'
import { useFetch } from '../../hooks/useFetch'

// Uma página só para as duas listas. type: 'followers' (seguidores) ou 'following' (seguindo).
export default function FollowListPage({ type }) {
  const { id } = useParams()
  const { user } = useAuth()
  const { data, loading, error, reload } = useFetch(
    () =>
      Promise.all([
        usersApi.get(id),
        type === 'followers' ? followersApi.followers(id) : followersApi.following(id),
      ]).then(([profile, people]) => ({ profile, people })),
    [id, type],
  )

  const isMe = user && String(user.id) === id
  const profileLink = isMe ? '/perfil' : `/usuarios/${id}`

  if (loading && !data) return <Loading />

  if (error) {
    if (error.status === 404 || error.status === 400) {
      return (
        <EmptyState title="Usuário não encontrado">
          <Link to="/pessoas" className="btn">Ver pessoas</Link>
        </EmptyState>
      )
    }
    return <ErrorMessage error={error} onRetry={reload} />
  }

  const name = isMe ? 'Você' : data.profile.firstName
  const isFollowers = type === 'followers'
  const title = isMe
    ? isFollowers ? 'Seus seguidores' : 'Quem você segue'
    : isFollowers ? `Seguidores de ${name}` : `Quem ${name} segue`
  const emptyTitle = isFollowers ? `${name} ainda não tem seguidores` : `${name} ainda não segue ninguém`

  return (
    <>
      <Link to={profileLink} className="link-back">← Voltar ao perfil</Link>
      <div className="page-header">
        <h1>{title}</h1>
      </div>

      <nav className="tabs" aria-label="Seguidores e seguindo">
        <NavLink to={`/usuarios/${id}/seguidores`}>Seguidores</NavLink>
        <NavLink to={`/usuarios/${id}/seguindo`}>Seguindo</NavLink>
      </nav>

      {data.people.length === 0 ? (
        <EmptyState title={emptyTitle} />
      ) : (
        <div className="user-grid">
          {data.people.map((person) => (
            <UserCard key={person.id} user={person} />
          ))}
        </div>
      )}
    </>
  )
}
