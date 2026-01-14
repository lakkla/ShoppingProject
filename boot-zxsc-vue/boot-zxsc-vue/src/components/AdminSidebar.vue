<template>
  <div class="sidebar">
    <div class="sidebar-header">
      <h2>后台管理</h2>
    </div>
    <nav class="sidebar-nav">
      <router-link to="/admin/index" class="nav-item">
        <span>首页</span>
      </router-link>
      <router-link to="/admin/user/list" class="nav-item">
        <span>用户管理</span>
      </router-link>
      <router-link to="/admin/product/list" class="nav-item">
        <span>商品管理</span>
      </router-link>
      <router-link to="/admin/category/list" class="nav-item">
        <span>分类管理</span>
      </router-link>
      <router-link to="/admin/order/list" class="nav-item">
        <span>订单管理</span>
      </router-link>
      <a @click="backToHome" class="nav-item">
        <span>返回前台</span>
      </a>
      <a @click="handleLogout" class="nav-item">
        <span>退出登录</span>
      </a>
    </nav>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { userApi } from '../api'

const router = useRouter()
const userStore = useUserStore()

const backToHome = () => {
  router.push('/product/list')
}

const handleLogout = async () => {
  try {
    await userApi.logout()
    userStore.logout()
    router.push('/login')
  } catch (error) {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.sidebar {
  width: 200px;
  background-color: #2c3e50;
  min-height: 100vh;
  color: white;
}

.sidebar-header {
  padding: 20px;
  background-color: #67C23A;
  text-align: center;
}

.sidebar-header h2 {
  font-size: 20px;
  margin: 0;
}

.sidebar-nav {
  padding: 10px 0;
}

.nav-item {
  display: block;
  padding: 15px 20px;
  color: white;
  text-decoration: none;
  cursor: pointer;
  transition: background-color 0.3s;
}

.nav-item:hover {
  background-color: #34495e;
}

.nav-item.router-link-active {
  background-color: #67C23A;
}
</style>
