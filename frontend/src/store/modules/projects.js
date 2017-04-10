import Vue from 'vue'

const state = {
  followingProjects: [],
  allProjects: []
}

const getters = {
  followingProjects: state => {
    return state.followingProjects
  },

  allProjects: state => {
    return state.allProjects
  },

  activeProject: state => {
    return state.allProjects[0]
  }
}

const mutations = {
  setFollowingProjects: (state, followingProjets) => {
    state.followingProjets = followingProjets
  },

  setAllProjects: (state, allProjets) => {
    state.allProjects = allProjets
  }
}

const actions = {
  updateProjects: (context) => {
    /* TODO: how to check the current state and dont update the project list if
    already populated */
    Vue.axios.get('/1/projects/getNamesEnabled').then((response) => {
      context.commit('setAllProjects', response.data.projectList)
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
