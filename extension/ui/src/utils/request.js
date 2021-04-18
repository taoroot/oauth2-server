import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000, // request timeout,
  paramsSerializer: function(params) {
    return require('qs').stringify(params, { arrayFormat: 'repeat' })
  },
  validateStatus: function(status) {
    return status >= 200 && status < 600 // default
  }
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.token && !config.headers['Authorization']) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    if (response.status !== 200) {
      // Bad Request
      var errmsg = response.data.error_description || '未知错误, 请联系管理员'
      if (response.status === 400) {
        Message({
          message: errmsg,
          type: 'error',
          duration: 2 * 1000
        })
      }
      return Promise.reject(new Error(errmsg))
    }
    // OAuth2.0 不是通用数据格式
    if (response.data.code === undefined) {
      return response.data
    }

    // 通用数据
    const res = response.data

    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 'SUCCESS') {
      Message({
        message: res.msg || '未知错误',
        type: 'error',
        duration: 2 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err ', error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
