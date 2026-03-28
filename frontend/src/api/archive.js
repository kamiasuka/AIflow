import { get } from './client'

export function fetchArchiveProjectList() {
  return get('/api/archive/project/list')
}

export function fetchArchiveProjectDetail(projectId) {
  return get(`/api/archive/project/${projectId}`)
}
