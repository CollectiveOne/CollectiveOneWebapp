const state = {
  levels: 1,
  cardsType: 'card',
  showPrivate: true,
  showShared: true,
  showCommon: true
}

const getters = {
}

const mutations = {
  setLevels: (state, payload) => {
    state.levels = payload
  },
  setCardsType: (state, payload) => {
    state.cardsType = payload
  },
  setShowPrivate: (state, payload) => {
    state.showPrivate = payload
  },
  setShowShared: (state, payload) => {
    state.showShared = payload
  },
  setShowCommon: (state, payload) => {
    state.showCommon = payload
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
