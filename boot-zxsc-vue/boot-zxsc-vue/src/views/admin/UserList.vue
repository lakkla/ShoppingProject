<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">用户管理</h2>
      </div>
      
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>姓名</th>
            <th>手机号</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span class="role-badge" :class="user.role === 1 ? 'role-admin' : 'role-user'">
                {{ user.role === 1 ? '管理员' : '普通用户' }}
              </span>
            </td>
            <td>
              <span class="status-badge" :class="user.status === 1 ? 'status-active' : 'status-inactive'">
                {{ user.status === 1 ? '启用' : '禁用' }}
              </span>
            </td>
            <td>{{ formatDate(user.createTime) }}</td>
            <td>
              <button class="btn btn-edit" @click="editUser(user.id)">编辑</button>
              <button v-if="user.role !== 1" class="btn btn-delete" @click="deleteUser(user.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import AdminSidebar from '../../components/AdminSidebar.vue'
import { adminApi } from '../../api'

const router = useRouter()
const users = ref([])

const loadUsers = async () => {
  try {
    const res = await adminApi.userList()
    if (res.code === 200) {
      users.value = res.data
    }
  } catch (error) {
    console.error('加载用户列表失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const editUser = (id) => {
  router.push(`/admin/user/edit?id=${id}`)
}

const deleteUser = async (id) => {
  if (!confirm('确定要删除该用户吗？')) return
  
  try {
    const res = await adminApi.userDelete(id)
    if (res.code === 200) {
      alert(res.message)
      loadUsers()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('删除失败')
  }
}

onMounted(() => {
  loadUsers()
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

table {
  width: 100%;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  border-collapse: collapse;
}

th {
  background-color: #f5f5f5;
  padding: 15px;
  text-align: left;
  color: #333;
  font-weight: bold;
}

td {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.role-badge,
.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  display: inline-block;
}

.role-admin {
  background-color: #fef0f0;
  color: #f56c6c;
}

.role-user {
  background-color: #f0f9ff;
  color: #409eff;
}

.status-active {
  background-color: #f0f9ff;
  color: #67c23a;
}

.status-inactive {
  background-color: #f4f4f5;
  color: #909399;
}

.btn {
  padding: 6px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 5px;
}

.btn-edit {
  background-color: #67C23A;
  color: white;
}

.btn-edit:hover {
  background-color: #5ab02e;
}

.btn-delete {
  background-color: #f56c6c;
  color: white;
}

.btn-delete:hover {
  background-color: #e85555;
}
</style>
