import Vue from 'vue'

const state = {
  contentAnimationType: 'slideToDown',
  initiativesTree: null
}

const findLevel = function (initiatives, id, level) {
  for (var ix in initiatives) {
    if (initiatives[ix].id === id) {
      return level
    }

    if (initiatives[ix].subInitiatives.length > 0) {
      var subLevel = level + 1
      var foundLevel = findLevel(initiatives[ix].subInitiatives, id, subLevel)
      if (foundLevel !== -1) {
        /* stop searching if found in a subinitiative */
        return foundLevel
      }
    }
  }
  return -1
}

const getters = {
  initiativeLevel: (state, getters) => (id) => {
    return findLevel(state.initiativesTree, id, 0)
  }
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
  },
  setInitiativesTree: (state, payload) => {
    state.initiativesTree = payload
  }
}

const actions = {
  updatedMyInitiatives: (context) => {
    Vue.axios.get('/1/secured/initiatives/mines').then((response) => {
      context.commit('setInitiativesTree', response.data.data)
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
