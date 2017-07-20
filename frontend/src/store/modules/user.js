import Vue from 'vue'
import Auth0Lock from 'auth0-lock'

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

  initUserAuthenticated: (context, redirectPath) => {
    var options = {
      auth: {
        state: redirectPath,
        responseType: 'token',
        params: {
          connectionScopes: {
            connectionName: [ 'openid', 'user_metadata', 'app_metadata', 'picture' ]
          }
        }
      },
      theme: {
        logo: 'https://image.ibb.co/d5abxv/Logo_Dark_Auth0.png',
        primaryColor: '#15a5cc'
      },
      languageDictionary: {
        title: 'welcome'
      }
    }

    var lock = new Auth0Lock(process.env.AUTH0_CLIENT_ID, process.env.AUTH0_DOMAIN, options)

    lock.on('authenticated', (authResult) => {
      localStorage.setItem('access_token', authResult.accessToken)
      localStorage.setItem('id_token', authResult.idToken)
      context.dispatch('updateAuthenticated')
      if (authResult.state.startsWith('/inits')) {
        window.location.href = '/#' + authResult.state
      } else {
        window.location.href = '/'
      }
    })

    lock.on('authorization_error', (error) => {
      console.log(error)
    })

    context.commit('setLock', lock)
    context.dispatch('updateAuthenticated')

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
  },

  updateAuthenticated: (context) => {
    context.commit('authenticate', !!localStorage.getItem('id_token'))
    context.dispatch('updateProfile')
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
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
