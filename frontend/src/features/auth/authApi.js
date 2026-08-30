const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export async function login(credentials) {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials),
  })

  if (!response.ok) {
    const body = await response.json().catch(() => null)
    throw new Error(body?.message ?? 'Não foi possível entrar. Tente novamente.')
  }

  return response.json()
}
