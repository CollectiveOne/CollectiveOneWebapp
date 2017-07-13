import Vue from 'vue'

const state = {
  initiative: null
}

const getters = {
  initiativeMembersUsers: (state) => () => {
    let members = state.initiative.initiativeMembers.members
    var memberUsers = []
    for (var ix in members) {
      memberUsers.push(members[ix].user)
    }
    return memberUsers
  }
}

const mutations = {
  setInitiative: (state, payload) => {
    state.initiative = payload
  }
}

const actions = {

  updateInitiative: (context, id) => {
    Vue.axios.get('/1/secured/initiative/' + id, {
      params: {
        addAssets: true,
        addSubinitiatives: true,
        addParents: true,
        addMembers: true,
        addLoggedUser: true
      }
    }).then((response) => {
      context.commit('setInitiative', response.data.data)
    }).catch((error) => {
      console.log(error)
    })
  },

  refreshInitiative: (context) => {
    context.dispatch('updateInitiative', context.state.initiative.id)
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
