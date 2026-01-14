<template>
  <div class="register-container">
    <div class="register-box">
      <h2 class="register-title">用户注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" required>
        </div>
        <div class="form-group">
          <label>确认密码</label>
          <input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" required>
        </div>
        <div class="form-group">
          <label>姓名</label>
          <input v-model="form.name" type="text" placeholder="请输入姓名" required>
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input v-model="form.phone" type="text" placeholder="请输入手机号" required>
        </div>
        <div class="form-group">
          <label>邮箱</label>
          <input v-model="form.email" type="email" placeholder="请输入邮箱" required>
        </div>
        <button type="submit" class="btn-register">注册</button>
      </form>
      <div class="register-footer">
        <span>已有账号？</span>
        <router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '../api'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  phone: '',
  email: ''
})

const handleRegister = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    alert('两次密码输入不一致')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('username', form.value.username)
    formData.append('password', form.value.password)
    formData.append('name', form.value.name)
    formData.append('phone', form.value.phone)
    formData.append('email', form.value.email)
    
    const res = await userApi.register(formData)
    if (res.code === 200) {
      alert(res.message)
      router.push('/login')
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('注册失败，请重试')
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
  padding: 20px 0;
}

.register-box {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  width: 400px;
}

.register-title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #67C23A;
}

.btn-register {
  width: 100%;
  padding: 12px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-register:hover {
  background-color: #5ab02e;
}

.register-footer {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.register-footer a {
  color: #67C23A;
  text-decoration: none;
  margin-left: 5px;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style>
