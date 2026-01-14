<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">用户登录</h2>
      <form @submit.prevent="handleLogin" v-if="!isForgotPassword">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" required>
        </div>
        <button type="submit" class="btn-login">登录</button>
      </form>
      <form @submit.prevent="handleForgotPassword" v-else>
        <div class="form-group">
          <label>用户名</label>
          <input v-model="forgotForm.username" type="text" placeholder="请输入用户名" required>
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input v-model="forgotForm.phone" type="text" placeholder="请输入注册时的手机号" required>
        </div>
        <div class="form-group">
          <label>验证码</label>
          <div class="verify-code-container">
            <input v-model="forgotForm.verifyCode" type="text" placeholder="请输入验证码" required>
            <button type="button" class="btn-verify" @click="getVerifyCode" :disabled="isCountingDown">
              {{ isCountingDown ? `${countdown}s后重新获取` : '获取验证码' }}
            </button>
          </div>
        </div>
        <div class="form-group">
          <label>新密码</label>
          <input v-model="forgotForm.newPassword" type="password" placeholder="请输入新密码" required>
        </div>
        <button type="submit" class="btn-login">修改密码</button>
      </form>
      <div class="login-footer">
        <span v-if="!isForgotPassword">
          还没有账号？
          <router-link to="/register">立即注册</router-link>
          <span class="forgot-password-link">
            <a @click="toggleForgotPassword">忘记密码？</a>
          </span>
        </span>
        <span v-else>
          <a @click="toggleForgotPassword">返回登录</a>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { userApi } from '../api'

const router = useRouter()
const userStore = useUserStore()

const isForgotPassword = ref(false)
const isCountingDown = ref(false)
const countdown = ref(60)
let countdownTimer = null

const form = ref({
  username: '',
  password: ''
})

const forgotForm = ref({
  username: '',
  phone: '',
  verifyCode: '',
  newPassword: ''
})

const toggleForgotPassword = () => {
  isForgotPassword.value = !isForgotPassword.value
}

const startCountdown = () => {
  isCountingDown.value = true
  countdown.value = 60
  countdownTimer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(countdownTimer)
      isCountingDown.value = false
    }
  }, 1000)
}

const getVerifyCode = async () => {
  if (!forgotForm.value.username || !forgotForm.value.phone) {
    alert('请先输入用户名和手机号')
    return
  }
  
  // 模拟后端验证手机号是否正确
  // 实际项目中应该调用后端接口验证
  alert('验证码已发送，请注意查收\n验证码：123456')
  startCountdown()
}

const handleForgotPassword = async () => {
  if (!forgotForm.value.username || !forgotForm.value.phone || !forgotForm.value.verifyCode || !forgotForm.value.newPassword) {
    alert('请填写完整信息')
    return
  }
  
  try {
    // 使用URLSearchParams来发送参数，确保后端能够正确接收
    const params = new URLSearchParams()
    params.append('username', forgotForm.value.username)
    params.append('phone', forgotForm.value.phone)
    params.append('verifyCode', forgotForm.value.verifyCode)
    params.append('newPassword', forgotForm.value.newPassword)
    
    const res = await userApi.forgotPassword(params)
    if (res.code === 200) {
      alert(res.message)
      toggleForgotPassword()
    } else {
      alert(res.message)
    }
  } catch (error) {
    // 尝试解析错误信息
    console.error('修改密码失败:', error)
    if (error.response) {
      // 服务器返回错误响应
      if (error.response.data && error.response.data.message) {
        alert(error.response.data.message)
      } else if (error.response.data && error.response.data.error) {
        alert(`系统异常: ${error.response.data.error}`)
      } else if (error.response.status === 404) {
        alert('请求的接口不存在，请检查后端服务')
      } else if (error.response.status === 500) {
        alert('服务器内部错误，请稍后重试')
      } else {
        alert(`请求失败，状态码: ${error.response.status}`)
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      alert('网络错误，请检查后端服务是否运行')
    } else {
      // 请求配置出错
      alert(`请求错误: ${error.message}`)
    }
  }
}

const handleLogin = async () => {
  try {
    const formData = new FormData()
    formData.append('username', form.value.username)
    formData.append('password', form.value.password)
    
    const res = await userApi.login(formData)
    if (res.code === 200) {
      userStore.setUser(res.data)
      alert(res.message)
      if (res.data.role === 1) {
        router.push('/admin/index')
      } else {
        router.push('/product/list')
      }
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('登录失败，请重试')
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-box {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  width: 400px;
}

.login-title {
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

.verify-code-container {
  display: flex;
  gap: 10px;
}

.verify-code-container input {
  flex: 1;
}

.btn-verify {
  padding: 0 16px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap;
}

.btn-verify:hover {
  background-color: #5ab02e;
}

.btn-verify:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.btn-login {
  width: 100%;
  padding: 12px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-login:hover {
  background-color: #5ab02e;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.login-footer a {
  color: #67C23A;
  text-decoration: none;
  margin-left: 5px;
}

.login-footer a:hover {
  text-decoration: underline;
}

.forgot-password-link {
  margin-left: 20px;
}
</style>
