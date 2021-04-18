<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" size="mini">
        <el-form-item>
          <el-button type="primary" icon="el-icon-refresh" @click="tableGetPage" />
          <el-button type="primary" icon="el-icon-plus" @click="tableCreate({})" />
          <el-button type="primary" icon="el-icon-delete" @click="tableGetPage" />
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table ref="table" v-loading="table.loading" border :data="table.data" style="width: 100%" size="mini">
        <el-table-column label="名称" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ scope.row.name }} {{ scope.row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="50">
          <template slot-scope="scope">
            <el-button v-permission="['sys:role:update']" type="text" icon="el-icon-edit" @click="tableEdit(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :append-to-body="true" :visible.sync="form.dialog" :title="form.data.id === undefined ? '新增' : '编辑'" width="500px" :before-close="tableCancel">
      <el-form ref="form" :model="form.data" :rules="form.rules" size="small" label-width="100px">

        <el-form-item label="名称" prop="name">
          <el-input v-model="form.data.name" />
        </el-form-item>

        <el-form-item label="描述" prop="desc">
          <el-input v-model="form.data.remark" />
        </el-form-item>

        <el-form-item label="权限" prop="desc">
          <el-button v-if="menu.checkAll" type="text" size="mini" @click="treeButtonCheckAll(menu, $refs.menuTree, ...arguments)"> 取消全选</el-button>
          <el-button v-else type="text" size="mini" @click="treeButtonCheckAll(menu, $refs.menuTree, ...arguments)">选择全部</el-button>
          <el-tree
            ref="menuTree"
            :data="menu.data"
            :default-checked-keys="form.data.menus"
            show-checkbox
            node-key="id"
            :props="{ children: 'children', label: 'title' }"
            element-loading-text="拼命加载中"
            :check-strictly="true"
            @check-change="authroityCheckChange($refs.menuTree, ...arguments)"
          />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="form.dialog = false">取消</el-button>
        <el-button :loading="table.loading" type="primary" @click="tableSubmit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { rolePage, roleDel, roleAdd, roleUpdate } from '@/api/sys/role'
import { menuTree } from '@/api/sys/menu'
const _defaultRow = {
  id: 0,
  remark: ''
}

export default {
  name: 'Role',
  props: {},
  data() {
    return {
      table: {
        data: [],
        current: this.$route.query.page ? Number.parseInt(this.$route.query.page) : 1,
        size: -1,
        total: 0,
        loading: false
      },
      form: {
        data: Object.assign({}, _defaultRow),
        rules: {},
        scope: [],
        dialog: false
      },
      menu: {
        checkAll: false,
        data: []
      },
      dict: {
      }
    }
  },
  mounted() {
    this.tableGetPage()
  },
  methods: {
    tableCreate() {
      this.form.dialog = true
      this.form.data = Object.assign({}, _defaultRow)
    },
    tableDelete(row) {
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
        roleDel({ id: row.id }).then(response => {
          this.tableGetPage()
        })
      })
    },
    tableEdit(row) {
      this.form.dialog = true
      this.form.data = Object.assign({}, row)
      this.menu.checkAll = false
    },
    tableCancel(done) {
      if (this.$refs.menuTree) {
        this.$refs.menuTree.setCheckedKeys([])
      }
      if (this.$refs.scopeTree) {
        this.$refs.scopeTree.setCheckedKeys([])
      }
      done()
    },
    tableGetPage() {
      this.table.loading = true
      const params = {
        current: this.table.current,
        size: this.table.size
      }
      rolePage(params).then(response => {
        this.table.loading = false
        this.table.data = response.data.records
        this.table.total = response.data.total
      })
      menuTree().then(response => {
        this.menu.data = response.data
      })
    },
    tableSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.form.data.menus = [...this.$refs.menuTree.getCheckedKeys(), ...this.$refs.menuTree.getHalfCheckedKeys()]
          if (this.form.data.id === undefined) {
            roleAdd(this.form.data).then((res) => {
              this.form.dialog = false
              this.tableGetPage()
              this.$refs['form'].resetFields()
            })
          } else {
            roleUpdate(this.form.data).then((res) => {
              this.form.dialog = false
              this.tableGetPage()
            })
          }
        }
      })
    },
    treeButtonCheckAll(treeData, refsTree) {
      treeData.checkAll = !treeData.checkAll
      if (treeData.checkAll) {
        const allAuthoritys = []
        this.treeDataCheckAll(treeData.data, allAuthoritys)
        refsTree.setCheckedNodes(allAuthoritys)
      } else {
        refsTree.setCheckedNodes([])
      }
    },
    treeDataCheckAll(menuData, allAuthoritys) {
      menuData.forEach(menu => {
        allAuthoritys.push(menu)
        if (menu.children) {
          this.treeDataCheckAll(menu.children, allAuthoritys)
        }
      })
    },
    authroityCheckChange(refsTree, data, check) {
      if (check) { // 节点选中时同步选中父节点
        refsTree.setChecked(data.parentId, true, false)
      } else {
        if (data.children != null) { // 节点取消选中时同步取消选中子节点
          data.children.forEach(element => {
            refsTree.setChecked(element.id, false, false)
          })
        }
      }
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
