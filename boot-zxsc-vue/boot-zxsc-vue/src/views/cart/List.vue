<template>
  <div>
    <Header />

    <div class="container">
      <h2 class="cart-title">我的购物车</h2>
      
      <div v-if="carts.length === 0" class="empty-cart">
        <p>购物车是空的,快去选购商品吧</p>
        <button @click="$router.push('/product/list')">去购物</button>
      </div>

      <div v-else class="cart-content">
        <table>
          <thead>
            <tr>
              <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll"></th>
              <th>商品信息</th>
              <th>单价</th>
              <th>数量</th>
              <th>小计</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="cart in carts" :key="cart.id">
              <td><input type="checkbox" v-model="selectedIds" :value="cart.id" @change="calculateTotal"></td>
              <td>
                <div class="product-info">
                  <div class="product-image">
                    <img v-if="cart.image" :src="cart.image" :alt="cart.productName">
                    <div v-else class="no-image">无图</div>
                  </div>
                  <span>{{ cart.productName }}</span>
                </div>
              </td>
              <td>¥{{ cart.price }}</td>
              <td>
                <div class="quantity-control">
                  <button @click="updateQuantity(cart.id, cart.quantity, -1)">-</button>
                  <input type="number" :value="cart.quantity" readonly>
                  <button @click="updateQuantity(cart.id, cart.quantity, 1, cart.stock)">+</button>
                </div>
              </td>
              <td>¥{{ (cart.price * cart.quantity).toFixed(2) }}</td>
              <td>
                <button class="btn-delete" @click="deleteCart(cart.id)">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
        
        <div class="cart-footer">
          <div>
            已选择 <span>{{ selectedIds.length }}</span> 件商品
          </div>
          <div class="total-price">
            总价：<span>¥{{ totalPrice }}</span>
          </div>
          <button class="btn-checkout" @click="checkout">结算</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/Header.vue'
import { cartApi } from '../../api'

const router = useRouter()

const carts = ref([])
const selectedIds = ref([])
const selectAll = ref(false)

const totalPrice = computed(() => {
  return carts.value
    .filter(cart => selectedIds.value.includes(cart.id))
    .reduce((sum, cart) => sum + cart.price * cart.quantity, 0)
    .toFixed(2)
})

const loadCarts = async () => {
  try {
    const res = await cartApi.list()
    if (res.code === 200) {
      carts.value = res.data
    }
  } catch (error) {
    console.error('加载购物车失败', error)
  }
}

const toggleSelectAll = () => {
  if (selectAll.value) {
    selectedIds.value = carts.value.map(cart => cart.id)
  } else {
    selectedIds.value = []
  }
}

const calculateTotal = () => {
  selectAll.value = selectedIds.value.length === carts.value.length
}

const updateQuantity = async (id, currentQuantity, change, stock) => {
  const newQuantity = currentQuantity + change
  if (newQuantity < 1) {
    alert('数量不能小于1')
    return
  }
  if (stock && newQuantity > stock) {
    alert('库存不足')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('id', id)
    formData.append('quantity', newQuantity)
    
    const res = await cartApi.update(formData)
    if (res.code === 200) {
      loadCarts()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('更新失败')
  }
}

const deleteCart = async (id) => {
  if (!confirm('确定要删除该商品吗？')) return
  
  try {
    const res = await cartApi.delete(id)
    if (res.code === 200) {
      alert(res.message)
      loadCarts()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('删除失败')
  }
}

const checkout = () => {
  if (selectedIds.value.length === 0) {
    alert('请选择要结算的商品')
    return
  }
  router.push({
    path: '/order/create',
    query: { cartIds: selectedIds.value.join(',') }
  })
}

onMounted(() => {
  loadCarts()
})
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.cart-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.empty-cart {
  background: white;
  padding: 60px;
  text-align: center;
  border-radius: 8px;
}

.empty-cart p {
  color: #999;
  font-size: 16px;
  margin-bottom: 20px;
}

.empty-cart button {
  padding: 10px 30px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cart-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background-color: #f5f5f5;
  padding: 12px;
  text-align: left;
  color: #333;
}

td {
  padding: 15px 12px;
  border-bottom: 1px solid #f0f0f0;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-image {
  width: 60px;
  height: 60px;
  background-color: #f5f5f5;
  border-radius: 4px;
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
  font-size: 12px;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 5px;
}

.quantity-control button {
  width: 25px;
  height: 25px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.quantity-control input {
  width: 50px;
  text-align: center;
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.btn-delete {
  padding: 6px 16px;
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-delete:hover {
  background-color: #e85555;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid #f0f0f0;
}

.total-price {
  font-size: 18px;
  color: #333;
}

.total-price span {
  color: #67C23A;
  font-size: 24px;
  font-weight: bold;
  margin-left: 10px;
}

.btn-checkout {
  padding: 12px 40px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-checkout:hover {
  background-color: #5ab02e;
}
</style>
