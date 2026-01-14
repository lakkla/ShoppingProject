<template>
  <div class="header">
    <div class="header-content">
      <div class="logo">在线商城</div>
      <div class="nav">
        <router-link to="/product/list">首页</router-link>
        <router-link to="/cart/list" v-if="userStore.isLoggedIn">购物车</router-link>
        <router-link to="/order/list" v-if="userStore.isLoggedIn">我的订单</router-link>
        <router-link to="/user/profile" v-if="userStore.isLoggedIn">个人信息</router-link>
        <router-link to="/admin/index" v-if="userStore.isAdmin">返回管理后端</router-link>
        <a @click="handleLogout" v-if="userStore.isLoggedIn">退出</a>
        <router-link to="/login" v-if="!userStore.isLoggedIn">登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { userApi } from '../api'

const router = useRouter()
const userStore = useUserStore()

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
.header {
  background-color: #67C23A;
  color: white;
  padding: 15px 0;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav a {
  color: white;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.nav a:hover {
  background-color: rgba(255,255,255,0.2);
}
</style>
