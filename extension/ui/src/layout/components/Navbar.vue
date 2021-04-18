<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div v-if="name" class="avatar-wrapper">
          <img :src="avatar || '/logo.jpg'" class="user-avatar">
          <span class="user-title">{{ name }}<i class="el-icon-caret-bottom" style="margin-right: 6px;" /></span>
        </div>
        <el-dropdown-menu v-if="name" slot="dropdown" class="user-dropdown">
          <el-dropdown-item>
            <span style="display:block;" @click="logout">登出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'name'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        position: relative;
        height: 100%;
        padding-left: 40px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        font-size: 0;
        cursor: pointer;

        .user-avatar {
          position: absolute;
          top: 45%;
          left: 0;
          -webkit-transform: translateY(-50%);
          transform: translateY(-50%);
          display: inline-block;
          width: 30px;
          height: 30px;
          border-radius: 50%;
        }

        .user-title {
          font-size: 16px;
          border-right: 1px solid #e5e5e5;
          padding-right: 20px;
        }

        .el-icon-caret-bottom {
          position: absolute;
          right: 0;
          top: 50%;
          -webkit-transform: translateY(-50%);
          transform: translateY(-50%);
          font-size: 12px;
        }
      }
    }
  }
}
</style>
