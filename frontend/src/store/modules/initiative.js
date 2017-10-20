import Vue from 'vue'

const state = {
  initiative: null,
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
      if (state.initiative.loggedMember) {
        return state.initiative.loggedMember.role === 'ADMIN'
      } else {
        return false
      }
    } else {
      return false
    }
  },
  isLoggedAnEditor: (state) => {
    if (state.initiative) {
      if (state.initiative.loggedMember) {
        return state.initiative.loggedMember.role === 'ADMIN' || state.initiative.loggedMember.role === 'EDITOR'
      } else {
        return false
      }
    } else {
      return false
    }
  },
  isLoggedAMember: (state) => {
    if (state.initiative) {
      if (state.initiative.loggedMember) {
        return state.initiative.loggedMember.role === 'ADMIN' || state.initiative.loggedMember.role === 'EDITOR' || state.initiative.loggedMember.role === 'MEMBER'
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
  setModelViews: (state, payload) => {
    state.initiativeModelViews = payload
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

  refreshModelViews: (context, pars) => {
    if (context.state.initiative) {
      Vue.axios.get('/1/initiative/' + context.state.initiative.id + '/model', {
        params: {
          level: 0
        }
      }).then((response) => {
        context.commit('setModelViews', response.data.data)
        if (pars.redirect) {
          if (context.state.initiativeModelViews.views.length > 0) {
            /* redirect to the first view by default */
            pars.router.replace({ name: 'ModelView', params: { viewId: context.state.initiativeModelViews.views[0].id } })
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
