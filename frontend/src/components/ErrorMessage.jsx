// Aceita um ApiError (error) ou um texto (message). Com onRetry, mostra "Tentar novamente".
export default function ErrorMessage({ error, message, onRetry }) {
  return (
    <div className="alert--error error-box" role="alert">
      <p>{message || error?.message || 'Algo deu errado.'}</p>
      {onRetry && (
        <button type="button" className="btn btn--sm" onClick={onRetry}>
          Tentar novamente
        </button>
      )}
    </div>
  )
}
