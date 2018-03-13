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
  currentSection: null,
  currentSectionGenealogy: null
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
  setCurrentSection: (state, payload) => {
    state.currentSection = payload
  },
  setCurrentSectionGenealogy: (state, payload) => {
    state.currentSectionGenealogy = payload
  }
}

const actions = {
  updateCurrentSection: (context, section) => {
    context.commit('setCurrentSectionGenealogy', null)
    context.commit('setCurrentSection', section)

    Vue.axios.get('/1/model/section/' + section.id + '/genealogy').then((response) => {
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
