import router from '@/router'
import { menuArrayToString, menuStringToArray } from '@/components/model/nav/expandCode.js'

const state = {
  sectionsTree: []
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
