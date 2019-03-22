import Vue from 'vue'
import { swRegistration } from '@/registerServiceWorker.js'

const state = {
  lock: null,
  authenticated: false,
  auth0state: '',
  profile: null,
  triggerUpdateNotifications: false,
  intervalId: null,
  intervalId2: null
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
  triggerUpdateNotifications: (state) => {
    state.triggerUpdateNotifications = !state.triggerUpdateNotifications
  }
}

const actions = {

  updateProfile: (context) => {
    /* user profile */
    if (context.state.authenticated) {
      Vue.axios.get('/1/user/myProfile').then((response) => {
        if (response.data.result === 'success') {
          context.commit('setProfile', response.data.data)
          context.commit('triggerUpdateNotifications')
        } else {
          if (response.data.message === 'anonymous user') {
            context.commit('authenticate', false)
            context.commit('setProfile', null)
          }
        }
      }).catch((error) => {
        console.log(error)
        context.dispatch('logoutUser')
      })
    }
  },

  updateSubscription: (context) => {
    console.log('updating subscription')
    if (swRegistration) {
      console.log('sw ready')
      swRegistration.pushManager.getSubscription().then((subscription) => {
        if (subscription != null) {
          console.log('subscription found')
          let subscriptionDto = {
            endpoint: subscription.endpoint,
            p256dh: btoa(String.fromCharCode.apply(null, new Uint8Array(subscription.getKey('p256dh')))),
            auth: btoa(String.fromCharCode.apply(null, new Uint8Array(subscription.getKey('auth'))))
          }
          Vue.axios.put('/1/user/' + context.state.profile.c1Id + '/subscription', subscriptionDto)
        } else {
          console.log('subscription not found')
        }
      }).catch((error) => {
        console.log('error getting subscription')
        console.log(error)
      })
    } else {
      console.log('SW not ready for subscription update')
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
