<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">商品管理</h2>
        <button class="btn-add" @click="addProduct">添加商品</button>
      </div>
      
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>图片</th>
            <th>商品名称</th>
            <th>分类</th>
            <th>价格</th>
            <th>库存</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.id }}</td>
            <td>
              <img v-if="product.image" :src="product.image" :alt="product.name" class="product-thumb">
              <span v-else class="no-image">无图片</span>
            </td>
            <td>{{ product.name }}</td>
            <td>{{ product.categoryName }}</td>
            <td>¥{{ product.price }}</td>
            <td>{{ product.stock }}</td>
            <td>
              <span class="status-badge" :class="product.status === 1 ? 'status-active' : 'status-inactive'">
                {{ product.status === 1 ? '上架' : '下架' }}
              </span>
            </td>
            <td>{{ formatDate(product.createTime) }}</td>
            <td>
              <button class="btn btn-edit" @click="editProduct(product.id)">编辑</button>
              <button class="btn btn-delete" @click="deleteProduct(product.id)">删除</button>
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
const products = ref([])

const loadProducts = async () => {
  try {
    const res = await adminApi.productList()
    if (res.code === 200) {
      products.value = res.data
    }
  } catch (error) {
    console.error('加载商品列表失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const addProduct = () => {
  router.push('/admin/product/edit')
}

const editProduct = (id) => {
  router.push(`/admin/product/edit?id=${id}`)
}

const deleteProduct = async (id) => {
  if (!confirm('确定要删除该商品吗？')) return
  
  try {
    const res = await adminApi.productDelete(id)
    if (res.code === 200) {
      alert(res.message)
      loadProducts()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('删除失败')
  }
}

onMounted(() => {
  loadProducts()
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

.product-thumb {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.no-image {
  color: #999;
  font-size: 12px;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  display: inline-block;
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
