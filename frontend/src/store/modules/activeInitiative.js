import Vue from 'vue'

const state = {
  id: '',
  initiative: null
}

const getters = {
}

const mutations = {
  setId: (state, payload) => {
    state.id = payload
  },
  setInitiative: (state, payload) => {
    state.initiative = payload
  }
}

const actions = {
  setActiveInitiative: (context, id) => {
    context.commit('setId', id)
    context.dispatch('updateActiveInitiative')
  },
  updateActiveInitiative: (context) => {
    Vue.axios.get('/1/secured/initiative/' + context.state.id).then((response) => {
      context.commit('setInitiative', response.data.data)
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
