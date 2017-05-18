import Vue from 'vue'

const state = {
  showNewInitiative: false,
  newInitiativeParent: null
}

const getters = {
}

const mutations = {
  showNewInitiativeModal: (state, payload) => {
    state.showNewInitiative = payload
  },
  setNewInitiativeParent: (state, payload) => {
    state.newInitiativeParent = payload
  }
}

const actions = {
  getNewInitiativeParent: (context, id) => {
    Vue.axios.get('/1/secured/initiative/' + id).then((response) => {
      context.commit('setNewInitiativeParent', response.data.data)
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
