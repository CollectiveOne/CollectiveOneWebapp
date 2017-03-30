import Vue from 'vue'
import Router from 'vue-router'

import Hello from '@/components/Hello'
import NotFound from '@/components/NotFound'

Vue.use(Router)

export const router = new Router({
  routes: [
    { path: '/', name: 'Hello', component: Hello },
    { path: '/notFound', name: 'Error', component: NotFound },
    { path: '*', redirect: '/notFound' }
  ]
})
