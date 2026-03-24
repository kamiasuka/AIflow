import { del, get, post, put } from './client'

export function fetchAllModelConfigs() {
  return get('/api/model-config/list')
}

export function fetchEnabledModelConfigs() {
  return get('/api/model-config/enabled')
}

export function createModelConfig(payload) {
  return post('/api/model-config', payload)
}

export function updateModelConfig(id, payload) {
  return put(`/api/model-config/${id}`, payload)
}

export function deleteModelConfig(id) {
  return del(`/api/model-config/${id}`)
}

export function setDefaultModelConfig(id) {
  return put(`/api/model-config/${id}/default`)
}
