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
    }
  }
  const actions = {
    doBackup: (context, payload) => {
      context.commit('setData', payload)
    }
  }
  export default {
    state,
    getters,
    mutations,
    actions
  }
