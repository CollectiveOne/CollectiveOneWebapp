const state = {
  user: null,
  isAuthenticated: false
}

const getters = {
  isUserLogged: state => {
    return state.isAuthenticated
  },
  usernameLogged: state => {
    if (state.user) {
      return state.user.username
    } else {
      return ''
    }
  }
}

const mutations = {
  setUserLogged: (state, userData) => {
    if (userData !== '') {
      state.isAuthenticated = true
      state.user = userData
    } else {
      state.isAuthenticated = false
      state.user = null
    }
  },

  logoutUser: (state, userData) => {
    state.isAuthenticated = false
    state.user = null
  }
}

const actions = {
  setUserLogged: ({ commit }, payload) => {
    commit('setUserLogged', payload)
  },

  logoutUser: ({ commit }) => {
    commit('logoutUser')
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
