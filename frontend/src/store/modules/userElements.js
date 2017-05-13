import Vue from 'vue'

const state = {
  initiatives: []
}

const getters = {
}

const mutations = {
  setInitiatives: (state, payload) => {
    state.initiatives = payload
  }
}

const actions = {
  updateUserInitiatives: (context) => {
    Vue.axios.get('/1/secured/initiatives/mines').then((response) => {
      context.commit('setInitiatives', response.data.data)
    }).catch((error) => {
      console.log(error)
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
