import { z } from 'zod'

const emailSchema = z
  .string()
  .trim()
  .min(1, 'Informe seu e-mail.')
  .email('Informe um e-mail válido.')

const passwordSchema = z
  .string()
  .min(8, 'A senha deve ter pelo menos 8 caracteres.')
  .max(72, 'A senha deve ter no máximo 72 caracteres.')

function isValidCnpj(value) {
  if (!value) return true

  const normalized = value.trim().toUpperCase()
  if (!/^[A-Z0-9./-]+$/.test(normalized)) return false

  const canonical = normalized.replace(/[./-]/g, '')
  return /^[A-Z0-9]{12}\d{2}$/.test(canonical)
}

export const loginSchema = z.object({
  email: emailSchema,
  password: z.string().min(1, 'Informe sua senha.'),
})

export const signUpSchema = z
  .object({
    name: z.string().trim().min(2, 'Informe seu nome.').max(100, 'O nome deve ter no máximo 100 caracteres.'),
    organizationName: z.string().trim().min(2, 'Informe o nome da confeitaria.').max(120, 'O nome deve ter no máximo 120 caracteres.'),
    cnpj: z.string().trim().refine(isValidCnpj, 'Informe um CNPJ válido.'),
    email: emailSchema,
    password: passwordSchema,
    confirmPassword: z.string().min(1, 'Confirme sua senha.'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'As senhas não conferem.',
    path: ['confirmPassword'],
  })

export function formatCnpj(value) {
  const characters = value.toUpperCase().replace(/[^A-Z0-9]/g, '').slice(0, 14)
  const parts = [
    characters.slice(0, 2),
    characters.slice(2, 5),
    characters.slice(5, 8),
    characters.slice(8, 12),
    characters.slice(12, 14),
  ]

  let formatted = parts[0]
  if (characters.length > 2) formatted += `.${parts[1]}`
  if (characters.length > 5) formatted += `.${parts[2]}`
  if (characters.length > 8) formatted += `/${parts[3]}`
  if (characters.length > 12) formatted += `-${parts[4]}`

  return formatted
}
