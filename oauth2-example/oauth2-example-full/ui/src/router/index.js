import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
      path: '/login',
      component: () => import('@/views/login/index'),
      hidden: true
  },

  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  // {
  //   path: '/login',
  //   component: Layout,
  //   redirect: '/login/index',
  //   children: [{
  //     path: '/index',
  //     component: () => import('@/views/login/index'),
  //     hidden: true
  //   }]
  // },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'profile', icon: 'user', noCache: true }
      }
    ]
  }

  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/authority/index',
  //   meta: { title: '系统设置', icon: 'user' },
  //   children: [
  //     {
  //       path: 'authority',
  //       component: () => import('@/views/authority/index'),
  //       name: 'Authority',
  //       meta: { title: '权限管理', icon: 'authority' }
  //     },
  //     {
  //       path: 'dept',
  //       component: () => import('@/views/dept/index'),
  //       name: 'Dept',
  //       meta: { title: '部门管理', icon: 'dept' }
  //     },
  //     {
  //       path: 'role',
  //       component: () => import('@/views/role/index'),
  //       name: 'Role',
  //       meta: { title: '角色管理', icon: 'user' }
  //     },
  //     {
  //       path: 'user',
  //       component: () => import('@/views/user/index'),
  //       name: 'User',
  //       meta: { title: '用户管理', icon: 'user' }
  //     }
  //   ]
  // }

  // 404 page must be placed at the end !!!
  // { path: '*', redirect: '/404', hidden: false }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
