import Vue from 'vue'
import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'

const socket = new SockJS(process.env.WEBSOCKET_SERVER_URL)
const stompClient = Stomp.over(socket)

const state = {
  socket: null,
  sckConnected: false,
  stompClient:null
}

const getters = {
  isConnected: (state) => {
    return state.connected;
  },

  getSocket: (state) => {
    return state.socket;
  },

  getStompClient: (state) => {
    return state.stompClient;
  }
}

const mutations = {
  setSocket: (state, payload) => {
    state.socket = payload
  },

  setConnected: (state, payload) => {
    state.connected = payload;
  },

  setStompClient: (state, payload) => {
    state.stompClient = payload;
  }
}

const actions = {
  initializeWebsocket: (context, payload) => {
    var socket = new SockJS(process.env.WEBSOCKET_SERVER_URL)
    var stompClient = Stomp.over(this.socket)
    stompClient.connect(
      {},
      frame => {
        context.commit('setConnected', true)
      },
      error => {
        console.log(error)
        context.commit('setConnected', false)
      }
    );
  },

  subscribe: (context, payload) => {
    stompClient.subscribe(payload.url, payload.callback)
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
