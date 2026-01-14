<template>
  <div>
    <Header />
    
    <div class="search-bar">
      <input v-model="keyword" type="text" placeholder="搜索商品..." @keyup.enter="search">
      <button @click="search">搜索</button>
    </div>

    <div class="container">
      <div class="category-list">
        <a @click="selectCategory(null)" :class="{ active: !currentCategoryId }">全部</a>
        <a v-for="category in categories" :key="category.id" 
           @click="selectCategory(category.id)"
           :class="{ active: currentCategoryId === category.id }">
          {{ category.name }}
        </a>
      </div>

      <div class="product-grid">
        <div class="product-card" v-for="product in products" :key="product.id">
          <div class="product-image" @click="goToDetail(product.id)">
            <img v-if="product.image" :src="product.image" :alt="product.name">
            <div v-else class="no-image">暂无图片</div>
          </div>
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-price">¥{{ product.price }}</div>
            <div class="product-stock">库存：{{ product.stock }}</div>
            <button class="btn-cart" @click="addToCart(product.id)">加入购物车</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Header from '../../components/Header.vue'
import { productApi, cartApi } from '../../api'

const router = useRouter()
const route = useRoute()

const products = ref([])
const categories = ref([])
const keyword = ref('')
const currentCategoryId = ref(null)

const loadProducts = async () => {
  try {
    const params = {}
    if (currentCategoryId.value) {
      params.categoryId = currentCategoryId.value
    }
    if (keyword.value) {
      params.keyword = keyword.value
    }
    const res = await productApi.list(params)
    if (res.code === 200) {
      products.value = res.data
    }
  } catch (error) {
    console.error('加载商品失败', error)
  }
}

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

const selectCategory = (categoryId) => {
  currentCategoryId.value = categoryId
  loadProducts()
}

const search = () => {
  loadProducts()
}

const goToDetail = (id) => {
  router.push(`/product/detail/${id}`)
}

const addToCart = async (productId) => {
  try {
    const formData = new FormData()
    formData.append('productId', productId)
    formData.append('quantity', 1)
    
    const res = await cartApi.add(formData)
    if (res.code === 200) {
      alert(res.message)
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('请先登录')
    router.push('/login')
  }
}

onMounted(() => {
  if (route.query.categoryId) {
    currentCategoryId.value = parseInt(route.query.categoryId)
  }
  if (route.query.keyword) {
    keyword.value = route.query.keyword
  }
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.search-bar {
  background: white;
  padding: 20px;
  display: flex;
  justify-content: center;
  gap: 10px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.search-bar input {
  width: 400px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-bar button {
  padding: 10px 30px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-bar button:hover {
  background-color: #5ab02e;
}

.container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.category-list {
  background: white;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.category-list a {
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
  color: #666;
  text-decoration: none;
}

.category-list a:hover {
  background-color: #f0f0f0;
}

.category-list a.active {
  background-color: #67C23A;
  color: white;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  width: 100%;
  height: 200px;
  background-color: #f5f5f5;
  cursor: pointer;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #67C23A;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
}

.product-stock {
  color: #999;
  font-size: 14px;
  margin-bottom: 10px;
}

.btn-cart {
  width: 100%;
  padding: 10px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cart:hover {
  background-color: #5ab02e;
}
</style>
