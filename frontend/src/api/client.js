const DEFAULT_HEADERS = {
  'Content-Type': 'application/json'
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: {
      ...DEFAULT_HEADERS,
      ...(options.headers || {})
    }
  })

  const payload = await response.json()
  if (!response.ok || payload.code !== 2001) {
    throw new Error(payload.msg || 'Request failed')
  }

  return payload.data
}

export function get(path) {
  return request(path, { method: 'GET' })
}

export function post(path, body) {
  return request(path, {
    method: 'POST',
    body: JSON.stringify(body)
  })
}

export function put(path, body) {
  return request(path, {
    method: 'PUT',
    body: body == null ? null : JSON.stringify(body)
  })
}

export function del(path) {
  return request(path, { method: 'DELETE' })
}
