import { defineStore } from 'pinia'
import axios from 'axios'
import request from '../utils/request'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    userRole: localStorage.getItem('userRole') || null
  }),
  getters: {
    isLoggedIn: (state) => !!state.user,
    isTeacher: (state) => state.userRole === 'TEACHER'
  },
  actions: {
    async login(username, password) {
      try {
        console.log('尝试登录，用户名:', username);
        // 确保禁用withCredentials
        const response = await axios.post('http://localhost:8080/api/user/login', {
          username,
          password
        }, {
          withCredentials: false,
          headers: {
            'Content-Type': 'application/json'
          }
        })
        
        console.log('登录响应:', response.data);
        if (response.data.code === 200) {
          const userData = response.data.data
          this.user = userData
          this.userRole = userData.role
          localStorage.setItem('user', JSON.stringify(userData))
          localStorage.setItem('userRole', userData.role)
          return { success: true }
        } else {
          return { success: false, message: response.data.message }
        }
      } catch (error) {
        console.error('登录错误详情:', error);
        return { success: false, message: error.response?.data?.message || '登录失败，请稍后再试' }
      }
    },
    
    logout() {
      this.user = null
      this.userRole = null
      localStorage.removeItem('user')
      localStorage.removeItem('userRole')
      // 不再需要调用后端登出接口
    }
  }
}) 