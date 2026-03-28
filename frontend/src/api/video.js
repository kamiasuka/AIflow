import { post } from './client'

export function generateVideo(payload) {
  return post('/api/video/generate', payload)
}

export function generateVideoWorkflow(payload) {
  return post('/api/video/workflow/generate', payload)
}
