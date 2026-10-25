import { CATEGORIES } from '../constants/categories'

// Seleção múltipla de categorias. value é um array de valores do enum (ex.: ['ITALIAN']).
export default function CategoryPicker({ label, hint, value, onChange }) {
  function toggle(category) {
    const next = value.includes(category)
      ? value.filter((item) => item !== category)
      : [...value, category]
    // mantém a ordem da lista, não a ordem dos cliques
    onChange(CATEGORIES.map((item) => item.value).filter((item) => next.includes(item)))
  }

  return (
    <fieldset className="field">
      <legend>
        {label} <span className="field__optional">(opcional)</span>
      </legend>
      {hint && <p className="field__hint">{hint}</p>}
      <div className="tags">
        {CATEGORIES.map((category) => (
          <button
            key={category.value}
            type="button"
            className="tag tag--button"
            aria-pressed={value.includes(category.value)}
            onClick={() => toggle(category.value)}
          >
            {category.label}
          </button>
        ))}
      </div>
    </fieldset>
  )
}
