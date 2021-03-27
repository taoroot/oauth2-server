import request from '@/utils/request'

export function getDepts(params) {
  return request({
    url: '/upms/depts',
    method: 'get',
    params
  })
}

export function delDepts(ids) {
  return request({
    url: `/upms/dept`,
    method: 'delete',
    params: {
      ids: ids
    }
  })
}

export function createDept(data) {
  return request({
    url: '/upms/dept',
    method: 'post',
    data
  })
}

export function updateDept(data) {
  return request({
    url: `/upms/dept`,
    method: 'put',
    data
  })
}
