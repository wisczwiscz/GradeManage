import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建Axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 直接使用后端地址
  timeout: 10000, // 超时时间
  withCredentials: false, // 不再携带cookie
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    console.log('发送请求:', config.url)
    return config
  },
  error => {
    console.error('请求发送失败', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 2xx 范围内的状态码都会触发该函数
    const res = response.data
    
    // 如果接口返回的状态码不是200，提示错误信息
    if (res.code !== 200) {
      ElMessage({
        message: res.message || '操作失败',
        type: 'error',
        duration: 3000
      })
      
      // 如果是未授权，跳转到登录页
      if (res.code === 401) {
        localStorage.removeItem('user')
        localStorage.removeItem('userRole')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '操作失败'))
    }
    
    return res
  },
  error => {
    // 超出 2xx 范围内的状态码都会触发该函数
    console.error('请求失败', error)
    
    // 如果响应不存在，网络错误
    if (!error.response) {
      ElMessage({
        message: '网络错误，请检查网络连接',
        type: 'error',
        duration: 3000
      })
      return Promise.reject(error)
    }
    
    // 处理HTTP状态码
    if (error.response.status === 401) {
      localStorage.removeItem('user')
      localStorage.removeItem('userRole')
      router.push('/login')
      ElMessage({
        message: '登录已过期，请重新登录',
        type: 'error',
        duration: 3000
      })
    } else if (error.response.status === 403) {
      console.error('403错误详情:', error.response)
      ElMessage({
        message: '无权限访问该资源',
        type: 'error',
        duration: 3000
      })
    } else {
      ElMessage({
        message: error.response.data?.message || '请求失败',
        type: 'error',
        duration: 3000
      })
    }
    
    return Promise.reject(error)
  }
)

export default request 