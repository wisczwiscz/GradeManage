<script setup>
import { ref } from 'vue'

defineProps({
  msg: String,
})

const count = ref(0)
</script>

<template>
  <div >
    <h1>前端页面</h1>
    <p>这是一个前端页面</p>
    <p>这是一个前端页面, 已完成前端项目构建</p>
  </div>
</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
