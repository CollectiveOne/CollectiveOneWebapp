import Vue from 'vue'

const state = {
  contentAnimationType: 'slideToDown',
  initiativesTree: null,
  triggerUpdateAssets: false
}

const findCoordinate = function (initiatives, id, coord) {
  /** warning coord is a reference! */
  for (var ix in initiatives) {
    var thisInitiative = initiatives[ix]
    var thisCoord = JSON.parse(JSON.stringify(coord))

    thisCoord.push(ix)

    if (thisInitiative.id === id) {
      /* update input reference */
      coord.length = 0
      thisCoord.forEach((e) => {
        coord.push(e)
      })
      return true
    }

    if (thisInitiative.subInitiatives.length > 0) {
      var found = findCoordinate(thisInitiative.subInitiatives, id, thisCoord)
      if (found) {
        /* update input reference */
        coord.length = 0
        thisCoord.forEach((e) => {
          coord.push(e)
        })
        return true
      }
    }
  }
  return false
}

const getters = {
  initiativeCoordinate: (state, getters) => (id) => {
    var coord = []
    findCoordinate(state.initiativesTree, id, coord)
    return coord
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
  },
  triggerUpdateAssets: (state) => {
    state.triggerUpdateAssets = !state.triggerUpdateAssets
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
