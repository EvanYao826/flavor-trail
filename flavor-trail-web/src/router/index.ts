import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue')
    },
    {
      path: '/food/:id',
      name: 'food-detail',
      component: () => import('../views/FoodDetail.vue')
    },
    {
      path: '/explore',
      name: 'explore',
      component: () => import('../views/Explore.vue')
    },
    {
      path: '/ranking',
      name: 'ranking',
      component: () => import('../views/Ranking.vue')
    },
    {
      path: '/chat',
      name: 'chat',
      component: () => import('../views/Chat.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/Profile.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    }
  ]
})

export default router