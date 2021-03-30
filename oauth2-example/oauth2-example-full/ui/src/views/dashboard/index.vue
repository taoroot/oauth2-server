<template>
  <div class="dashboard-container">
    <div class="dashboard-text">账号: {{ name }}</div>
    <el-button type="primary" @click.native.prevent="bindButton(840)"> 账号中心 </el-button>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import openWindow from '@/utils/open-window'

export default {
  name: 'Dashboard',
  data() {
    return {
      socials: [],
      msg: ''
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  mounted() {

  },
  methods: {
    bindButton(wdith) {
      const url = process.env.VUE_APP_OAUTH2_API;
      openWindow(url, '统一认证中心', wdith, 540)
      this.loading = false
    },
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
