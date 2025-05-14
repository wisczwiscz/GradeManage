<template>
  <div class="dashboard-container">
    <el-container>
      <el-aside width="220px" class="aside">
        <div class="logo">
          <h3>学生成绩管理系统</h3>
        </div>
        <el-menu
          router
          :default-active="activeMenu"
          class="el-menu-vertical"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard/scores">
            <el-icon><Document /></el-icon>
            <span>成绩查询</span>
          </el-menu-item>
          
          <el-menu-item index="/dashboard/statistics">
            <el-icon><PieChart /></el-icon>
            <span>成绩统计</span>
          </el-menu-item>
          
          <el-menu-item v-if="userStore.isTeacher" index="/dashboard/add-score">
            <el-icon><Edit /></el-icon>
            <span>录入成绩</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="header">
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                {{ userStore.user?.username }}
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Document, PieChart, Edit, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 当前活动菜单
const activeMenu = computed(() => route.path)

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      router.push('/login')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
  width: 100%;
  overflow: hidden;
}

.aside {
  background-color: #304156;
  overflow: hidden;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  padding: 0 10px;
}

.header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: flex-end;
  padding: 0 20px;
  width: 100%;
  box-sizing: border-box;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.main {
  background-color: #f5f7fa;
  padding: 20px;
  width: 100%;
  height: calc(100vh - 60px);
  box-sizing: border-box;
  overflow-y: auto;
}

.el-menu-vertical {
  border-right: none;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .el-aside {
    width: 64px !important;
  }
  
  .el-aside .logo h3 {
    display: none;
  }
  
  .el-menu-vertical span {
    display: none;
  }
}
</style> 