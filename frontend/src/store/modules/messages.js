const state = {
  outputMessage: 'Output message',
  showOutputMessage: false
}

const getters = {
}

const mutations = {
  setOutputMessage: (state, payload) => {
    state.outputMessage = payload
  },
  setShowOutputMessage: (state, payload) => {
    state.showOutputMessage = payload
  }
}

const actions = {
  showOutputMessage: (context, payload) => {
    context.commit('setOutputMessage', payload)
    context.commit('setShowOutputMessage', true)

    setTimeout(() => {
      context.commit('setShowOutputMessage', false)
    }, 4000)
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
