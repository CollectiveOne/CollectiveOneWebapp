// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import VueHead from 'vue-head'
import VueMarkdown from 'vue-markdown'

import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'animate.css/animate.css'

import App from './App'
import { router } from './router/router'
import { store } from './store/store'

Vue.use(VueHead)
Vue.use(VueAxios, axios)
Vue.use(BootstrapVue)

Vue.component('vueMarkdown', VueMarkdown)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {
    App
  }
})
