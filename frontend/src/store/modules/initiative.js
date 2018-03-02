import Vue from 'vue'

const state = {
  initiative: null,
  initiativeModelSections: null
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
      if (state.initiative.loggedMembership[0]) {
        return state.initiative.loggedMembership[0].role === 'ADMIN'
      } else {
        return false
      }
    } else {
      return false
    }
  },
  isLoggedAParentAdmin: (state) => {
    if (state.initiative) {
      if (state.initiative.loggedMembership[1]) {
        return state.initiative.loggedMembership[1].role === 'ADMIN'
      } else {
        return false
      }
    } else {
      return false
    }
  },
  isLoggedAnEditor: (state) => {
    if (state.initiative) {
      if (state.initiative.loggedMembership[0]) {
        return state.initiative.loggedMembership[0].role === 'ADMIN' || state.initiative.loggedMembership[0].role === 'EDITOR'
      } else {
        return false
      }
    } else {
      return false
    }
  },
  isLoggedAMember: (state) => {
    if (state.initiative) {
      if (state.initiative.loggedMembership[0]) {
        return state.initiative.loggedMembership[0].role === 'ADMIN' || state.initiative.loggedMembership[0].role === 'EDITOR' || state.initiative.loggedMembership[0].role === 'MEMBER'
      } else {
        return false
      }
    } else {
      return false
    }
  }
}

const mutations = {
  setInitiative: (state, payload) => {
    state.initiative = payload
  },
  setModelSections: (state, payload) => {
    state.initiativeModelSections = payload
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

    Vue.axios.get('/1/initiative/' + id, {
      params: {
        addAssetsIds: true,
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

  refreshModelSections: (context, pars) => {
    if (context.state.initiative) {
      Vue.axios.get('/1/initiative/' + context.state.initiative.id + '/model', {
        params: {
          level: 0
        }
      }).then((response) => {
        context.commit('setModelSections', response.data.data)
        if (pars) {
          if (pars.redirect) {
            if (context.state.initiativeModelSections.length > 0) {
              /* redirect to the first section by default */
              pars.router.replace({ name: 'ModelSection', params: { viewId: context.state.initiativeModelSections[0].id } })
            }
          }
        }
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
