<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">{{ userId ? '编辑用户' : '添加用户' }}</h2>
      </div>
      
      <div class="form-card">
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="form.username" type="text" :disabled="!!userId" required>
          </div>
          <div class="form-group">
            <label>姓名</label>
            <input v-model="form.name" type="text" required>
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input v-model="form.phone" type="text" required>
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input v-model="form.email" type="email" required>
          </div>
          <div class="form-group">
            <label>角色</label>
            <select v-model.number="form.role" required>
              <option :value="0">普通用户</option>
              <option :value="1">管理员</option>
            </select>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select v-model.number="form.status" required>
              <option :value="1">启用</option>
              <option :value="0">禁用</option>
            </select>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-secondary" @click="$router.back()">取消</button>
            <button v-if="userId" type="button" class="btn btn-warning" @click="resetPassword">重置密码</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import AdminSidebar from '../../components/AdminSidebar.vue'
import { adminApi, userApi } from '../../api'

const router = useRouter()
const route = useRoute()

const userId = ref(route.query.id || null)

const form = ref({
  username: '',
  name: '',
  phone: '',
  email: '',
  role: 0,
  status: 1
})

const loadUser = async () => {
  if (!userId.value) return
  
  try {
    const res = await adminApi.userList()
    if (res.code === 200) {
      const user = res.data.find(u => u.id == userId.value)
      if (user) {
        form.value = {
          username: user.username,
          name: user.name,
          phone: user.phone,
          email: user.email,
          role: user.role,
          status: user.status
        }
      }
    }
  } catch (error) {
    console.error('加载用户信息失败', error)
  }
}

const saveUser = async () => {
  try {
    const data = {
      ...form.value,
      id: userId.value
    }
    
    const res = await adminApi.userUpdate(data)
    if (res.code === 200) {
      alert(res.message)
      router.push('/admin/user/list')
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('保存失败')
  }
}

const resetPassword = async () => {
  if (!confirm('确定要重置该用户密码为123456吗？')) return
  
  try {
    const res = await adminApi.userResetPassword(userId.value)
    if (res.code === 200) {
      alert(res.message)
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('重置失败')
  }
}

onMounted(() => {
  loadUser()
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  background-color: #f5f5f5;
  padding: 30px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  color: #333;
}

.form-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  max-width: 600px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
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

.form-group input,
.form-group select {
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

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #67C23A;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

.btn {
  padding: 12px 30px;
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

.btn-secondary {
  background-color: #ddd;
  color: #333;
}

.btn-secondary:hover {
  background-color: #ccc;
}

.btn-warning {
  background-color: #e6a23c;
  color: white;
}

.btn-warning:hover {
  background-color: #cf9236;
}
</style>
