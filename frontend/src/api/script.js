import { post } from './client'

export function generateScript(payload) {
  return post('/api/script/generate', payload)
}
