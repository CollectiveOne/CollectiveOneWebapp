// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import beforeEach from './router/beforeEach'
import { store } from './store/store'

import axios from 'axios'
import VueAxios from 'vue-axios'
import VueAnalytics from 'vue-analytics'

Vue.config.productionTip = false

Vue.use(VueAxios, axios)
Vue.use(VueAnalytics, {
  id: 'UA-92543820-1'
})

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
import VueMarkdown from 'vue-markdown'
import MarkdownEditor from '@/components/global/MarkdownEditor.vue'
import MessagesMarkdownEditor from '@/components/global/MessagesMarkdownEditor.vue'
import Indicator from '@/components/notifications/Indicator.vue'

Vue.component('app-error-panel', ErrorPanel)
Vue.component('vue-markdown', VueMarkdown)
Vue.component('app-markdown-editor', MarkdownEditor)
Vue.component('app-messages-markdown-editor', MessagesMarkdownEditor)
Vue.component('app-indicator', Indicator)

/* registered globally to solve the circular reference */
import ModelSectionWithModal from '@/components/model/ModelSectionWithModal.vue'
import ModelCardWithModal from '@/components/model/ModelCardWithModal.vue'

Vue.component('app-model-section-with-modal', ModelSectionWithModal)
Vue.component('app-model-card-with-modal', ModelCardWithModal)

/* custom directive to detect click outside from
https://stackoverflow.com/questions/36170425/detect-click-outside-element?answertab=votes#tab-top */
Vue.directive('click-outside', {
  bind: function (el, binding, vnode) {
    Vue.event = function (event) {
      if (!(el === event.target || el.contains(event.target))) {
        vnode.context[binding.expression](event)
      }
    }
    document.body.addEventListener('click', Vue.event)
  },
  unbind: function (el) {
    document.body.removeEventListener('click', Vue.event)
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})

router.beforeEach(beforeEach)
