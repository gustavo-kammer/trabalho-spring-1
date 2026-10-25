import { Link } from 'react-router-dom'
import Avatar from '../../components/Avatar'
import CategoryTags from '../../components/CategoryTags'
import { formatMonthYear, fullName } from '../../utils/format'
import './Profile.css'

// Cabeçalho comum do "Meu perfil" e do perfil de outra pessoa.
// email só é passado no próprio perfil; actions são os botões do lado direito.
export default function ProfileHeader({ profile, email, postsCount, followersCount, followingCount, actions }) {
  return (
    <section className="profile card">
      <Avatar user={profile} size="lg" />

      <div className="profile__info">
        <h1>{fullName(profile)}</h1>
        {profile.nickname && <p className="muted">@{profile.nickname}</p>}
        {email && <p className="muted">{email}</p>}
        {profile.cityRegion && <p className="profile__city">{profile.cityRegion}</p>}
        {profile.bio && <p className="profile__bio">{profile.bio}</p>}
        <CategoryTags categories={profile.preferredCategories} />
        <p className="profile__since">Membro desde {formatMonthYear(profile.createdAt)}</p>
      </div>

      <div className="profile__side">
        <ul className="profile__stats">
          <li>
            <strong>{postsCount}</strong> {postsCount === 1 ? 'receita' : 'receitas'}
          </li>
          <li>
            <Link to={`/usuarios/${profile.id}/seguidores`}>
              <strong>{followersCount}</strong> {followersCount === 1 ? 'seguidor' : 'seguidores'}
            </Link>
          </li>
          <li>
            <Link to={`/usuarios/${profile.id}/seguindo`}>
              <strong>{followingCount}</strong> seguindo
            </Link>
          </li>
        </ul>
        {actions && <div className="profile__actions">{actions}</div>}
      </div>
    </section>
  )
}
