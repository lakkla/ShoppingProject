<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">分类管理</h2>
        <button class="btn-add" @click="showAddDialog">添加分类</button>
      </div>
      
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>分类名称</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in categories" :key="category.id">
            <td>{{ category.id }}</td>
            <td>{{ category.name }}</td>
            <td>{{ formatDate(category.createTime) }}</td>
            <td>
              <button class="btn btn-edit" @click="showEditDialog(category)">编辑</button>
              <button class="btn btn-delete" @click="deleteCategory(category.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="showDialog" class="dialog-overlay" @click="closeDialog">
        <div class="dialog-content" @click.stop>
          <h3>{{ editingCategory ? '编辑分类' : '添加分类' }}</h3>
          <form @submit.prevent="saveCategory">
            <div class="form-group">
              <label>分类名称</label>
              <input v-model="categoryForm.name" type="text" required>
            </div>
            <div class="dialog-actions">
              <button type="submit" class="btn btn-primary">保存</button>
              <button type="button" class="btn btn-secondary" @click="closeDialog">取消</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminSidebar from '../../components/AdminSidebar.vue'
import { adminApi } from '../../api'

const categories = ref([])
const showDialog = ref(false)
const editingCategory = ref(null)
const categoryForm = ref({
  name: ''
})

const loadCategories = async () => {
  try {
    const res = await adminApi.categoryList()
    if (res.code === 200) {
      categories.value = res.data
    }
  } catch (error) {
    console.error('加载分类列表失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const showAddDialog = () => {
  editingCategory.value = null
  categoryForm.value = { name: '' }
  showDialog.value = true
}

const showEditDialog = (category) => {
  editingCategory.value = category
  categoryForm.value = { name: category.name }
  showDialog.value = true
}

const closeDialog = () => {
  showDialog.value = false
  editingCategory.value = null
  categoryForm.value = { name: '' }
}

const saveCategory = async () => {
  try {
    const data = {
      name: categoryForm.value.name,
      id: editingCategory.value?.id
    }
    
    const res = await adminApi.categorySave(data)
    if (res.code === 200) {
      alert(res.message)
      closeDialog()
      loadCategories()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('保存失败')
  }
}

const deleteCategory = async (id) => {
  if (!confirm('确定要删除该分类吗？')) return
  
  try {
    const res = await adminApi.categoryDelete(id)
    if (res.code === 200) {
      alert(res.message)
      loadCategories()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('删除失败')
  }
}

onMounted(() => {
  loadCategories()
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  color: #333;
}

.btn-add {
  padding: 10px 20px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-add:hover {
  background-color: #5ab02e;
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

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  width: 400px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.3);
}

.dialog-content h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 20px;
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

.dialog-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.btn-primary {
  flex: 1;
  padding: 12px;
  background-color: #67C23A;
  color: white;
}

.btn-primary:hover {
  background-color: #5ab02e;
}

.btn-secondary {
  flex: 1;
  padding: 12px;
  background-color: #ddd;
  color: #333;
}

.btn-secondary:hover {
  background-color: #ccc;
}
</style>
