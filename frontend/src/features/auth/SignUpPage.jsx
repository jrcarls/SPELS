import { useState } from 'react'
import { zodResolver } from '@hookform/resolvers/zod'
import { ArrowLeft, Eye, EyeOff, Gift01 } from '@untitledui/icons'
import { useForm } from 'react-hook-form'
import { Button } from '@/components/base/buttons/button'
import { register } from './authApi'
import { formatCnpj, signUpSchema } from './authSchemas'

export function SignUpPage() {
  const [showPassword, setShowPassword] = useState(false)
  const [submitError, setSubmitError] = useState('')
  const {
    register: registerField,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm({
    resolver: zodResolver(signUpSchema),
    mode: 'onChange',
    reValidateMode: 'onChange',
    defaultValues: {
      name: '', organizationName: '', cnpj: '', email: '', password: '', confirmPassword: '',
    },
  })

  async function onSubmit(form) {
    setSubmitError('')
    try {
      await register({
        name: form.name,
        organizationName: form.organizationName,
        cnpj: form.cnpj || null,
        email: form.email,
        password: form.password,
      })
      window.location.assign('/dashboard')
    } catch (err) {
      setSubmitError(err.message)
    }
  }

  return (
    <main className="min-h-svh bg-bg-secondary p-4 sm:p-8">
      <div className="mx-auto grid min-h-[calc(100svh-2rem)] max-w-7xl overflow-hidden rounded-2xl bg-bg-primary shadow-xl shadow-gray-900/8 sm:min-h-[calc(100svh-4rem)] md:grid-cols-[1.08fr_0.92fr]">
        <aside className="relative hidden overflow-hidden bg-gray-900 p-10 text-white md:order-2 md:flex md:flex-col md:justify-between lg:p-12">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_25%_18%,rgba(158,119,237,0.52),transparent_28%),radial-gradient(circle_at_90%_85%,rgba(255,255,255,0.12),transparent_30%)]" />
          <div className="relative max-w-md">
            <p className="text-3xl font-semibold leading-tight tracking-tight">Comece a organizar sua confeitaria hoje.</p>
            <p className="mt-5 text-base leading-7 text-gray-300">Crie sua conta e tenha pedidos, produção, estoque e financeiro conectados desde o primeiro dia.</p>
          </div>
          <div className="relative border-t border-white/15 pt-6">
            <p className="text-sm font-medium">SPELS</p>
            <p className="mt-1 text-sm text-gray-400">ERP para confeitarias que querem crescer.</p>
          </div>
        </aside>

        <section className="flex items-center justify-center px-6 py-12 sm:px-12 md:order-1 md:px-14 lg:px-20">
          <div className="w-full max-w-md">
            <a href="/" className="mb-8 inline-flex items-center gap-2 text-sm font-semibold text-text-secondary hover:text-text-primary">
              <ArrowLeft className="size-4" aria-hidden="true" /> Voltar para entrar
            </a>

            <div className="mb-8 flex items-center gap-3">
              <div className="grid size-10 place-items-center rounded-xl bg-brand-600 text-white shadow-lg shadow-brand-600/25">
                <Gift01 className="size-5" aria-hidden="true" />
              </div>
              <span className="text-lg font-semibold tracking-tight text-text-primary">SPELS</span>
            </div>

            <header className="mb-8">
              <h1 className="text-3xl font-semibold tracking-tight text-text-primary">Crie sua confeitaria</h1>
              <p className="mt-2 text-sm text-text-secondary">Seu período de teste começa assim que a conta for criada.</p>
            </header>

            <form className="space-y-5" onSubmit={handleSubmit(onSubmit)} noValidate>
              <div className="grid gap-5 sm:grid-cols-2">
                <Field label="Seu nome" placeholder="Seu nome" autoComplete="name" error={errors.name?.message} inputProps={registerField('name')} />
                <Field label="Nome da confeitaria" placeholder="Ex.: Doce & Cia" error={errors.organizationName?.message} inputProps={registerField('organizationName')} />
              </div>

              <Field
                label="CNPJ (opcional)"
                placeholder="00.000.000/E08G-12"
                inputMode="text"
                error={errors.cnpj?.message}
                inputProps={registerField('cnpj')}
                onChange={(event) => setValue('cnpj', formatCnpj(event.target.value), { shouldDirty: true, shouldValidate: true })}
              />
              <Field label="E-mail" type="email" placeholder="voce@confeitaria.com" autoComplete="email" error={errors.email?.message} inputProps={registerField('email')} />

              <label className="block">
                <span className="mb-1.5 block text-sm font-medium text-text-secondary">Senha</span>
                <span className="relative block">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    autoComplete="new-password"
                    placeholder="Pelo menos 8 caracteres"
                    aria-invalid={Boolean(errors.password)}
                    {...registerField('password')}
                    className={inputClassName(true, errors.password)}
                  />
                  <button type="button" onClick={() => setShowPassword((visible) => !visible)} className="absolute inset-y-0 right-0 grid w-11 place-items-center text-text-tertiary hover:text-text-secondary" aria-label={showPassword ? 'Ocultar senha' : 'Mostrar senha'}>
                    {showPassword ? <EyeOff className="size-5" /> : <Eye className="size-5" />}
                  </button>
                </span>
                <FieldError message={errors.password?.message} />
              </label>

              <Field label="Confirmar senha" type={showPassword ? 'text' : 'password'} placeholder="Repita sua senha" autoComplete="new-password" error={errors.confirmPassword?.message} inputProps={registerField('confirmPassword')} />

              {submitError && <p role="alert" className="rounded-lg bg-red-50 px-3.5 py-3 text-sm text-red-700">{submitError}</p>}

              <Button type="submit" className="w-full" isDisabled={isSubmitting}>
                {isSubmitting ? 'Criando conta...' : 'Criar conta'}
              </Button>
            </form>

            <p className="mt-6 text-center text-xs leading-5 text-text-tertiary">Ao criar a conta, você concorda com os Termos de uso e a Política de privacidade.</p>
          </div>
        </section>
      </div>
    </main>
  )
}

function Field({ label, type = 'text', placeholder, autoComplete, inputMode, error, inputProps, onChange }) {
  return (
    <label className="block">
      <span className="mb-1.5 block text-sm font-medium text-text-secondary">{label}</span>
      <input
        type={type}
        placeholder={placeholder}
        autoComplete={autoComplete}
        inputMode={inputMode}
        aria-invalid={Boolean(error)}
        {...inputProps}
        onChange={onChange}
        className={inputClassName(false, error)}
      />
      <FieldError message={error} />
    </label>
  )
}

function FieldError({ message }) {
  return message ? <span role="alert" className="mt-1.5 block text-sm text-red-600">{message}</span> : null
}

function inputClassName(hasIcon = false, error) {
  const borderColor = error ? 'border-red-400 focus:border-red-500 focus:ring-red-100' : 'border-border-primary focus:border-brand-500 focus:ring-brand-100'
  return `h-11 w-full rounded-lg border bg-bg-primary py-2 text-sm text-text-primary outline-none transition placeholder:text-gray-400 focus:ring-4 ${borderColor} ${hasIcon ? 'pl-3.5 pr-11' : 'px-3.5'}`
}
