import { post } from './client'

export function generateImages(payload) {
  return post('/api/image/generate', payload)
}

export function generateImageWorkflow(payload) {
  return post('/api/image/workflow/generate', payload)
}
