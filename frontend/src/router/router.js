import Vue from 'vue'
import Router from 'vue-router'

import Hello from '@/components/Hello'
import Login from '@/components/Login'
import NotFound from '@/components/NotFound'

Vue.use(Router)

export const router = new Router({
  routes: [
    { path: '/', name: 'Home', component: Hello },
    { path: '/login', name: 'Login', component: Login },
    { path: '/notFound', name: 'Error', component: NotFound },
    { path: '*', redirect: '/notFound' }
  ]
})