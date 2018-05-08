  const state = {
    data: []
  }
  const getters = {
      getData: (state) => (elementId) => {
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
    setData: (state, payload) => {
      var found = state.data.findIndex(res => {
        return res.elementId === payload.elementId
      })

      if (found < 0) {
        state.data.push(payload)
      } else {
        state.data[found] = payload
      }
    },

    clearData: (state, payload) => {
        var found = state.data.findIndex(res => {
          return res.elementId === payload
        })
        if (found >= 0) {
          state.data.splice(found, 1)
        }
      }
  }
  const actions = {
    doBackup: (context, payload) => {
      context.commit('setData', payload)
    },

    clear: (context, payload) => {
        context.commit('clearData', payload)
    }
  }
  export default {
    state,
    getters,
    mutations,
    actions
  }
