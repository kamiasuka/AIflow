import { post } from './client'

export function generateVideo(payload) {
  return post('/api/video/generate', payload)
}
