const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export async function login(credentials) {
  return request('/auth/login', credentials)
}

export async function register(account) {
  return request('/auth/register', account)
}

async function request(path, payload) {
  const response = await fetch(`${API_URL}${path}`, {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  if (!response.ok) {
    const body = await response.json().catch(() => null)
    throw new Error(body?.message ?? 'Não foi possível entrar. Tente novamente.')
  }

  return response.json()
}
