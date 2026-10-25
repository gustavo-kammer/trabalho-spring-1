import { Route, Routes } from 'react-router-dom'
import { PrivateRoute, PublicOnlyRoute } from './auth/RouteGuards'
import Layout from './components/Layout'
import NotFoundPage from './pages/NotFoundPage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'
import FavoritesPage from './pages/posts/FavoritesPage'
import FeedPage from './pages/posts/FeedPage'
import PostDetailPage from './pages/posts/PostDetailPage'
import PostFormPage from './pages/posts/PostFormPage'
import EditProfilePage from './pages/users/EditProfilePage'
import FollowListPage from './pages/users/FollowListPage'
import MyProfilePage from './pages/users/MyProfilePage'
import PeoplePage from './pages/users/PeoplePage'
import UserProfilePage from './pages/users/UserProfilePage'

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<PublicOnlyRoute><LoginPage /></PublicOnlyRoute>} />
      <Route path="/cadastro" element={<PublicOnlyRoute><RegisterPage /></PublicOnlyRoute>} />

      {/* Páginas de quem está logado, todas com o menu no topo */}
      <Route element={<PrivateRoute><Layout /></PrivateRoute>}>
        <Route index element={<FeedPage />} />
        {/* key diferente: ao ir de "editar" para "nova", o formulário começa vazio */}
        <Route path="receitas/nova" element={<PostFormPage key="nova" />} />
        <Route path="receitas/:id" element={<PostDetailPage />} />
        <Route path="receitas/:id/editar" element={<PostFormPage key="editar" />} />
        <Route path="favoritos" element={<FavoritesPage />} />

        <Route path="perfil" element={<MyProfilePage />} />
        <Route path="perfil/editar" element={<EditProfilePage />} />
        <Route path="pessoas" element={<PeoplePage />} />
        <Route path="usuarios/:id" element={<UserProfilePage />} />
        <Route path="usuarios/:id/seguidores" element={<FollowListPage type="followers" />} />
        <Route path="usuarios/:id/seguindo" element={<FollowListPage type="following" />} />

        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  )
}
