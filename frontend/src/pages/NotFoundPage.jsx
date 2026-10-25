import { Link } from 'react-router-dom'
import EmptyState from '../components/EmptyState'

export default function NotFoundPage() {
  return (
    <EmptyState title="Página não encontrada" description="O endereço pode estar errado ou a página não existe mais.">
      <Link to="/" className="btn btn--primary">Ir para o início</Link>
    </EmptyState>
  )
}
