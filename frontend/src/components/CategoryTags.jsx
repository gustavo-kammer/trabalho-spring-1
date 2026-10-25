import { categoryLabel } from '../constants/categories'

// Com max, mostra só as primeiras e um "+N" com o resto.
export default function CategoryTags({ categories = [], max }) {
  if (categories.length === 0) return null
  const visible = max ? categories.slice(0, max) : categories
  const hidden = categories.length - visible.length

  return (
    <ul className="tags">
      {visible.map((category) => (
        <li key={category} className="tag">{categoryLabel(category)}</li>
      ))}
      {hidden > 0 && <li className="tag tag--more">+{hidden}</li>}
    </ul>
  )
}
