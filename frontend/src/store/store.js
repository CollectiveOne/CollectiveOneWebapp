import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import initiative from './modules/initiative'
import messages from './modules/messages'
import support from './modules/support'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {},

  modules: {
    user,
    initiative,
    messages,
    support
  }
})
