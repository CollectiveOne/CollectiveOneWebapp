const state = {
  levels: 1,
  isInfiniteLevels: false,
  cardsType: 'card',
  showPrivate: true,
  showShared: true,
  showCommon: true
}

const getters = {
  getActualLevels: (state) => {
    if (state.isInfiniteLevels) {
      return 999
    } else {
      return state.levels
    }
  }
}

const mutations = {
  setLevels: (state, payload) => {
    state.levels = payload
  },
  levelUp: (state) => {
    if (!state.infiniteLevels) {
      state.levels = state.levels + 1
    }
  },
  levelDown: (state) => {
    if (!state.infiniteLevels) {
      if (state.levels > 1) {
        state.levels = state.levels - 1
      }
    }
  },
  toggleInifinteLevels: (state) => {
    state.isInfiniteLevels = !state.isInfiniteLevels
  },
  setCardsType: (state, payload) => {
    state.cardsType = payload
  },
  toggleShowPrivate: (state) => {
    state.showPrivate = !state.showPrivate
  },
  toggleShowShared: (state) => {
    state.showShared = !state.showShared
  },
  toggleShowCommon: (state) => {
    state.showCommon = !state.showCommon
  },
  showAllTypes: (state) => {
    state.showPrivate = true
    state.showShared = true
    state.showCommon = true
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
