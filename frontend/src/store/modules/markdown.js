  const state = {
    data: []
  }
  const getters = {
      getMarkdownBackupData: (state) => (elementId) => {
          var result = state.data.filter(res => {
              return elementId === res.elementId
          })

          if (result.length > 0) {
              return result[0].value
          } else {
              return null
          }
      }
  }
  const mutations = {
    setMarkdownBackupData: (state, payload) => {
      var found = state.data.findIndex(res => {
        return res.elementId === payload.elementId
      })

      if (found < 0) {
        state.data.push(payload)
      } else {
        state.data[found] = payload
      }
    },

    clearMarkdownBackup: (state, payload) => {
        var found = state.data.findIndex(res => {
          return res.elementId === payload
        })
        if (found >= 0) {
          state.data.splice(found, 1)
        }
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
