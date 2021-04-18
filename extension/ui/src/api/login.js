import request from '@/utils/request'

var Authorization = 'BASIC ' + btoa('test:secret')
var VUE_APP_OAUTH2_API = process.env.VUE_APP_OAUTH2_API

/**
 * OAuth2 密码登录
 * @param {*} data
 * @param {*} params
 * @returns
 */
export function oauth2Login(data, params) {
  return request({
    url: VUE_APP_OAUTH2_API + 'oauth/token',
    method: 'post',
    headers: {
      Authorization: Authorization
    },
    data: data,
    params: params
  })
}

export function oauth2CodeLogin(params) {
  return request({
    url: '/token',
    method: 'post',
    params: params
  })
}

export function getInfo() {
  return request({
    url: '/user_info',
    method: 'get'
  })
}

export function loginPhone(params) {
  return request({
    url: '/sms',
    method: 'post',
    params
  })
}

export function getUserSocial() {
  return request({
    url: '/upms/user_socials',
    method: 'get'
  })
}

export function getUserProfile() {
  return request({
    url: '/upms/user_info',
    method: 'get'
  })
}

export function unbindUserSocial(id) {
  return request({
    url: `/upms/user_social/${id}`,
    method: 'delete'
  })
}

export function updateUser(data) {
  return request({
    url: `/upms/user_info`,
    method: 'put',
    data
  })
}

export function resetPassword(data) {
  return request({
    url: `/upms/user_password`,
    method: 'put',
    data
  })
}

export function getSms({ phone }) {
  return request({
    url: `https://auth.flizi.cn/sms?phone=${phone}`,
    method: 'post'
  })
}
