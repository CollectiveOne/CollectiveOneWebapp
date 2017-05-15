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
}

export default {
  state,
  getters,
  mutations,
  actions
}
