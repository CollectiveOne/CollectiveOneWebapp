// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueResource from 'vue-resource'
import VueHead from 'vue-head'

import App from './App'
import { router } from './router/router'
import { store } from './store/store'

Vue.use(VueHead)
Vue.use(VueResource)

Vue.config.productionTip = false
Vue.http.options.root = 'https://test-api-3cea7.firebaseio.com/'

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
