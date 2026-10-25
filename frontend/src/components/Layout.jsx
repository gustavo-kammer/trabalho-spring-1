import { useEffect } from 'react'
import { Outlet, useLocation } from 'react-router-dom'
import Navbar from './Navbar'
import './Layout.css'

// Moldura das páginas de quem está logado: menu no topo e a página embaixo.
export default function Layout() {
  const { pathname } = useLocation()

  // cada página nova começa do topo
  useEffect(() => {
    window.scrollTo(0, 0)
  }, [pathname])

  return (
    <>
      <Navbar />
      <main className="container page">
        <Outlet />
      </main>
    </>
  )
}
