import request from '@/utils/request'

/**
 * 菜单具有的权限
 * @param {*} params
 */
export const getMenuAuthoritys = (params) => {
  return request({
    url: '/sys/menu/authority/page',
    method: 'get',
    params
  })
}

/**
 * 菜单具有的权限新增
 * @param {*} menuId
 * @param {*} data
 */
export const updateMenuAuthority = (menuId, data) => {
  return request({
    url: `/sys/menu/authority?menuId=${menuId}`,
    method: 'post',
    data
  })
}

/**
 * 菜单具有的权限移除
 * @param {*} menuId
 * @param {*} data
 */
export const deleteMenuAuthority = (menuId, data) => {
  return request({
    url: `/sys/menu/authority?menuId=${menuId}`,
    method: 'delete',
    data
  })
}

/**
 * 所有菜单
 * @param {*} params
 */
export const getAuthoritys = (params) => {
  return request({
    url: '/sys/authority/page',
    method: 'get',
    params
  })
}
