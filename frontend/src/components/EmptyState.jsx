// children: ação opcional (ex.: um botão "Publicar a primeira")
export default function EmptyState({ title, description, children }) {
  return (
    <div className="state">
      <p className="state__title">{title}</p>
      {description && <p>{description}</p>}
      {children}
    </div>
  )
}
