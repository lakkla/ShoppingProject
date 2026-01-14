<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">订单管理</h2>
      </div>
      
      <table>
        <thead>
          <tr>
            <th>订单号</th>
            <th>用户</th>
            <th>总价</th>
            <th>收货地址</th>
            <th>状态</th>
            <th>下单时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.orderNo }}</td>
            <td>{{ order.userName }}</td>
            <td>¥{{ order.totalAmount }}</td>
            <td>{{ order.address }}</td>
            <td>
              <span class="status-badge" :class="getStatusClass(order.status)">
                {{ getStatusText(order.status) }}
              </span>
            </td>
            <td>{{ formatDate(order.createTime) }}</td>
            <td>
              <select v-model.number="order.status" @change="updateStatus(order.id, order.status)" class="status-select">
                <option :value="0">待支付</option>
                <option :value="1">已支付</option>
                <option :value="2">已发货</option>
                <option :value="3">已完成</option>
                <option :value="4">已取消</option>
              </select>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminSidebar from '../../components/AdminSidebar.vue'
import { adminApi } from '../../api'

const orders = ref([])

const loadOrders = async () => {
  try {
    const res = await adminApi.orderList()
    if (res.code === 200) {
      orders.value = res.data
    }
  } catch (error) {
    console.error('加载订单列表失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const getStatusText = (status) => {
  const statusMap = {
    0: '待支付',
    1: '已支付',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

const getStatusClass = (status) => {
  const classMap = {
    0: 'status-pending',
    1: 'status-paid',
    2: 'status-shipped',
    3: 'status-completed',
    4: 'status-cancelled'
  }
  return classMap[status] || ''
}

const updateStatus = async (id, status) => {
  try {
    const res = await adminApi.orderUpdateStatus({ id, status })
    if (res.code === 200) {
      alert(res.message)
      loadOrders()
    } else {
      alert(res.message)
      loadOrders()
    }
  } catch (error) {
    alert('更新失败')
    loadOrders()
  }
}

onMounted(() => {
  loadOrders()
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

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  display: inline-block;
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

.status-select {
  padding: 6px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.status-select:focus {
  outline: none;
  border-color: #67C23A;
}
</style>
