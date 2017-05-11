import Vue from 'vue'
import Router from 'vue-router'

import InitiativesView from '../components/InitiativesView.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', redirect: '/home' },
    { path: '/home',
      name: 'Home',
      component: InitiativesView,
      children: []}
  ]
})
