export default function Loading({ text = 'Carregando...' }) {
  return (
    <div className="state" role="status">
      <span className="spinner" aria-hidden="true" />
      <p>{text}</p>
    </div>
  )
}
