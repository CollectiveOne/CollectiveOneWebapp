import Vue from 'vue'
import Router from 'vue-router'

import Hello from '@/views/Hello'
import UserPage from '@/views/UserPage'
import NotFound from '@/views/NotFound'

Vue.use(Router)

export const router = new Router({
  routes: [
    { path: '/', name: 'Hello', component: Hello },
    { path: '/u/:username', name: 'UserPage', component: UserPage },
    { path: '/notFound', name: 'Error', component: NotFound },
    { path: '*', redirect: '/notFound' }
  ]
})
