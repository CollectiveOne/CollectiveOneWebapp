import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'

const socket = new SockJS(process.env.VUE_APP_WEBSOCKET_SERVER_URL)
const stompClient = Stomp.over(socket, { debug: false })

const state = {
  connected: false
}

const getters = {}

const mutations = {
  setConnected: (state, payload) => {
    state.connected = payload
  }
}

const actions = {
  initializeWebsocket: (context) => {
    return new Promise((resolve, reject) => {
      stompClient.connect(
        {},
        frame => {
          console.log('succesfully connected SockJS to: ' + process.env.VUE_APP_WEBSOCKET_SERVER_URL)
          context.commit('setConnected', true)
          resolve(true)
        },
        error => {
          console.log('error conecting SockJS to: ' + process.env.VUE_APP_WEBSOCKET_SERVER_URL)
          console.log(error)
          context.commit('setConnected', false)
          reject(new Error('error conecting SockJS to: ' + process.env.VUE_APP_WEBSOCKET_SERVER_URL))
        }
      )
    })
  },

  subscribe: (context, payload) => {
    if (context.state.connected) {
      return stompClient.subscribe(payload.url, tick => {
        payload.onMessage(tick)
      })
    } else {
      return null
    }
  },

  unsubscribe: (context, payload) => {
    if (context.state.connected) {
      stompClient.unsubscribe(payload.id)
    } else {
      return null
    }
  },

  disconnectSocket: (context) => {
    if (stompClient) {
      stompClient.disconnect()
    }
    context.commit('setConnected', false)
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
