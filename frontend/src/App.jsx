import { Navigate, Route, Routes } from 'react-router-dom'
import { PrivateRoute, PublicOnlyRoute } from './auth/RouteGuards'
import HomePage from './pages/HomePage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<PublicOnlyRoute><LoginPage /></PublicOnlyRoute>} />
      <Route path="/cadastro" element={<PublicOnlyRoute><RegisterPage /></PublicOnlyRoute>} />
      <Route path="/" element={<PrivateRoute><HomePage /></PrivateRoute>} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
