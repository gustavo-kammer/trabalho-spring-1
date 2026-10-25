import { Link } from 'react-router-dom'
import { fullName } from '../utils/format'
import Avatar from './Avatar'
import CategoryTags from './CategoryTags'
import './UserCard.css'

// Aceita UserSummary (seguidores) ou PublicUser (lista de pessoas, que traz cidade e categorias).
export default function UserCard({ user }) {
  return (
    <Link to={`/usuarios/${user.id}`} className="user-card">
      <Avatar user={user} />
      <div className="user-card__info">
        <span className="user-card__name">{fullName(user)}</span>
        {user.nickname && <span className="user-card__meta">@{user.nickname}</span>}
        {user.cityRegion && <span className="user-card__meta">{user.cityRegion}</span>}
        {user.preferredCategories?.length > 0 && (
          <CategoryTags categories={user.preferredCategories} max={3} />
        )}
      </div>
    </Link>
  )
}
