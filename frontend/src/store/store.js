import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import initiative from './modules/initiative'
import initiativesTree from './modules/initiativesTree'
import messages from './modules/messages'
import support from './modules/support'
import model from './modules/model'
import sectionsTree from './modules/sectionsTree'
import socket from './modules/socket'
import pushManager from './modules/pushManager'
import backup from './modules/backup'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {},

  modules: {
    user,
    initiative,
    initiativesTree,
    messages,
    support,
    model,
    sectionsTree,
    socket,
    pushManager,
    backup
  }
})
