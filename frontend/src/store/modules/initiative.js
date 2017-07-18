import Vue from 'vue'

const state = {
  initiative: null,
  initiativeTransfers: null,
  initiativeAssignations: null
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
  },
  setTransfers: (state, payload) => {
    state.initiativeTransfers = payload
  },
  setAssignations: (state, payload) => {
    state.initiativeAssignations = payload
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
  },

  refreshTransfers: (context) => {
    Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/transfersToInitiatives').then((response) => {
      context.commit('setTransfers', response.data.data)
    })

    Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/assignations').then((response) => {
      context.commit('setAssignations', response.data.data)
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}