import request from '@/utils/request'

/**
 * 分页
 * @param {分页参数} params
 */
export function userPage(params) {
  return request({
    url: '/sys/user/page',
    method: 'get',
    params
  })
}

/**
 * 批量删除
 * @param {id数组} ids
 */
export function userDel(ids) {
  return request({
    url: `/sys/user`,
    method: 'delete',
    params: {
      ids: ids
    }
  })
}

/**
 * 新增
 * @param {数据体} data
 */
export function userAdd(data) {
  return request({
    url: '/sys/user',
    method: 'post',
    data
  })
}

/**
 * 更新
 * @param {数据体} data
 */
export function userUpdate(data) {
  return request({
    url: `/sys/user`,
    method: 'put',
    data
  })
}

/**
 * 更新用户状态
 * @param {用户ID} id
 * @param {状态} enabled
 */
export function userStatusChange(id, enabled) {
  const data = {
    id,
    enabled
  }
  return request({
    url: `/sys/user`,
    method: 'put',
    data: data
  })
}
