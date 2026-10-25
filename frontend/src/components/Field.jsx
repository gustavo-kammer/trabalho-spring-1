import { useId } from 'react'

export default function Field({ label, name, error, hint, optional, ...inputProps }) {
  const id = useId()
  const messageId = `${id}-msg`
  return (
    <div className="field">
      <label htmlFor={id}>
        {label}
        {optional && <span className="field__optional"> (opcional)</span>}
      </label>
      <input
        id={id}
        name={name}
        aria-invalid={error ? 'true' : undefined}
        aria-describedby={error || hint ? messageId : undefined}
        {...inputProps}
      />
      {error ? (
        <p id={messageId} className="field__error">{error}</p>
      ) : hint ? (
        <p id={messageId} className="field__hint">{hint}</p>
      ) : null}
    </div>
  )
}
