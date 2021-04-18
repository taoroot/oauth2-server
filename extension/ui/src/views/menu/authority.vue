<template>
  <div class="app-container">

    <div class="filter-container">
      <el-form ref="form" :inline="true" :model="form" size="mini">
        <el-form-item>
          <el-button v-show="!form.isAdd" type="primary" icon="el-icon-plus" @click="AuthorityAdd" />
          <el-button v-show="!form.isAdd" type="danger" icon="el-icon-delete" :disabled="table.selection.length === 0" @click="tableDelete(table.selection)" />
          <el-button v-show="!form.isAdd" icon="el-icon-refresh" @click="tablePage" />
          <el-select
            v-show="form.isAdd"
            ref="selectAuthority"
            v-model="form.authority.value"
            placeholder="请选择"
            style="margin:0 10px;"
            clearable
            multiple
            collapse-tags
          >
            <el-option
              v-for="item in form.authority.list"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right;  color: #8492a6; font-size: 13px">{{ item.authority }} [{{ item.method }} {{ item.path }} ]</span>
            </el-option>
          </el-select>
          <el-button v-show="form.isAdd" type="success" icon="el-icon-check" @click="submit" />
          <el-button v-show="form.isAdd" icon="el-icon-close" @click="cancel" />
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table row-key="id" border :data="table.data" size="mini" @selection-change="tableSelectionChange">
        <el-table-column type="selection" align="center" />
        <el-table-column label="名称" align="center" header-align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="请求地址" align="center" header-align="center" prop="path" :show-overflow-tooltip="true" />
        <el-table-column label="请求类型" align="center" header-align="center" prop="method" :show-overflow-tooltip="true" />
        <el-table-column label="权限标识" align="center" header-align="center" prop="authority" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" width="50">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-delete" @click="tableDelete([scope.row.id])" />
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getMenuAuthoritys, getAuthoritys, updateMenuAuthority, deleteMenuAuthority } from '@/api/sys/authority'
export default {
  name: 'Role',
  props: {
    menuId: {
      default: -1,
      type: Number
    }
  },
  data() {
    return {
      form: {
        isAdd: false,
        authority: {
          list: [],
          value: []
        }
      },
      table: {
        data: [],
        selection: []
      }
    }
  },
  watch: {
    menuId(val) {
      console.log(val)
      this.tablePage()
    }
  },
  mounted() {
    this.tablePage()
  },
  methods: {
    AuthorityAdd() {
      this.form.authority.value = []
      this.form.isAdd = true
      getAuthoritys({ size: -1 }).then(response => {
        this.form.authority.list = response.data.records.filter(item => {
          return !this.table.data.some(_item => _item.authority === item.authority)
        })
      })
    },
    tableDelete(ids) {
      console.log(ids)
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenuAuthority(this.menuId, ids).then(response => {
          this.tablePage()
        })
      })
    },
    tablePage() {
      const params = {
        size: -1,
        menuId: this.menuId
      }
      getMenuAuthoritys(params).then(response => {
        this.table.data = response.data.records
        this.table.total = response.data.total
      })
    },
    tableSelectionChange(val) {
      this.table.selection = []
      val.forEach(element => {
        this.table.selection.push(element.id)
      })
    },
    submit() {
      updateMenuAuthority(this.menuId, this.form.authority.value).then(res => {
        this.form.isAdd = false
        this.form.authority.value = []
        this.tablePage()
      })
    },
    cancel() {
      this.form.isAdd = false
      this.tablePage()
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 0;
}
.tools-container {
  margin-top: 5px;
  margin-bottom: 15px;
}
.pagination-container {
  position: relative;
  float: right;
  margin-top: 10px;
}
</style>
