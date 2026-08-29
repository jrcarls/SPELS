import { Button as AriaButton } from 'react-aria-components'
import { twMerge } from 'tailwind-merge'

const variants = {
  primary: 'bg-bg-brand-solid text-text-white shadow-sm hover:bg-bg-brand-solid_hover',
  secondary: 'bg-white text-text-secondary ring-1 ring-inset ring-border-primary hover:bg-gray-50',
  tertiary: 'text-text-secondary hover:bg-gray-100',
}

export function Button({ className, color = 'primary', size = 'md', ...props }) {
  return (
    <AriaButton
      {...props}
      className={twMerge(
        'inline-flex items-center justify-center gap-2 rounded-lg font-semibold transition-colors focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-600 disabled:cursor-not-allowed disabled:opacity-50',
        size === 'sm' ? 'px-3 py-2 text-sm' : 'px-4 py-2.5 text-sm',
        variants[color],
        className,
      )}
    />
  )
}
