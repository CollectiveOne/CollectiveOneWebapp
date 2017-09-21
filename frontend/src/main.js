// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import beforeEach from './router/beforeEach'
import { store } from './store/store'

import axios from 'axios'
import VueAxios from 'vue-axios'

import VueMarkdown from 'vue-markdown'
import VueSimplemde from 'vue-simplemde'

Vue.config.productionTip = false

Vue.use(VueAxios, axios)
Vue.use(VueSimplemde)

axios.interceptors.request.use(
  function (config) {
    config.headers.common.Authorization = 'Bearer ' + localStorage.getItem('id_token')
    return config
  },
  function (error) {
    return Promise.reject(error)
  }
)

import ErrorPanel from '@/components/global/ErrorPanel.vue'
import MarkdownEditor from '@/components/global/MarkdownEditor.vue'

Vue.component('app-error-panel', ErrorPanel)
Vue.component('vue-markdown', VueMarkdown)
Vue.component('app-markdown-editor', MarkdownEditor)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})

router.beforeEach(beforeEach)
