import request from '@/utils/request'

export const getAuthorities = (params) => {
  return request({
    url: '/upms/authorities',
    method: 'get',
    params
  })
}

// 保存
export const saveAuthority = (data) => {
  return request({
    url: '/upms/authority',
    method: 'post',
    data: data
  })
}
// 删除
export const delItem = (id) => {
  return request({
    url: '/upms/authority/' + id,
    method: 'delete'
  })
}
// 查询
export const getAuthorityById = (id) => {
  return request({
    url: `/upms/authority/${id}`,
    method: 'get'
  })
}

// 获取路由
export const getRouters = () => {
  return request({
    url: '/upms/authority',
    method: 'get'
  })
}

// 更新菜单
export function updateItem(data) {
  return request({
    url: '/upms/authority',
    method: 'put',
    data: data
  })
}

