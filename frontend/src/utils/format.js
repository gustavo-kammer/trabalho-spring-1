// A API manda datas sem fuso ("2026-10-23T20:17:10"), lidas como horário local.
export function formatDate(value) {
  if (!value) return ''
  return new Date(value).toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  })
}

// "outubro de 2026"
export function formatMonthYear(value) {
  if (!value) return ''
  return new Date(value).toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' })
}

export function fullName(user) {
  return [user?.firstName, user?.lastName].filter(Boolean).join(' ')
}

// "Maria Silva" -> "MS"
export function initials(user) {
  return [user?.firstName, user?.lastName]
    .filter(Boolean)
    .map((name) => name.trim()[0]?.toUpperCase() ?? '')
    .join('')
    .slice(0, 2)
}

// Ingredientes e modo de preparo são texto livre: um item por linha.
export function splitLines(text, separator = /\n/) {
  return (text ?? '')
    .split(separator)
    .map((line) => line.trim())
    .filter(Boolean)
}

// "1 receita" / "2 receitas"
export function plural(count, singular, pluralForm) {
  return `${count} ${count === 1 ? singular : pluralForm}`
}

// Para buscas: sem acento e sem maiúsculas ("Pão" casa com "pao").
export function normalize(text) {
  return (text ?? '')
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '')
    .toLowerCase()
}
