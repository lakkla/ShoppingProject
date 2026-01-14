import axios from 'axios'

const request = axios.create({
  timeout: 10000
})

request.interceptors.response.use(
  response => response.data,
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export const userApi = {
  login: (data) => request.post('/api/user/login', data),
  register: (data) => request.post('/api/user/register', data),
  update: (data) => request.post('/api/user/update', data),
  updatePassword: (data) => request.post('/api/user/updatePassword', data),
  forgotPassword: (data) => request.post('/api/user/forgotPassword', data),
  logout: () => request.post('/api/user/logout')
}

export const productApi = {
  list: (params) => request.get('/api/product/list', { params }),
  detail: (id) => request.get(`/api/product/detail/${id}`),
  categories: () => request.get('/api/product/categories')
}

export const cartApi = {
  list: () => request.get('/api/cart/list'),
  add: (data) => request.post('/api/cart/add', data),
  update: (data) => request.post('/api/cart/update', data),
  delete: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/cart/delete', formData)
  },
  clear: () => request.post('/api/cart/clear')
}

export const orderApi = {
  list: () => request.get('/api/order/list'),
  detail: (id) => request.get(`/api/order/detail/${id}`),
  create: (data) => request.post('/api/order/create', data),
  pay: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/order/pay', formData)
  },
  cancel: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/order/cancel', formData)
  }
}

export const adminApi = {
  userList: () => request.get('/api/admin/user/list'),
  userUpdate: (data) => request.post('/api/admin/user/update', data),
  userDelete: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/admin/user/delete', formData)
  },
  userResetPassword: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/admin/user/resetPassword', formData)
  },
  
  productList: () => request.get('/api/admin/product/list'),
  productSave: (data) => request.post('/api/admin/product/save', data),
  productDelete: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/admin/product/delete', formData)
  },
  
  categoryList: () => request.get('/api/admin/category/list'),
  categorySave: (data) => request.post('/api/admin/category/save', data),
  categoryDelete: (id) => {
    const formData = new FormData()
    formData.append('id', id)
    return request.post('/api/admin/category/delete', formData)
  },
  
  orderList: () => request.get('/api/admin/order/list'),
  orderUpdateStatus: (data) => request.post('/api/admin/order/updateStatus', data)
}

export const fileApi = {
  upload: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/api/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}
