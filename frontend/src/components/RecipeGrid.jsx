import EmptyState from './EmptyState'
import RecipeCard from './RecipeCard'

// emptyAction: botão ou link mostrado quando a lista está vazia
export default function RecipeGrid({ posts, emptyTitle, emptyDescription, emptyAction }) {
  if (posts.length === 0) {
    return (
      <EmptyState title={emptyTitle} description={emptyDescription}>
        {emptyAction}
      </EmptyState>
    )
  }

  return (
    <div className="recipe-grid">
      {posts.map((post) => (
        <RecipeCard key={post.id} post={post} />
      ))}
    </div>
  )
}
