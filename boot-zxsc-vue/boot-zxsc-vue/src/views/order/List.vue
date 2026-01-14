<template>
  <div>
    <Header />

    <div class="container">
      <h2 class="page-title">我的订单</h2>
      
      <div v-if="orders.length === 0" class="empty-order">
        <p>暂无订单</p>
        <button @click="$router.push('/product/list')">去购物</button>
      </div>

      <div v-else class="order-list">
        <div class="order-card" v-for="order in orders" :key="order.id">
          <div class="order-header">
            <span>订单号：{{ order.orderNo }}</span>
            <span>收货地址：{{ order.address }}</span>
            <span>下单时间：{{ formatDate(order.createTime) }}</span>
          </div>
          <div class="order-body">
            <div class="order-items">
              <div class="order-item" v-for="item in order.items" :key="item.id">
                <div class="item-image">
                  <img v-if="item.image" :src="item.image" :alt="item.productName">
                  <div v-else class="no-image">无图</div>
                </div>
                <div class="item-info">
                  <div class="item-name">{{ item.productName }}</div>
                  <div class="item-price">¥{{ item.price }} × {{ item.quantity }}</div>
                </div>
              </div>
            </div>
            <div class="order-info">
              <div class="order-total">总价：¥{{ order.totalAmount }}</div>
              <div class="order-status">
                <span v-if="order.status === 0" class="status-pending">待支付</span>
                <span v-else-if="order.status === 1" class="status-paid">已支付</span>
                <span v-else-if="order.status === 2" class="status-shipped">已发货</span>
                <span v-else-if="order.status === 3" class="status-completed">已完成</span>
                <span v-else class="status-cancelled">已取消</span>
              </div>
              <div class="order-actions">
                <button @click="viewDetail(order.id)" class="btn btn-detail">查看详情</button>
                <button v-if="order.status === 0" @click="payOrder(order.id)" class="btn btn-pay">去支付</button>
                <button v-if="order.status === 0" @click="cancelOrder(order.id)" class="btn btn-cancel">取消订单</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '../../components/Header.vue'
import { orderApi } from '../../api'

const router = useRouter()
const orders = ref([])

const loadOrders = async () => {
  try {
    const res = await orderApi.list()
    if (res.code === 200) {
      orders.value = res.data
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

const viewDetail = (id) => {
  router.push(`/order/detail/${id}`)
}

const payOrder = async (id) => {
  try {
    const res = await orderApi.pay(id)
    if (res.code === 200) {
      alert(res.message)
      loadOrders()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('支付失败')
  }
}

const cancelOrder = async (id) => {
  if (!confirm('确定要取消该订单吗？')) return
  
  try {
    const res = await orderApi.cancel(id)
    if (res.code === 200) {
      alert(res.message)
      loadOrders()
    } else {
      alert(res.message)
    }
  } catch (error) {
    alert('取消失败')
  }
}

onMounted(() => {
  loadOrders()
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

.empty-order {
  background: white;
  padding: 60px;
  text-align: center;
  border-radius: 8px;
}

.empty-order p {
  color: #999;
  font-size: 16px;
  margin-bottom: 20px;
}

.empty-order button {
  padding: 10px 30px;
  background-color: #67C23A;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.order-header {
  background-color: #f5f5f5;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 14px;
}

.order-body {
  padding: 20px;
  display: flex;
  gap: 20px;
}

.order-items {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.order-item {
  display: flex;
  gap: 15px;
}

.item-image {
  width: 80px;
  height: 80px;
  background-color: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}

.item-image img {
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

.item-info {
  flex: 1;
}

.item-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.item-price {
  color: #666;
  font-size: 14px;
}

.order-info {
  width: 200px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: flex-end;
}

.order-total {
  font-size: 18px;
  color: #67C23A;
  font-weight: bold;
}

.order-status span {
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 14px;
}

.status-pending {
  background-color: #fef0f0;
  color: #f56c6c;
}

.status-paid {
  background-color: #f0f9ff;
  color: #409eff;
}

.status-shipped {
  background-color: #f4f4f5;
  color: #909399;
}

.status-completed {
  background-color: #f0f9ff;
  color: #67c23a;
}

.status-cancelled {
  background-color: #f4f4f5;
  color: #909399;
}

.order-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-detail {
  background-color: #67C23A;
  color: white;
}

.btn-detail:hover {
  background-color: #5ab02e;
}

.btn-pay {
  background-color: #409eff;
  color: white;
}

.btn-pay:hover {
  background-color: #3a8ee6;
}

.btn-cancel {
  background-color: #f56c6c;
  color: white;
}

.btn-cancel:hover {
  background-color: #e85555;
}
</style>
