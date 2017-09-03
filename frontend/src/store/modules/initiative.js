import Vue from 'vue'

const state = {
  initiative: null,
  initiativeTransfers: null,
  initiativeAssignations: null,
  initiativeModelViews: null
}

const getters = {
  initiativeMembersUsers: (state) => () => {
    let members = state.initiative.initiativeMembers.members
    var memberUsers = []
    for (var ix in members) {
      memberUsers.push(members[ix].user)
    }
    return memberUsers
  },
  isLoggedAnAdmin: (state) => {
    if (state.initiative) {
      return state.initiative.loggedMember.role === 'ADMIN'
    } else {
      false
    }
  },
  isLoggedAMember: (state) => {
    if (state.initiative) {
      return state.initiative.loggedMember.role === 'MEMBER'
    } else {
      return false
    }
  },
  isLoggedAnEditor: (state) => {
    if (state.initiative) {
      return state.initiative.loggedMember.role === 'ADMIN' || state.initiative.loggedMember.role === 'EDITOR'
    } else {
      false
    }
  }
}

const mutations = {
  setInitiative: (state, payload) => {
    state.initiative = payload
  },
  setTransfers: (state, payload) => {
    state.initiativeTransfers = payload
  },
  setModelViews: (state, payload) => {
    state.initiativeModelViews = payload
  },
  setAssignations: (state, payload) => {
    state.initiativeAssignations = payload
  }
}

const actions = {

  updateInitiative: (context, id) => {
    /* show loading if new initiative */
    if (context.state.initiative) {
      if (context.state.initiative.id !== id) {
        context.commit('setInitiativeLoaded', false)
      }
    }

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
      context.commit('setInitiativeLoaded', true)
    }).catch((error) => {
      console.log(error)
    })
  },

  refreshInitiative: (context) => {
    if (context.state.initiative) {
      context.dispatch('updateInitiative', context.state.initiative.id)
    }
  },

  refreshTransfers: (context) => {
    if (context.state.initiative) {
      Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/transfersToInitiatives').then((response) => {
        context.commit('setTransfers', response.data.data)
      })

      Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/assignations').then((response) => {
        context.commit('setAssignations', response.data.data)
      })
    }
  },

  refreshModelViews: (context) => {
    if (context.state.initiative) {
      Vue.axios.get('/1/secured/initiative/' + context.state.initiative.id + '/model', {
        params: {
          level: 0
        }
      }).then((response) => {
        context.commit('setModelViews', response.data.data)
      })
    }
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
