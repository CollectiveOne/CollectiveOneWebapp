  const state = {
    data: new Map()
  }
  const getters = { }
  const mutations = {
    setMarkdownBackupData: (state, payload) => {
      console.log('setting id: ' + payload.elementId + ' - value: ' + payload.value)
      state.data.set(payload.elementId, payload.value)
    },

    clearMarkdownBackup: (state, payload) => {
        if (state.data.has(payload)) {
          console.log('deleting id: ' + payload)
          state.data.delete(payload)
        }
      }
  }
  const actions = {
  }
  export default {
    state,
    getters,
    mutations,
    actions
  }
