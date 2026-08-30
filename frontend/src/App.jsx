import { LoginPage } from '@/features/auth/LoginPage'
import { SignUpPage } from '@/features/auth/SignUpPage'

export default function App() {
  return window.location.pathname === '/signup' ? <SignUpPage /> : <LoginPage />
}
