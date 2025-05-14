<script setup>
import { onMounted } from 'vue'
import axios from 'axios'
import request from './utils/request'

onMounted(() => {
  // 在这里我们确保全局axios配置和我们的request实例一致
  axios.defaults.withCredentials = false
  axios.defaults.headers.common['Content-Type'] = 'application/json'
  
  // 设置默认超时时间
  axios.defaults.timeout = 10000
})
</script>

<template>
  <div class="app-container">
    <router-view />
  </div>
</template>

<style>
.app-container {
  width: 100%;
  min-height: 100vh;
}
</style>