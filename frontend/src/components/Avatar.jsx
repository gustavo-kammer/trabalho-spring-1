import { initials } from '../utils/format'

export default function Avatar({ user, size = 'md' }) {
  return (
    <span className={`avatar avatar--${size}`} aria-hidden="true">
      {initials(user) || '?'}
    </span>
  )
}
