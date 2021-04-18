<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="search" size="mini">
        <el-form-item>
          <el-input v-model="search.title" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-select v-model="search.hidden" placeholder="菜单状态" clearable>
            <el-option label="可见" :value="false" />
            <el-option label="隐藏" :value="true" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="tablePage" />
          <el-button type="primary" icon="el-icon-plus" @click="tableCreate({})" />
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table size="mini" row-key="id" :row-class-name="tableRowClassName" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" border :data="table.data" @row-dblclick="tableData">

        <el-table-column label="名称" align="left" header-align="center" width="200">
          <template slot-scope="scope">
            <svg-icon v-if="scope.row.icon" :icon-class="scope.row.icon" style="width: 16px;margin-right:5px" />
            <span>{{ scope.row.title }}</span>
            <el-button style="margin-left: 10px" type="text" size="mini" icon="el-icon-top" :disabled="!scope.row.sortPrev" @click="tableSort(scope.row, -1)" />
            <el-button type="text" size="mini" icon="el-icon-bottom" :disabled="!scope.row.sortNext" @click="tableSort(scope.row, 1)" />
          </template>
        </el-table-column>

        <el-table-column label="类型" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 0">菜单</el-tag>
            <el-tag v-else-if="scope.row.type === 1" type="warning">按钮</el-tag>
            <el-tag v-else>--</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="权限标识" align="left" header-align="center" prop="authority" :show-overflow-tooltip="true" />

        <el-table-column label="路径" align="left" header-align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span v-if="scope.row.type === 0">{{ scope.row.absPath }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" @click="tableEdit(scope.row)" />
            <el-button type="text" icon="el-icon-plus" @click="tableCreate(scope.row)" />
            <el-button type="text" icon="el-icon-delete" @click="tableDelete(scope.row)" />
            <el-button type="text" icon="el-icon-s-unfold" @click="tableData(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :append-to-body="true" :visible.sync="form.dialog" :title="form.data.id === undefined ? '新增' : '编辑'" width="600px">
      <el-form ref="form" :model="form.data" :rules="form.rules" label-width="80px">

        <el-form-item label="强制删除" prop="authority">
          <el-button type="danger" icon="el-icon-delete" @click="tableDeleteForce(form.data.id)">删除与之相关的权限数据,子集,角色绑定,权限绑定</el-button>
        </el-form-item>

        <el-form-item label="权限标识" prop="authority">
          <el-input v-model="form.data.authority" />
        </el-form-item>

        <el-form-item label="上级菜单" prop="parentId">
          <treeselect v-model="form.data.parentId" :options="form.authorityTree" :normalizer="node => {if (node.children && !node.children.length) delete node.children; return { id: node.id, label: node.title, children: node.children }}" :show-count="true" placeholder="选择上级菜单" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单标题">
              <el-input v-model="form.data.title" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="role">
              <el-radio-group v-model="form.data.type">
                <el-radio :label="0">菜单</el-radio>
                <el-radio :label="1">功能</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="菜单图标" prop="role">
          <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
            <IconSelect ref="iconSelect" @selected="name => {form.data.icon = name}" />
            <el-input slot="reference" v-model="form.data.icon" placeholder="点击选择图标" readonly>
              <svg-icon v-if="form.data.icon" slot="prefix" :icon-class="form.data.icon" class="el-input__icon" style="height: 40px;width: 16px;" />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.data.type === 0" label="路由名称">
              <el-input v-model="form.data.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.data.type === 0" label="组件路径" prop="component">
              <el-input v-model="form.data.component" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="是否隐藏">
              <el-radio-group v-model="form.data.hidden">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.data.type === 0" label="路由地址" prop="path">
              <el-input v-model="form.data.path" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item v-if="form.data.type === 0" label="跳转地址" prop="redirect">
          <el-input v-model="form.data.redirect" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="form.dialog = false">取消</el-button>
        <el-button :loading="table.loading" type="primary" @click="formSubmit">确认</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import { menuTree, menuUpdate, menuDel, menuAdd, menuSort, menuDelForce } from '@/api/sys/menu'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from '@/components/IconSelect'
const _defaultRow = {
  'id': undefined,
  'parentId': -1,
  'weight': 1,
  'name': undefined,
  'path': undefined,
  'type': 0,
  'component': '',
  'hidden': false,
  'alwaysShow': false,
  'redirect': null,
  'title': undefined,
  'icon': undefined,
  'authority': null,
  'breadcrumb': false
}

export default {
  name: 'Role',
  components: { Treeselect, IconSelect },
  props: {},
  data() {
    return {
      search: {
        title: undefined,
        hidden: undefined
      },
      table: {
        data: [],
        current: 1,
        size: 10,
        total: 0,
        loading: false,
        selection: []
      },
      form: {
        data: Object.assign({}, _defaultRow),
        rules: {
          parentId: [{ required: true, message: '上级部门不能为空', trigger: 'blur' }],
          path: [{ required: true, message: '路由路径不能为空', trigger: 'blur' }],
          title: [{ required: true, message: '菜单标题不能为空', trigger: 'blur' }],
          weight: [{ required: true, message: '排列顺序不能为空', trigger: 'blur' }]
        },
        dialog: false,
        authorityTree: []
      },
      authority: {
        menuId: -1
      }
    }
  },
  mounted() {
    this.tablePage()
  },
  methods: {
    tableRowClassName({ row, rowIndex }) {
      if (this.authority.menuId === row.id) {
        return 'success-row'
      } else {
        return ''
      }
    },
    tableSort(row, offset) {
      this.tableData(row)
      menuSort({ menuId: row.id, index: row.weight + offset }).then(res => {
        this.tablePage()
      })
    },
    tableCreate(row) {
      this.form.dialog = true
      this.form.data = Object.assign({}, _defaultRow)
      menuTree().then(response => {
        this.form.authorityTree = [{ id: -1, title: '主类目', children: [...response.data] }]
        this.form.data.parentId = row.id === undefined ? -1 : row.id
        this.tableData(row)
      })
    },
    tableDelete(row) {
      this.tableData(row)
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        menuDel([row.id]).then(response => {
          this.tablePage()
        })
      })
    },
    tableDeleteForce(id) {
      this.$confirm('此操作将删除选中数据,这将删除他他相关的所有绑定以及子集,是非常威胁的操作, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        menuDelForce(id).then(response => {
          this.tablePage()
        })
      })
    },
    tableData(row) {
      this.authority.menuId = row.id
      this.$emit('menuId', row.id)
    },
    tableEdit(row) {
      this.tableData(row)
      this.form.dialog = true
      this.form.data = Object.assign({}, row)
      menuTree().then(response => {
        this.form.authorityTree = [{ id: -1, title: '主类目', children: [...response.data] }]
      })
    },
    tablePage() {
      this.table.loading = true
      menuTree(this.search).then(response => {
        this.table.loading = false
        this.table.data = response.data
        if (this.authority.menuId === -1) {
          this.authority.menuId = this.table.data[0].id
        }
      })
    },
    formSubmit() {
      this.$refs['form'].validate((valid) => {
        if (!valid) return
        if (this.form.data.id === undefined) {
          menuAdd(this.form.data).then((res) => {
            this.form.dialog = false
            this.tablePage()
            this.$refs['form'].resetFields()
          })
        } else {
          menuUpdate(this.form.data).then(res => {
            this.form.dialog = false
            this.tablePage()
          })
        }
      })
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
