import { useCallback, useEffect, useRef, useState } from 'react'

// Padrão das telas que carregam dados.
// - deps: quando mudam, busca de novo do zero (ex.: outro id)
// - reload(): busca de novo mantendo os dados atuais na tela até chegar a resposta
// - setData(): atualiza os dados sem buscar (ex.: depois de curtir)
export function useFetch(loader, deps) {
  const [state, setState] = useState({ data: null, loading: true, error: null })
  const [version, setVersion] = useState(0)
  const lastVersion = useRef(version)

  useEffect(() => {
    const isReload = lastVersion.current !== version
    lastVersion.current = version
    let cancelled = false

    setState((current) => ({ data: isReload ? current.data : null, loading: true, error: null }))
    loader()
      .then((data) => {
        if (!cancelled) setState({ data, loading: false, error: null })
      })
      .catch((error) => {
        if (!cancelled) setState({ data: null, loading: false, error })
      })

    return () => {
      cancelled = true
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [...deps, version])

  const reload = useCallback(() => setVersion((current) => current + 1), [])

  const setData = useCallback((updater) => {
    setState((current) => ({
      ...current,
      data: typeof updater === 'function' ? updater(current.data) : updater,
    }))
  }, [])

  return { ...state, reload, setData }
}
