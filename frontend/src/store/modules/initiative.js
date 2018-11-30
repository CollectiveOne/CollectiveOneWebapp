import Vue from 'vue'
import router from '@/router'

const state = {
  initiative: null,
  initiativeLoaded: false,
  accessDenied: false
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
  setInitiativeLoaded: (state, payload) => {
    state.initiativeLoaded = payload
  },
  setAccessDenied: (state, payload) => {
    state.accessDenied = payload
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
      if (response.data.result === 'success') {
        let initiative = response.data.data
        context.commit('setInitiative', initiative)
        context.commit('setAccessDenied', false)
        context.commit('setInitiativeLoaded', true)
        context.dispatch('resetSectionsTree', {
          baseSectionId: initiative.topModelSection.id,
          currentSectionId: router.app.$route.params.sectionId ? router.app.$route.params.sectionId : ''
        })
      } else {
        if (response.data.message === 'access denied') {
          context.commit('setAccessDenied', true)
          context.commit('setInitiativeLoaded', true)
        }
      }
    }).catch((error) => {
      console.log(error)
    })
  },

  refreshInitiative: (context) => {
    if (context.state.initiative) {
      context.dispatch('updateInitiative', context.state.initiative.id)
    }
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
