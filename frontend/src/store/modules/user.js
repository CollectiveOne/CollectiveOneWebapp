const state = {
  user: null
}

const getters = {
  isUserLogged: state => {
    return state.user != null
  }
}

const mutations = {
}

const actions = {
}

export default {
  state,
  getters,
  mutations,
  actions
}
