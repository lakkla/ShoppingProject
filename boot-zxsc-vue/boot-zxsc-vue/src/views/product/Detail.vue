<template>
  <div>
    <Header />

    <div class="container">
      <div class="detail-card" v-if="product">
        <div class="product-image">
          <img v-if="product.image" :src="product.image" :alt="product.name">
          <div v-else class="no-image">暂无图片</div>
        </div>
        <div class="product-info">
          <div class="product-name">{{ product.name }}</div>
          <div class="product-price">¥{{ product.price }}</div>
          <div class="product-category">分类：{{ product.categoryName }}</div>
          <div class="product-stock">库存：{{ product.stock }}</div>
          <div class="product-description">{{ product.description }}</div>
          
          <div class="quantity-selector">
            <span>数量：</span>
            <button @click="decreaseQuantity">-</button>
            <input v-model.number="quantity" type="number" min="1" :max="product.stock">
            <button @click="increaseQuantity">+</button>
          </div>
          
          <div class="action-buttons">
            <button class="btn btn-primary" @click="addToCart">加入购物车</button>
            <button class="btn btn-secondary" @click="goBack">返回</button>
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

const product = ref(null)
const quantity = ref(1)

const loadProduct = async () => {
  try {
    const res = await productApi.detail(route.params.id)
    if (res.code === 200) {
      product.value = res.data
    }
  } catch (error) {
    console.error('加载商品失败', error)
  }
}

const increaseQuantity = () => {
  if (quantity.value < product.value.stock) {
    quantity.value++
  }
}

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const addToCart = async () => {
  try {
    const formData = new FormData()
    formData.append('productId', product.value.id)
    formData.append('quantity', quantity.value)
    
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

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadProduct()
})
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.detail-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 40px;
}

.product-image {
  width: 400px;
  height: 400px;
  background-color: #f5f5f5;
  border-radius: 8px;
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
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.product-name {
  font-size: 24px;
  color: #333;
  font-weight: bold;
}

.product-price {
  color: #67C23A;
  font-size: 32px;
  font-weight: bold;
}

.product-category,
.product-stock {
  color: #666;
  font-size: 16px;
}

.product-description {
  color: #666;
  line-height: 1.6;
  margin-top: 10px;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}

.quantity-selector button {
  width: 30px;
  height: 30px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.quantity-selector button:hover {
  background-color: #f5f5f5;
}

.quantity-selector input {
  width: 60px;
  text-align: center;
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  flex: 1;
  padding: 12px;
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
