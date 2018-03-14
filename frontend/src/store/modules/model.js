import Vue from 'vue'

const pathToTop = function (genealogy, parents) {
  parents.push(genealogy.section)
  if (genealogy.parents.length > 0) {
    /* only one path to top */
    parents = pathToTop(genealogy.parents[0], parents)
  }

  return parents
}

const state = {
  currentSectionId: null,
  currentSectionGenealogy: null,
  level: 1
}

const getters = {
  currentSectionPath: (state) => {
    if (state.currentSectionGenealogy !== null) {
      return pathToTop(state.currentSectionGenealogy, [])
    }
    return []
  }
}

const mutations = {
  setCurrentSectionGenealogy: (state, payload) => {
    state.currentSectionGenealogy = payload
  },
  levelDown: (state) => {
    state.level = state.level > 1 ? state.level - 1 : 1
  },
  levelUp: (state) => {
    state.level = state.level + 1
  }
}

const actions = {
  updateCurrentSection: (context, sectionId) => {
    context.commit('setCurrentSectionGenealogy', null)

    Vue.axios.get('/1/model/section/' + sectionId + '/genealogy').then((response) => {
      if (response.data.result === 'success') {
        context.commit('setCurrentSectionGenealogy', response.data.data)
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
