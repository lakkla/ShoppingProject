<template>
  <div>
    <Header />

    <div class="container">
      <div class="detail-card" v-if="order">
        <h2 class="page-title">订单详情</h2>
        
        <div class="info-section">
          <h3>订单信息</h3>
          <div class="info-row">
            <span class="label">订单号：</span>
            <span>{{ order.orderNo }}</span>
          </div>
          <div class="info-row">
            <span class="label">下单时间：</span>
            <span>{{ formatDate(order.createTime) }}</span>
          </div>
          <div class="info-row">
            <span class="label">订单状态：</span>
            <span v-if="order.status === 0" class="status-pending">待支付</span>
            <span v-else-if="order.status === 1" class="status-paid">已支付</span>
            <span v-else-if="order.status === 2" class="status-shipped">已发货</span>
            <span v-else-if="order.status === 3" class="status-completed">已完成</span>
            <span v-else class="status-cancelled">已取消</span>
          </div>
        </div>

        <div class="info-section">
          <h3>收货信息</h3>
          <div class="info-row">
            <span class="label">收货地址：</span>
            <span>{{ order.address }}</span>
          </div>
        </div>

        <div class="info-section">
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
              <tr v-for="item in order.items" :key="item.id">
                <td>
                  <div class="product-info">
                    <div class="product-image">
                      <img v-if="item.image" :src="item.image" :alt="item.productName">
                      <div v-else class="no-image">无图</div>
                    </div>
                    <span>{{ item.productName }}</span>
                  </div>
                </td>
                <td>¥{{ item.price }}</td>
                <td>{{ item.quantity }}</td>
                <td>¥{{ (item.price * item.quantity).toFixed(2) }}</td>
              </tr>
            </tbody>
          </table>
          <div class="total-price">
            订单总价：<span>¥{{ order.totalAmount }}</span>
          </div>
        </div>

        <div class="action-buttons">
          <button @click="$router.back()" class="btn btn-secondary">返回</button>
          <button v-if="order.status === 0" @click="payOrder" class="btn btn-primary">去支付</button>
          <button v-if="order.status === 0" @click="cancelOrder" class="btn btn-cancel">取消订单</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Header from '../../components/Header.vue'
import { orderApi } from '../../api'

const router = useRouter()
const route = useRoute()
const order = ref(null)

const loadOrder = async () => {
  try {
    const res = await orderApi.detail(route.params.id)
    if (res.code === 200) {
      order.value = res.data
    }
  } catch (error) {
    console.error('加载订单失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const payOrder = async () => {
  try {
    const res = await orderApi.pay(order.value.id)
    if (res.code === 200) {
      alert(res.message)
      loadOrder()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('支付失败')
  }
}

const cancelOrder = async () => {
  if (!confirm('确定要取消该订单吗？')) return
  
  try {
    const res = await orderApi.cancel(order.value.id)
    if (res.code === 200) {
      alert(res.message)
      router.push('/order/list')
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('取消失败')
  }
}

onMounted(() => {
  loadOrder()
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
}

.page-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
}

.info-section {
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
}

.info-section:last-of-type {
  border-bottom: none;
}

.info-section h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  margin-bottom: 15px;
  font-size: 14px;
}

.label {
  width: 100px;
  color: #666;
}

.status-pending {
  color: #f56c6c;
}

.status-paid {
  color: #409eff;
}

.status-shipped {
  color: #909399;
}

.status-completed {
  color: #67c23a;
}

.status-cancelled {
  color: #909399;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
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

.total-price {
  text-align: right;
  font-size: 18px;
  color: #333;
  padding-top: 15px;
}

.total-price span {
  color: #67C23A;
  font-size: 24px;
  font-weight: bold;
  margin-left: 10px;
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 30px;
}

.btn {
  padding: 10px 30px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
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

.btn-cancel {
  background-color: #f56c6c;
  color: white;
}

.btn-cancel:hover {
  background-color: #e85555;
}
</style>
