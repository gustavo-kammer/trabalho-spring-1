import { useState } from 'react'

// Foto da receita. Sem foto (ou se a imagem não carregar), mostra a inicial do título.
export default function RecipePhoto({ src, title, className = '' }) {
  const [failedSrc, setFailedSrc] = useState(null)

  if (!src || failedSrc === src) {
    return (
      <div className={`recipe-photo recipe-photo--placeholder ${className}`} aria-hidden="true">
        {title?.trim()[0]?.toUpperCase() || 'R'}
      </div>
    )
  }

  return (
    <img
      className={`recipe-photo ${className}`}
      src={src}
      alt=""
      loading="lazy"
      onError={() => setFailedSrc(src)}
    />
  )
}
