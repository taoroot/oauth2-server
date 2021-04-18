import request from '@/utils/request'

/**
 * 菜单树
 * @param {分页参数} params
 */
export const menuTree = (params) => {
  return request({
    url: '/api/sys/menu/tree',
    method: 'get',
    params
  })
}

/**
 * 新增
 * @param {数据体} data
 */
export const menuAdd = (data) => {
  return request({
    url: '/api/sys/menu',
    method: 'post',
    data: data
  })
}

/**
 * 批量删除
 * @param {id数组} ids
 */
export const menuDel = (ids) => {
  return request({
    url: '/api/sys/menu',
    method: 'delete',
    params: {
      ids
    }
  })
}

/**
 * 强制删除
 * @param {主键} id
 */
export const menuDelForce = (id) => {
  return request({
    url: '/api/sys/menu/force',
    method: 'delete',
    params: {
      id
    }
  })
}

/**
 * 更新
 * @param {数据体} data
 */
export function menuUpdate(data) {
  return request({
    url: '/api/sys/menu',
    method: 'put',
    data: data
  })
}

/**
 * 排序
 * @param {排序参数} params
 */
export function menuSort(params) {
  return request({
    url: '/api/sys/menu/sort',
    method: 'put',
    params
  })
}

