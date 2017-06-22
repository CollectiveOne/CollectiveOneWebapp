// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import beforeEach from './router/beforeEach'
import { store } from './store/store'

import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.config.productionTip = false

Vue.use(VueAxios, axios)

axios.interceptors.request.use(
  function (config) {
    config.headers.common.Authorization = 'Bearer ' + localStorage.getItem('id_token')
    return config
  },
  function (error) {
    return Promise.reject(error)
  }
)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})

router.beforeEach(beforeEach)
