import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import messages from './modules/messages'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {},

  modules: {
    user,
    messages
  }
})
