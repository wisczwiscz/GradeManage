import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000, // 你想要的端口号
    // 禁用代理，改用直接访问后端
    /*
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务器地址，根据实际情况修改
        changeOrigin: true
      }
    }
    */
  }
});
