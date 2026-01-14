<template>
  <div>
    <Header />

    <div class="container">
      <h2 class="page-title">确认订单</h2>
      
      <div class="order-content">
        <div class="section">
          <h3>收货信息</h3>
          <form class="address-form">
            <div class="form-group">
              <label>收货地址：</label>
              <textarea v-model="form.address" placeholder="请输入收货人姓名、联系电话和详细地址" rows="4" required></textarea>
            </div>
          </form>
        </div>

        <div class="section">
          <h3>商品清单</h3>
          <table>
            <thead>
              <tr>
                <th>商品信息</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="cart in selectedCarts" :key="cart.id">
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
                <td>{{ cart.quantity }}</td>
                <td>¥{{ (cart.price * cart.quantity).toFixed(2) }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="order-footer">
          <div class="total-info">
            <span>商品总价：</span>
            <span class="total-price">¥{{ totalPrice }}</span>
          </div>
          <button class="btn-submit" @click="submitOrder">提交订单</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Header from '../../components/Header.vue'
import { cartApi, orderApi } from '../../api'

const router = useRouter()
const route = useRoute()

const selectedCarts = ref([])
const form = ref({
  address: ''
})

const totalPrice = computed(() => {
  return selectedCarts.value
    .reduce((sum, cart) => sum + cart.price * cart.quantity, 0)
    .toFixed(2)
})

const loadSelectedCarts = async () => {
  const cartIds = route.query.cartIds
  if (!cartIds) {
    alert('请先选择商品')
    router.push('/cart/list')
    return
  }

  try {
    const res = await cartApi.list()
    if (res.code === 200) {
      const ids = cartIds.split(',').map(id => parseInt(id))
      selectedCarts.value = res.data.filter(cart => ids.includes(cart.id))
      
      if (selectedCarts.value.length === 0) {
        alert('购物车商品不存在')
        router.push('/cart/list')
      }
    }
  } catch (error) {
    console.error('加载购物车失败', error)
  }
}

const submitOrder = async () => {
  if (!form.value.address || form.value.address.trim() === '') {
    alert('请填写收货地址')
    return
  }

  try {
    const formData = new FormData()
    formData.append('cartIds', route.query.cartIds)
    formData.append('receiverName', '')
    formData.append('receiverPhone', '')
    formData.append('receiverAddress', form.value.address)

    const res = await orderApi.create(formData)
    if (res.code === 200) {
      alert('订单创建成功')
      router.push('/order/list')
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('订单创建失败')
  }
}

onMounted(() => {
  loadSelectedCarts()
})
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.order-content {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.section:last-child {
  border-bottom: none;
}

.section h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
}

.address-form {
  max-width: 600px;
}

.form-group {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
}

.form-group label {
  width: 100px;
  padding-top: 10px;
  color: #666;
  font-weight: bold;
}

.form-group input,
.form-group textarea {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #67C23A;
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
  font-weight: bold;
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
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 12px;
}

.order-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 30px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid #f0f0f0;
}

.total-info {
  font-size: 18px;
  color: #333;
}

.total-price {
  color: #67C23A;
  font-size: 24px;
  font-weight: bold;
  margin-left: 10px;
}

.btn-submit {
  padding: 12px 40px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.btn-submit:hover {
  background-color: #5ab02e;
}
</style>
