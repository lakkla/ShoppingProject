import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/',
    redirect: '/product/list'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/product/list',
    name: 'ProductList',
    component: () => import('../views/product/List.vue')
  },
  {
    path: '/product/detail/:id',
    name: 'ProductDetail',
    component: () => import('../views/product/Detail.vue')
  },
  {
    path: '/cart/list',
    name: 'CartList',
    component: () => import('../views/cart/List.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order/list',
    name: 'OrderList',
    component: () => import('../views/order/List.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order/create',
    name: 'OrderCreate',
    component: () => import('../views/order/Create.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order/detail/:id',
    name: 'OrderDetail',
    component: () => import('../views/order/Detail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user/profile',
    name: 'UserProfile',
    component: () => import('../views/user/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/index',
    name: 'AdminIndex',
    component: () => import('../views/admin/Index.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/user/list',
    name: 'AdminUserList',
    component: () => import('../views/admin/UserList.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/user/edit',
    name: 'AdminUserEdit',
    component: () => import('../views/admin/UserEdit.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/product/list',
    name: 'AdminProductList',
    component: () => import('../views/admin/ProductList.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/product/edit',
    name: 'AdminProductEdit',
    component: () => import('../views/admin/ProductEdit.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/category/list',
    name: 'AdminCategoryList',
    component: () => import('../views/admin/CategoryList.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/order/list',
    name: 'AdminOrderList',
    component: () => import('../views/admin/OrderList.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.meta.requiresAdmin && userStore.user?.role !== 1) {
    next('/product/list')
  } else {
    next()
  }
})

export default router
