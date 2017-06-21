const state = {
  contentAnimationType: 'slideToDown',
  selectedInitiativeLevel: null,
  selectedInitiativeLevelOld: null
}

const getters = {
}

const mutations = {
  setContentAnimationType: (state, payload) => {
    state.contentAnimationType = payload
  },
  setSelectedInitiativeLevel: (state, payload) => {
    state.selectedInitiativeLevel = payload
  },
  selectedInitiativeLevelOld: (state, payload) => {
    state.selectedInitiativeLevelOld = payload
  }
}

const actions = {
}

export default {
  state,
  getters,
  mutations,
  actions
}
