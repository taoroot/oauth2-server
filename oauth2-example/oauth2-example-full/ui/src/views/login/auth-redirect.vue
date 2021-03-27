<template>
  <div>
    <h2 style="text-align: center">{{ errMsg }}, {{ time }} 秒后自动关闭</h2>
  </div>
</template>
<script>
import { param2Obj } from '@/utils'
export default {
  name: 'AuthRedirect',
  data() {
    return {
      errMsg: '',
      time: 5
    }
  },
  created() {
    var timer = setInterval(() => {
      this.time--
      if (this.time <= 0) {
        clearInterval(timer)
        window.close()
      }
    }, 1000)
    window.localStorage.setItem('x-admin-oauth-bind', token)
    window.localStorage.clear('x-admin-oauth-bind')
    this.errMsg = param2Obj(window.location.hash)['msg']
    var token = param2Obj(window.location.hash)['access_token']
    if (token) {
      if (window.localStorage) {
        window.localStorage.setItem('x-admin-oauth-code', token)
        window.localStorage.clear('x-admin-oauth-code')
        window.close()
      }
    }
  },
  render: function(h) {
    return h() // avoid warning message
  }
}
</script>
