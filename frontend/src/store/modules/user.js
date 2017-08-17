import Vue from 'vue'

const state = {
  lock: null,
  authenticated: false,
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
  setProfile: (state, payload) => {
    state.profile = payload
  },
  setNotifications: (state, payload) => {
    state.notifications = payload
  }
}

const actions = {

  updateAuthenticated: (context, payload) => {
    /* check local storage */
    context.commit('authenticate', !!localStorage.getItem('id_token'))

    /* if user is authenticated */
    if (context.state.authenticated) {
      /* read profile from C1 DB */
      context.dispatch('updateProfile')

      /* set auto-update ever 5 seconds */
      if (context.state.intervalId == null) {
        context.state.intervalId = setInterval(() => {
          /* update everything every 10 s */
          if (context.state.authenticated) {
            context.dispatch('updateNotifications')
            context.dispatch('updatedMyInitiatives')
            context.dispatch('refreshInitiative')
            context.dispatch('refreshTransfers')
            context.commit('triggerUpdateAssets')
          }
        }, 10000)
      }

      /* if user is not authenticated */
      // var state = payload.state
      // if (state !== '') {
      //   if (state.startsWith('/landing')) {
      //     /* user comes from landing and was authenticated, go to app then */
      //     window.location.href = '/#/app/inits'
      //   } else {
      //     /* user comes from a custom url */
      //     window.location.href = '/#' + state
      //   }
      // } else {
      //   window.location.href = '/#/app/inits'
      // }
    } else {
      /* if user is not authenticated */
      // window.location.href = '/#/landing'
    }
  },

  updateProfile: (context) => {
    /* user profile */
    if (context.state.authenticated) {
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
    window.location.href = '/'
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
