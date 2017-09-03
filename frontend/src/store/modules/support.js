import Vue from 'vue'

const state = {
  contentAnimationType: 'slideToDown',
  initiativesTree: [],
  triggerUpdateAssets: false,
  triggerUpdateModel: false,
  userEmailNotVerified: false,
  myInitiativesFirstLoaded: false,
  initiativeLoaded: false
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

const findInitiative = function (initiatives, id) {
  for (var ix in initiatives) {
    var thisInitiative = initiatives[ix]
    if (thisInitiative.id === id) {
      return thisInitiative
    }

    if (thisInitiative.subInitiatives.length > 0) {
      var foundInitiative = findInitiative(thisInitiative.subInitiatives, id)
      if (foundInitiative !== null) {
        return foundInitiative
      }
    }
  }
  return null
}

const getters = {
  initiativeCoordinate: (state, getters) => (id) => {
    var coord = []
    findCoordinate(state.initiativesTree, id, coord)
    return coord
  },
  colorOfInitiative: (state, getters) => (id) => {
    var initiative = findInitiative(state.initiativesTree, id)
    if (initiative) {
      return initiative.meta.color
    } else {
      return '#637484'
    }
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
  },
  triggerUpdateModel: (state) => {
    state.triggerUpdateModel = !state.triggerUpdateModel
  },
  setUserEmailNotVerified: (state, payload) => {
    state.userEmailNotVerified = payload
  },
  setMyInitiativesFirstLoaded: (state, payload) => {
    state.myInitiativesFirstLoaded = payload
  },
  setInitiativeLoaded: (state, payload) => {
    state.initiativeLoaded = payload
  }

}

const actions = {
  updatedMyInitiatives: (context) => {
    Vue.axios.get('/1/secured/initiatives/mines').then((response) => {
      context.commit('setInitiativesTree', response.data.data)
      context.commit('setMyInitiativesFirstLoaded', true)
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
