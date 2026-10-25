// Mesma ordem do enum RecipeCategory da API, com o nome exibido nas telas.
export const CATEGORIES = [
  ['BRAZILIAN', 'Brasileira'],
  ['ITALIAN', 'Italiana'],
  ['JAPANESE', 'Japonesa'],
  ['CHINESE', 'Chinesa'],
  ['MEXICAN', 'Mexicana'],
  ['FRENCH', 'Francesa'],
  ['INDIAN', 'Indiana'],
  ['THAI', 'Tailandesa'],
  ['ARABIC', 'Árabe'],
  ['PORTUGUESE', 'Portuguesa'],
  ['GERMAN', 'Alemã'],
  ['SPANISH', 'Espanhola'],
  ['KOREAN', 'Coreana'],
  ['PERUVIAN', 'Peruana'],
  ['AMERICAN', 'Americana'],
  ['ARGENTINE', 'Argentina'],
  ['GREEK', 'Grega'],
  ['TURKISH', 'Turca'],
  ['VIETNAMESE', 'Vietnamita'],
  ['MOROCCAN', 'Marroquina'],
  ['CARIBBEAN', 'Caribenha'],
  ['AFRICAN', 'Africana'],
  ['MEDITERRANEAN', 'Mediterrânea'],
  ['FUSION', 'Fusion'],
  ['VEGETARIAN', 'Vegetariana'],
  ['VEGAN', 'Vegana'],
  ['GLUTEN_FREE', 'Sem glúten'],
  ['LACTOSE_FREE', 'Sem lactose'],
  ['LOW_CARB', 'Low carb'],
].map(([value, label]) => ({ value, label }))

const LABELS = Object.fromEntries(CATEGORIES.map(({ value, label }) => [value, label]))

export function categoryLabel(value) {
  return LABELS[value] ?? value
}

export function isCategory(value) {
  return value in LABELS
}
