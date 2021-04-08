import request from '@/utils/request'

export function getRoles(params) {
  return request({
    url: '/upms/roles',
    method: 'get',
    params
  })
}

export function delItem(ids) {
  return request({
    url: `/upms/role`,
    method: 'delete',
    params: {
      ids: ids
    }
  })
}

export function saveItem(data) {
  return request({
    url: '/upms/role',
    method: 'post',
    data
  })
}

export function updateItem(data) {
  return request({
    url: `/upms/role`,
    method: 'put',
    data
  })
}

export function updatePermission(roleId, menuIds) {
  return request({
    url: `/upms/role/${roleId}/authorities`,
    method: 'put',
    params: {
      authorityIds: menuIds
    }
  })
}

export function getPermission(roleId) {
  return request({
    url: `/upms/role/${roleId}/authorities`,
    method: 'get'
  })
}
