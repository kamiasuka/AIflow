import { post } from './client'

export function generateImages(payload) {
  return post('/api/image/generate', payload)
}
