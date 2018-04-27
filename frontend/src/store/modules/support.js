import Vue from 'vue'
import router from '@/router'
import { menuArrayToString, menuStringToArray } from '@/components/model/nav/expandCode.js'

const state = {
  contentAnimationType: 'slideToDown',
  myInitiativesTree: [],
  currentInitiativeTree: [],
  expandedSubsectionsTree: [],
  triggerCheckExpandSubsections: false,
  triggerUpdateAssets: false,
  triggerUpdateSectionsTree: false,
  triggerUpdateSectionCards: false,
  triggerUpdateNotifications: false,
  userEmailNotVerified: false,
  initiativeLoaded: false,
  expandNav: false,
  expandModelNav: false,
  windowIsSmall: false,
  draggingElement: null
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

const findThisExpands = function (expandsTree, coord) {
  let globalLevel = coord.length - 1

  /* transverse the sections expansion tree to find the expansion data
     of this coordinate */
  let thisExpands = expandsTree
  for (var level = 0; level < globalLevel; level++) {
    /* find the expansion data of this subsection */
    for (var ixS in thisExpands) {
      let thisExpand = thisExpands[ixS]
      if (thisExpand) {
        if (thisExpand[0] === coord[level]) {
          /* replace thisExpands with that section subelements */
          thisExpands = thisExpands[ixS][1]
        }
      }
    }
  }

  return thisExpands
}

const getters = {
  initiativesTree: (state, getters, rootState) => (id) => {
    return rootState.user.authenticated ? state.myInitiativesTree : state.currentInitiativeTree
  },
  initiativesTreeTop: (state, getters, rootState) => (id) => {
    if (!rootState.user.authenticated) {
      return []
    } else {
      var topInitiatives = []
      for (var ix in state.myInitiativesTree) {
        topInitiatives.push(state.myInitiativesTree[ix])
      }
      return topInitiatives
    }
  },
  initiativeCoordinate: (state, getters) => (id) => {
    var coord = []
    findCoordinate(getters.initiativesTree(), id, coord)
    return coord
  },
  colorOfInitiative: (state, getters) => (id) => {
    var initiative = findInitiative(getters.initiativesTree(), id)
    if (initiative) {
      return initiative.meta.color
    } else {
      return '#637484'
    }
  },
  isSectionExpanded: (state, getters) => (coord) => {
    let ixInParent = coord[coord.length - 1]
    let thisExpands = findThisExpands(state.expandedSubsectionsTree, coord)
    for (let ixFound in thisExpands) {
      if (thisExpands[ixFound][0] === ixInParent) {
        return true
      }
    }
    return false
  }
}

const mutations = {
  setExpandedSubsectionsTreeFromString: (state, payload) => {
    state.expandedSubsectionsTree = menuStringToArray(payload)
  },
  expandSection: (state, setting) => {
    let coord = setting.coord
    let value = setting.value
    let ixInParent = coord[coord.length - 1]
    let thisExpands = findThisExpands(state.expandedSubsectionsTree, coord)

    if (value) {
      /* append the element to the expandedSubsectionsTree in the correct position
         by reference */
      thisExpands.push([ixInParent, []])
    } else {
      for (let ixFound in thisExpands) {
        if (thisExpands[ixFound][0] === ixInParent) {
          thisExpands.splice(ixFound, 1)
        }
      }
    }

    let menuCoded = menuArrayToString(state.expandedSubsectionsTree)
    if (menuCoded !== this.menu) {
      router.replace({name: router.app.$route.name, query: {menu: menuCoded}})
    }
  },
  setContentAnimationType: (state, payload) => {
    state.contentAnimationType = payload
  },
  setSelectedInitiativeLevel: (state, payload) => {
    state.selectedInitiativeLevel = payload
  },
  selectedInitiativeLevelOld: (state, payload) => {
    state.selectedInitiativeLevelOld = payload
  },
  setMyInitiativesTree: (state, payload) => {
    state.myInitiativesTree = payload
  },
  setCurrentInitiativeTree: (state, payload) => {
    state.currentInitiativeTree = payload
  },
  triggerUpdateAssets: (state) => {
    state.triggerUpdateAssets = !state.triggerUpdateAssets
  },
  triggerUpdateSectionsTree: (state) => {
    state.triggerUpdateSectionsTree = !state.triggerUpdateSectionsTree
  },
  triggerUpdateSectionCards: (state) => {
    state.triggerUpdateSectionCards = !state.triggerUpdateSectionCards
  },
  triggerUpdateNotifications: (state) => {
    state.triggerUpdateNotifications = !state.triggerUpdateNotifications
  },
  setUserEmailNotVerified: (state, payload) => {
    state.userEmailNotVerified = payload
  },
  setInitiativeLoaded: (state, payload) => {
    state.initiativeLoaded = payload
  },
  setExpandNav: (state, payload) => {
    state.expandNav = payload
  },
  toggleExpandNav: (state) => {
    state.expandNav = !state.expandNav
  },
  toggleExpandModelNav: (state) => {
    state.expandModelNav = !state.expandModelNav
  },
  setWindowIsSmall: (state, payload) => {
    state.windowIsSmall = payload
  },
  setDraggingElement: (state, payload) => {
    state.draggingElement = payload
  }
}

const actions = {
  updateMyInitiatives: (context) => {
    Vue.axios.get('/1/initiatives/mines').then((response) => {
      context.commit('setMyInitiativesTree', response.data.data)
    })
  },
  updateCurrentInitiativeTree: (context, initiativeId) => {
    Vue.axios.get('/1/initiative/' + initiativeId, {
      params: {
        addSubinitiatives: true
      }
    }).then((response) => {
      context.commit('setCurrentInitiativeTree', [response.data.data])
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
