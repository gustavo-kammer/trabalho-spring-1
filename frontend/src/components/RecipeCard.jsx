import { Link } from 'react-router-dom'
import { formatDate, fullName } from '../utils/format'
import CategoryTags from './CategoryTags'
import RecipePhoto from './RecipePhoto'
import './RecipeCard.css'

export default function RecipeCard({ post }) {
  return (
    <Link to={`/receitas/${post.id}`} className="recipe-card">
      <div className="recipe-card__media">
        <RecipePhoto src={post.photoUrl} title={post.title} />
      </div>
      <div className="recipe-card__body">
        <h3 className="recipe-card__title">{post.title}</h3>
        <p className="recipe-card__author">por {fullName(post.author)}</p>
        <CategoryTags categories={post.categories} max={3} />
        <div className="recipe-card__meta">
          <span className="recipe-card__likes" aria-label={`${post.totalLikes} curtidas`}>
            ♥ {post.totalLikes}
          </span>
          {post.favoritedByMe && <span className="recipe-card__favorite">★ Favorita</span>}
          <span className="recipe-card__date">{formatDate(post.createdAt)}</span>
        </div>
      </div>
    </Link>
  )
}
