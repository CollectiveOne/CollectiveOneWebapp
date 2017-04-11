import Vue from 'vue'

const state = {
  activeProjects: [],
  allProjects: [],
  activeProject: ''
}

const getters = {
  activeProjects: state => {
    return state.activeProjects
  },

  allProjects: state => {
    return state.allProjects
  },

  activeProject: state => {
    return state.activeProject
  }
}

const mutations = {
  setActiveProjects: (state, activeProjects) => {
    state.activeProjects = activeProjects
  },

  setAllProjects: (state, allProjects) => {
    state.allProjects = allProjects
  },

  setActiveProject: (state, activeProject) => {
    state.activeProject = activeProject
  }
}

const actions = {
  updateProjects: (context) => {
    /* TODO: how to check the current state and dont update the project list if
    already populated */
    Vue.axios.get('/1/projects/getNamesEnabled').then((response) => {
      context.commit('setAllProjects', response.data.projectList)
      context.commit('setActiveProjects', response.data.projectList)
      context.commit('setActiveProject', response.data.projectList[0])
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
