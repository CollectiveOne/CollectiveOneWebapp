import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'

const socket = new SockJS(process.env.WEBSOCKET_SERVER_URL)
const stompClient = Stomp.over(socket)

const state = {
  sckConnected: false
}

const getters = {
  isConnected: (state) => {
    return state.connected
  }

}

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
          context.commit('setConnected', true)
          resolve()
        },
        error => {
          console.log(error)
          context.commit('setConnected', false)
          reject()
        }
      )
    })
  },

  subscribe: (context, payload) => {
    if (context.state.connected) {
      stompClient.subscribe(payload.url, tick => {
        payload.callback(tick)
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
