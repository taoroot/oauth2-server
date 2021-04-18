<template>
  <div class="app-container">
    <el-card>
      <el-row :gutter="10">
        <el-col :span="4">
          <el-divider>角色列表</el-divider>
          <my-role />
        </el-col>
        <el-col :span="10">
          <el-divider>菜单列表</el-divider>
          <my-menu @menuId="menuChange" />
        </el-col>
        <el-col :span="10">
          <el-divider>权限列表</el-divider>
          <my-authority :menu-id="menuId" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import MyAuthority from './authority'
import MyRole from './role'
import MyMenu from './menu'

export default {
  name: 'Menu',
  components: { MyAuthority, MyRole, MyMenu },
  props: {},
  data() {
    return {
      menuId: -1
    }
  },
  methods: {
    menuChange(val) {
      this.menuId = val
    }
  }
}
</script>
