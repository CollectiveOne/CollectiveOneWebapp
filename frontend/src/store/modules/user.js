import Vue from 'vue'

const state = {
  lock: null,
  authenticated: false,
  auth0state: '',
  profile: null,
  notifications: [],
  intervalId: null
}

const getters = {
}

const mutations = {
  setLock: (state, payload) => {
    state.lock = payload
  },
  authenticate: (state, payload) => {
    state.authenticated = payload
  },
  setAuth0state: (state, payload) => {
    state.auth0state = payload
  },
  setProfile: (state, payload) => {
    state.profile = payload
  },
  setNotifications: (state, payload) => {
    state.notifications = payload
  }
}

const actions = {

  updateProfile: (context) => {
    /* user profile */
    if (context.state.authenticated) {
      /* set auto-update ever 5 seconds */
      if (context.state.intervalId == null) {
        context.state.intervalId = setInterval(() => {
          /* update everything every 10 s */
          if (context.state.authenticated) {
            // context.dispatch('updateNotifications')
            // context.dispatch('updatedMyInitiatives')
            // context.dispatch('refreshInitiative')
            // context.dispatch('refreshTransfers')
            // context.commit('triggerUpdateAssets')
          }
        }, 10000)
      }

      Vue.axios.get('/1/secured/user/myProfile').then((response) => {
        context.commit('setProfile', response.data.data)
        context.dispatch('updateNotifications')
      }).catch(function () {
        context.commit('setUserEmailNotVerified', true)
      })
    }
  },

  updateNotifications: (context) => {
    /* get notifications */
    if (context.state.authenticated) {
      if (context.state.profile) {
        Vue.axios.get('/1/secured/user/notifications').then((response) => {
          context.commit('setNotifications', response.data.data)
        }).catch(function (error) {
          console.log(error)
        })
      }
    }
  },

  notificationsRead: (context) => {
    /* notifications read */
    if (context.state.profile) {
      Vue.axios.put('/1/secured/user/notifications/read', {}).then((response) => {
      }).catch(function (error) {
        console.log(error)
      })
    }
  },

  logoutUser: (context) => {
    localStorage.removeItem('access_token')
    localStorage.removeItem('id_token')
    context.commit('authenticate', false)
    context.commit('setProfile', null)
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
