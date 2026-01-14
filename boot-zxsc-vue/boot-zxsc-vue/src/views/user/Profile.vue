<template>
  <div>
    <Header />

    <div class="container">
      <h2 class="page-title">个人信息</h2>
      
      <div class="profile-card">
        <div class="tab-header">
          <button :class="{ active: activeTab === 'info' }" @click="activeTab = 'info'">基本信息</button>
          <button :class="{ active: activeTab === 'password' }" @click="activeTab = 'password'">修改密码</button>
        </div>

        <div v-show="activeTab === 'info'" class="tab-content">
          <form @submit.prevent="updateInfo">
            <div class="form-group">
              <label>用户名</label>
              <input v-model="infoForm.username" type="text" disabled>
            </div>
            <div class="form-group">
              <label>姓名</label>
              <input v-model="infoForm.name" type="text" required>
            </div>
            <div class="form-group">
              <label>手机号</label>
              <input v-model="infoForm.phone" type="text" required>
            </div>
            <div class="form-group">
              <label>邮箱</label>
              <input v-model="infoForm.email" type="email" required>
            </div>
            <button type="submit" class="btn btn-primary">保存修改</button>
          </form>
        </div>

        <div v-show="activeTab === 'password'" class="tab-content">
          <form @submit.prevent="updatePassword">
            <div class="form-group">
              <label>原密码</label>
              <input v-model="passwordForm.oldPassword" type="password" required>
            </div>
            <div class="form-group">
              <label>新密码</label>
              <input v-model="passwordForm.newPassword" type="password" required>
            </div>
            <div class="form-group">
              <label>确认新密码</label>
              <input v-model="passwordForm.confirmPassword" type="password" required>
            </div>
            <button type="submit" class="btn btn-primary">修改密码</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import Header from '../../components/Header.vue'
import { userApi } from '../../api'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('info')

const infoForm = ref({
  username: '',
  name: '',
  phone: '',
  email: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const loadUserInfo = () => {
  if (userStore.user) {
    infoForm.value = {
      username: userStore.user.username,
      name: userStore.user.name,
      phone: userStore.user.phone,
      email: userStore.user.email
    }
  }
}

const updateInfo = async () => {
  try {
    const formData = new FormData()
    formData.append('name', infoForm.value.name)
    formData.append('phone', infoForm.value.phone)
    formData.append('email', infoForm.value.email)
    
    const res = await userApi.update(formData)
    if (res.code === 200) {
      alert(res.message)
      userStore.setUser({ 
        ...userStore.user, 
        name: infoForm.value.name,
        phone: infoForm.value.phone,
        email: infoForm.value.email
      })
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('更新失败')
  }
}

const updatePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert('两次密码输入不一致')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('oldPassword', passwordForm.value.oldPassword)
    formData.append('newPassword', passwordForm.value.newPassword)
    
    const res = await userApi.updatePassword(formData)
    if (res.code === 200) {
      alert(res.message)
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('修改失败')
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 30px auto;
  padding: 0 20px;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.profile-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.tab-header {
  display: flex;
  border-bottom: 2px solid #f0f0f0;
}

.tab-header button {
  flex: 1;
  padding: 15px;
  background: none;
  border: none;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-header button.active {
  color: #67C23A;
  border-bottom: 2px solid #67C23A;
  margin-bottom: -2px;
}

.tab-content {
  padding: 30px;
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

.form-group input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.form-group input:focus {
  outline: none;
  border-color: #67C23A;
}

.btn {
  padding: 12px 40px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-primary {
  background-color: #67C23A;
  color: white;
}

.btn-primary:hover {
  background-color: #5ab02e;
}
</style>
