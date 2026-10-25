import { useMemo, useState } from 'react'
import { usersApi } from '../../api/users'
import { useAuth } from '../../auth/AuthContext'
import EmptyState from '../../components/EmptyState'
import ErrorMessage from '../../components/ErrorMessage'
import Loading from '../../components/Loading'
import UserCard from '../../components/UserCard'
import { useFetch } from '../../hooks/useFetch'
import { fullName, normalize } from '../../utils/format'

export default function PeoplePage() {
  const { user } = useAuth()
  const { data: people, loading, error, reload } = useFetch(() => usersApi.list(), [])
  const [search, setSearch] = useState('')

  const visiblePeople = useMemo(() => {
    const others = (people ?? []).filter((person) => person.id !== user?.id)
    const term = normalize(search.trim())
    if (!term) return others
    return others.filter((person) =>
      normalize(`${fullName(person)} ${person.nickname ?? ''}`).includes(term),
    )
  }, [people, search, user])

  return (
    <>
      <div className="page-header">
        <div>
          <h1>Pessoas</h1>
          <p>Encontre quem cozinha por aqui e acompanhe as receitas.</p>
        </div>
        <input
          type="search"
          className="search-input"
          placeholder="Buscar por nome ou nickname"
          aria-label="Buscar por nome ou nickname"
          value={search}
          onChange={(event) => setSearch(event.target.value)}
        />
      </div>

      {loading && !people ? (
        <Loading text="Carregando pessoas..." />
      ) : error ? (
        <ErrorMessage error={error} onRetry={reload} />
      ) : visiblePeople.length === 0 ? (
        <EmptyState
          title={search.trim() ? `Ninguém encontrado para "${search.trim()}"` : 'Ainda não há outras pessoas por aqui'}
        />
      ) : (
        <div className="user-grid">
          {visiblePeople.map((person) => (
            <UserCard key={person.id} user={person} />
          ))}
        </div>
      )}
    </>
  )
}
