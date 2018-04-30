import Vue from 'vue'

const pathsToTop = function (genealogy, currentPath, allPaths) {
  // https://softwareengineering.stackexchange.com/questions/307263/algorithm-to-get-all-paths-in-a-tree
  currentPath.push(genealogy.section)

  if (genealogy.parents.length === 0) {
    // save this path to the allPaths array
    allPaths.push(JSON.parse(JSON.stringify(currentPath)))
    currentPath = []
    return
  } else {
    for (var pIx in genealogy.parents) {
      var newPath = JSON.parse(JSON.stringify(currentPath))
      pathsToTop(genealogy.parents[pIx], newPath, allPaths)
    }
  }
}

const state = {
  currentSectionId: null,
  currentSectionGenealogy: null
}

const getters = {
  currentSectionPaths: (state) => {
    if (state.currentSectionGenealogy !== null) {
      var allPaths = []
      /* updates allPaths as reference */
      pathsToTop(state.currentSectionGenealogy, [], allPaths)

      /* remove currentSection and reverse all paths */
      for (var pIx in allPaths) {
        allPaths[pIx].shift()
        allPaths[pIx].reverse()
      }

      return allPaths
    }
    return []
  },
  currentSection: (state) => {
    if (state.currentSectionGenealogy !== null) {
      return state.currentSectionGenealogy.section
    }
    return null
  }
}

const mutations = {
  setCurrentSectionGenealogy: (state, payload) => {
    state.currentSectionGenealogy = payload
  }
}

const actions = {
  refreshCurrentSection: (context, sectionId) => {
    context.dispatch('updateCurrentSection', context.state.currentSectionGenealogy.section.id)
  },
  updateCurrentSection: (context, sectionId) => {
    context.commit('setCurrentSectionGenealogy', null)

    Vue.axios.get('/1/model/section/' + sectionId + '/genealogy').then((response) => {
      if (response.data.result === 'success') {
        context.commit('setCurrentSectionGenealogy', response.data.data)
        context.dispatch('expandSectionsInPaths', context.getters.currentSectionPaths)
      }
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
