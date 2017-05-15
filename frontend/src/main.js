// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
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

// axios.interceptors.response.use(function (response) {
//   if (response.status !== 200) {
//     // Vue.$store.dispatch('showOutputMessage', response.message)
//   }
//   return response
// }, function (error) {
//   // Do something with response error
//   debugger
//   Vue.$store.dispatch('showOutputMessage', error.message)
// })

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
