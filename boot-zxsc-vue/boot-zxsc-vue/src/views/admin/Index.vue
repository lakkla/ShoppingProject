<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <div class="page-header">
        <h2 class="page-title">后台首页</h2>
      </div>
      
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-value">{{ stats.userCount || 0 }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-value">{{ stats.productCount || 0 }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-value">{{ stats.orderCount || 0 }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-value">{{ stats.categoryCount || 0 }}</div>
            <div class="stat-label">分类总数</div>
          </div>
        </div>
      </div>

      <div class="welcome-card">
        <h3>欢迎使用在线商城管理系统</h3>
        <p>您可以通过左侧菜单管理用户、商品、分类和订单。</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AdminSidebar from '../../components/AdminSidebar.vue'
import { adminApi } from '../../api'

const stats = ref({
  userCount: 0,
  productCount: 0,
  orderCount: 0,
  categoryCount: 0
})

const loadStats = async () => {
  try {
    const [users, products, orders, categories] = await Promise.all([
      adminApi.userList(),
      adminApi.productList(),
      adminApi.orderList(),
      adminApi.categoryList()
    ])
    
    stats.value = {
      userCount: users.data?.length || 0,
      productCount: products.data?.length || 0,
      orderCount: orders.data?.length || 0,
      categoryCount: categories.data?.length || 0
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  loadStats()
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
  margin-bottom: 30px;
}

.page-title {
  font-size: 24px;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 25px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
}

.user-icon {
  background-color: #e3f2fd;
}

.product-icon {
  background-color: #f3e5f5;
}

.order-icon {
  background-color: #e8f5e9;
}

.category-icon {
  background-color: #fff3e0;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.welcome-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.welcome-card h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 15px;
}

.welcome-card p {
  color: #666;
  line-height: 1.6;
}
</style>
