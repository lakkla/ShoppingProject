import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  
  const isLoggedIn = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === 1)
  
  function setUser(userData) {
    user.value = userData
    if (userData) {
      localStorage.setItem('user', JSON.stringify(userData))
    } else {
      localStorage.removeItem('user')
    }
  }
  
  function loadUser() {
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      user.value = JSON.parse(savedUser)
    }
  }
  
  function logout() {
    user.value = null
    localStorage.removeItem('user')
  }
  
  return {
    user,
    isLoggedIn,
    isAdmin,
    setUser,
    loadUser,
    logout
  }
})
