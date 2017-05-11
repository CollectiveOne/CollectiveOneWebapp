import Vue from 'vue'
import Auth0Lock from 'auth0-lock'

const state = {
  lock: null,
  authenticated: false,
  profile: null
}

const getters = {
  loggedUserImage: state => {
    if (state.profile) {
      return state.profile.pictureUrl
    } else {
      return ''
    }
  },
  loggedUserNickname: state => {
    if (state.profile) {
      return state.profile.nickname
    } else {
      return ''
    }
  }
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
  }
}

const actions = {

  initUserAuthenticated: (context) => {
    var options = {
      auth: {
        params: {
          connectionScopes: {
            connectionName: [ 'openid', 'user_metadata', 'app_metadata', 'picture' ]
          }
        }
      }
    }
    var lock = new Auth0Lock(process.env.AUTH0_CLIENT_ID, process.env.AUTH0_DOMAIN, options)

    lock.on('authenticated', (authResult) => {
      localStorage.setItem('access_token', authResult.accessToken)
      localStorage.setItem('id_token', authResult.idToken)
      context.dispatch('updateAuthenticated')
    })

    lock.on('authorization_error', (error) => {
      console.log(error)
    })

    context.commit('setLock', lock)
    context.dispatch('updateAuthenticated')
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
      }).catch(function (error) {
        console.log(error)
      })
    }
  },

  logoutUser: (context) => {
    debugger
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
