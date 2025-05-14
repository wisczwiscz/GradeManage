import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'scores',
        name: 'Scores',
        component: () => import('../views/Scores.vue')
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue')
      },
      {
        path: 'add-score',
        name: 'AddScore',
        component: () => import('../views/AddScore.vue'),
        meta: { requiresTeacher: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = !!localStorage.getItem('user')
  const userRole = localStorage.getItem('userRole')
  
  // 检查是否需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isLoggedIn) {
      next({ name: 'Login' })
    } else if (to.matched.some(record => record.meta.requiresTeacher)) {
      // 检查是否需要教师权限
      if (userRole !== 'TEACHER') {
        // 如果不是教师，则跳转到成绩查询页面
        next({ name: 'Scores' })
      } else {
        next()
      }
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router 