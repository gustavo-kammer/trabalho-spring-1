import { useState } from 'react'
import { Link, Navigate, useParams } from 'react-router-dom'
import { followersApi } from '../../api/followers'
import { postsApi } from '../../api/posts'
import { usersApi } from '../../api/users'
import { useAuth } from '../../auth/AuthContext'
import EmptyState from '../../components/EmptyState'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import RecipeGrid from '../../components/RecipeGrid'
import { useFetch } from '../../hooks/useFetch'
import ProfileHeader from './ProfileHeader'

export default function UserProfilePage() {
  const { id } = useParams()
  const { user } = useAuth()
  const { data, loading, error, reload, setData } = useFetch(
    () =>
      Promise.all([
        usersApi.get(id),
        postsApi.list({ userId: id }),
        followersApi.followers(id),
        followersApi.following(id),
      ]).then(([profile, posts, followers, following]) => ({ profile, posts, followers, following })),
    [id],
  )
  const [isFollowBusy, setIsFollowBusy] = useState(false)
  const [followError, setFollowError] = useState('')

  if (user && String(user.id) === id) return <Navigate to="/perfil" replace />
  if (loading && !data) return <Loading text="Carregando perfil..." />

  if (error) {
    if (error.status === 404 || error.status === 400) {
      return (
        <EmptyState title="Usuário não encontrado" description="A conta pode ter sido excluída.">
          <Link to="/pessoas" className="btn">Ver pessoas</Link>
        </EmptyState>
      )
    }
    return <ErrorMessage error={error} onRetry={reload} />
  }

  // a API não diz se eu sigo a pessoa: procuro meu id na lista de seguidores dela
  const isFollowing = data.followers.some((follower) => follower.id === user?.id)

  async function handleFollow() {
    setIsFollowBusy(true)
    setFollowError('')
    try {
      if (isFollowing) await followersApi.unfollow(id)
      else await followersApi.follow(id)
      const followers = await followersApi.followers(id)
      setData((current) => ({ ...current, followers }))
    } catch (err) {
      setFollowError(err.message)
    } finally {
      setIsFollowBusy(false)
    }
  }

  const { profile } = data

  return (
    <>
      <ProfileHeader
        profile={profile}
        postsCount={data.posts.length}
        followersCount={data.followers.length}
        followingCount={data.following.length}
        actions={
          <button
            type="button"
            className={`btn${isFollowing ? ' btn--active' : ' btn--primary'}`}
            aria-pressed={isFollowing}
            onClick={handleFollow}
            disabled={isFollowBusy}
          >
            {isFollowing ? 'Seguindo' : 'Seguir'}
          </button>
        }
      />
      {followError && <ErrorMessage message={followError} />}

      <h2 className="section-title">Receitas de {profile.firstName}</h2>
      <RecipeGrid posts={data.posts} emptyTitle={`${profile.firstName} ainda não publicou receitas`} />
    </>
  )
}
