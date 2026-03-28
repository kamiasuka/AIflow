import { get, post } from './client'

export function fetchInspirationList() {
  return get('/api/inspiration/list')
}

export function createInspiration(payload) {
  return post('/api/inspiration', payload)
}

export function importDemoInspirations() {
  return post('/api/inspiration/demo-import', {})
}

export function createProjectFromInspiration(id) {
  return post(`/api/inspiration/${id}/create-project`, {})
}
