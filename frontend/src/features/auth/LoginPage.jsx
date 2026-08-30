import { useState } from 'react'
import { Eye, EyeOff, Gift01 } from '@untitledui/icons'
import { Button } from '@/components/base/buttons/button'
import { login } from './authApi'

export function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setIsLoading(true)

    try {
      await login({ email, password })
      window.location.assign('/dashboard')
    } catch (err) {
      setError(err.message)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <main className="min-h-svh bg-bg-secondary p-4 sm:p-8">
      <div className="mx-auto grid min-h-[calc(100svh-2rem)] max-w-7xl overflow-hidden rounded-2xl bg-bg-primary shadow-xl shadow-gray-900/8 sm:min-h-[calc(100svh-4rem)] lg:grid-cols-[1.08fr_0.92fr]">
        <section className="flex items-center justify-center px-6 py-12 sm:px-12 lg:px-20">
          <div className="w-full max-w-sm">
            <div className="mb-10 flex items-center gap-3">
              <div className="grid size-10 place-items-center rounded-xl bg-brand-600 text-white shadow-lg shadow-brand-600/25">
                <Gift01 className="size-5" aria-hidden="true" />
              </div>
              <span className="text-lg font-semibold tracking-tight text-text-primary">SPELS</span>
            </div>

            <header className="mb-8">
              <h1 className="text-3xl font-semibold tracking-tight text-text-primary">Que bom ter você de volta</h1>
              <p className="mt-2 text-sm text-text-secondary">Entre para gerenciar sua confeitaria.</p>
            </header>

            <form className="space-y-5" onSubmit={handleSubmit} noValidate>
              <label className="block">
                <span className="mb-1.5 block text-sm font-medium text-text-secondary">E-mail</span>
                <input
                  type="email"
                  autoComplete="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  placeholder="voce@confeitaria.com"
                  required
                  className="h-11 w-full rounded-lg border border-border-primary bg-bg-primary px-3.5 text-sm text-text-primary outline-none transition placeholder:text-gray-400 focus:border-brand-500 focus:ring-4 focus:ring-brand-100"
                />
              </label>

              <label className="block">
                <span className="mb-1.5 block text-sm font-medium text-text-secondary">Senha</span>
                <span className="relative block">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    autoComplete="current-password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    placeholder="Digite sua senha"
                    required
                    className="h-11 w-full rounded-lg border border-border-primary bg-bg-primary py-2 pl-3.5 pr-11 text-sm text-text-primary outline-none transition placeholder:text-gray-400 focus:border-brand-500 focus:ring-4 focus:ring-brand-100"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword((visible) => !visible)}
                    className="absolute inset-y-0 right-0 grid w-11 place-items-center text-text-tertiary hover:text-text-secondary"
                    aria-label={showPassword ? 'Ocultar senha' : 'Mostrar senha'}
                  >
                    {showPassword ? <EyeOff className="size-5" /> : <Eye className="size-5" />}
                  </button>
                </span>
              </label>

              <div className="flex items-center justify-between text-sm">
                <label className="flex cursor-pointer items-center gap-2 text-text-secondary">
                  <input type="checkbox" className="size-4 rounded border-border-primary text-brand-600 focus:ring-brand-500" />
                  Lembrar de mim
                </label>
                <button type="button" className="font-semibold text-brand-700 hover:text-brand-800">Esqueci minha senha</button>
              </div>

              {error && <p role="alert" className="rounded-lg bg-red-50 px-3.5 py-3 text-sm text-red-700">{error}</p>}

              <Button type="submit" className="w-full" isDisabled={isLoading}>
                {isLoading ? 'Entrando...' : 'Entrar'}
              </Button>
            </form>

            <p className="mt-8 text-center text-sm text-text-secondary">
              Ainda não tem uma conta?{' '}
              <button type="button" className="font-semibold text-brand-700 hover:text-brand-800">Crie sua confeitaria</button>
            </p>
          </div>
        </section>

        <aside className="relative hidden overflow-hidden bg-gray-900 p-12 text-white lg:flex lg:flex-col lg:justify-between">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_75%_22%,rgba(158,119,237,0.52),transparent_28%),radial-gradient(circle_at_10%_90%,rgba(255,255,255,0.12),transparent_30%)]" />
          <div className="relative max-w-md">
            <p className="text-3xl font-semibold leading-tight tracking-tight">Sua operação, mais doce e organizada.</p>
            <p className="mt-5 text-base leading-7 text-gray-300">Controle pedidos, produção, estoque e financeiro em um só lugar.</p>
          </div>
          <div className="relative border-t border-white/15 pt-6">
            <p className="text-sm font-medium">SPELS</p>
            <p className="mt-1 text-sm text-gray-400">ERP para confeitarias que querem crescer.</p>
          </div>
        </aside>
      </div>
    </main>
  )
}
