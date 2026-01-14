<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">{{ productId ? '编辑商品' : '添加商品' }}</h2>
      </div>
      
      <div class="form-card">
        <form @submit.prevent="saveProduct">
          <div class="form-group">
            <label>商品名称</label>
            <input v-model="form.name" type="text" required>
          </div>
          <div class="form-group">
            <label>分类</label>
            <select v-model.number="form.categoryId" required>
              <option value="">请选择分类</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>价格</label>
            <input v-model.number="form.price" type="number" step="0.01" required>
          </div>
          <div class="form-group">
            <label>库存</label>
            <input v-model.number="form.stock" type="number" required>
          </div>
          <div class="form-group">
            <label>商品图片</label>
            <div class="image-upload">
              <input type="file" @change="handleFileChange" accept="image/*" ref="fileInput">
              <button type="button" @click="uploadImage" class="btn-upload">上传图片</button>
            </div>
            <div v-if="form.image" class="image-preview">
              <img :src="form.image" alt="商品图片">
            </div>
          </div>
          <div class="form-group">
            <label>商品描述</label>
            <textarea v-model="form.description" rows="5"></textarea>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select v-model.number="form.status" required>
              <option :value="1">上架</option>
              <option :value="0">下架</option>
            </select>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-secondary" @click="$router.back()">取消</button>
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
import { adminApi, productApi, fileApi } from '../../api'

const router = useRouter()
const route = useRoute()

const productId = ref(route.query.id || null)
const fileInput = ref(null)
const selectedFile = ref(null)

const form = ref({
  name: '',
  categoryId: '',
  price: 0,
  stock: 0,
  image: '',
  description: '',
  status: 1
})

const categories = ref([])

const loadCategories = async () => {
  try {
    const res = await productApi.categories()
    if (res.code === 200) {
      categories.value = res.data
    }
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadProduct = async () => {
  if (!productId.value) return
  
  try {
    const res = await productApi.detail(productId.value)
    if (res.code === 200) {
      form.value = {
        name: res.data.name,
        categoryId: res.data.categoryId,
        price: res.data.price,
        stock: res.data.stock,
        image: res.data.image || '',
        description: res.data.description || '',
        status: res.data.status
      }
    }
  } catch (error) {
    console.error('加载商品信息失败', error)
  }
}

const handleFileChange = (e) => {
  selectedFile.value = e.target.files[0]
}

const uploadImage = async () => {
  if (!selectedFile.value) {
    alert('请先选择图片')
    return
  }
  
  try {
    const res = await fileApi.upload(selectedFile.value)
    if (res.code === 200) {
      form.value.image = res.data
      alert('图片上传成功')
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('上传失败，请重试')
  }
}

const saveProduct = async () => {
  try {
    const data = {
      ...form.value,
      id: productId.value
    }
    
    const res = await adminApi.productSave(data)
    if (res.code === 200) {
      alert(res.message)
      router.push('/admin/product/list')
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('保存失败')
  }
}

onMounted(() => {
  loadCategories()
  loadProduct()
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #67C23A;
}

.image-upload {
  display: flex;
  gap: 10px;
  align-items: center;
}

.image-upload input[type="file"] {
  flex: 1;
}

.btn-upload {
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-upload:hover {
  background-color: #3a8ee6;
}

.image-preview {
  margin-top: 10px;
}

.image-preview img {
  max-width: 200px;
  max-height: 200px;
  border: 1px solid #ddd;
  border-radius: 4px;
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
</style>
