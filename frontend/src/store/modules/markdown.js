  const state = {
    data: new Map()
  }
  const getters = { }
  const mutations = {
    setMarkdownBackupData: (state, payload) => {
      state.data.set(payload.elementId, payload.value)
    },

    clearMarkdownBackup: (state, payload) => {
        state.data.clear()
      }
  }
  const actions = {
    doMarkdownBackup: (context, payload) => {
      context.commit('setMarkdownBackupData', payload)
    },

    clearMarkdownBackupData: (context, payload) => {
        context.commit('clearMarkdownBackup', payload)
    }
  }
  export default {
    state,
    getters,
    mutations,
    actions
  }
