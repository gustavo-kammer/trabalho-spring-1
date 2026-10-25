// Converte um erro da API em { fields, general } para os formulários.
export function formErrorsFrom(error) {
  const fields = { ...(error?.fieldErrors || {}) }
  const message = error?.message || ''

  if (error?.status === 409) {
    if (message.includes('E-mail já cadastrado')) fields.email = message
    if (message.includes('Nickname já está em uso')) fields.nickname = message
  }

  const general = Object.keys(fields).length === 0 ? message : ''
  return { fields, general }
}
