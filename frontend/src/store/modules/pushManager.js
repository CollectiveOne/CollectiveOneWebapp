import Vue from 'vue'

const state = {
  pushedIds: []
}

const getters = {
}

const mutations = {
  addPushedId: (state, payload) => {
    state.pushedIds.push(payload)
    /* limit size */
    if (state.pushedIds > 100) {
      state.pushedIds.shift()
    }
  }
}

const actions = {
  addPushNotifications: (context, notifications) => {
    for (var ix in notifications) {
      let notification = notifications[ix]
      if (notification.pushState === 'PENDING') {
        if (!context.state.pushedIds.includes(notification.id)) {
          console.log('pushing')
          console.log(notification)
          context.commit('addPushedId', notification.id)
          let user = notification.activity.triggerUser

          var notify = new Notification('CollectiveOne', {
            body: user.nickname + ' ' + notification.message,
            tag: notification.activity.id,
            icon: user.pictureUrl
          })

          notify.onshow = function () { setTimeout(notify.close.bind(notify), 5000) }
          notify.onclick = function (event) {
            event.preventDefault()
            window.open(notification.url, '_blank')
            notify.close()
          }

          Vue.axios.put('/1/notifications/pushed/' + notification.id, {}).then((response) => {
          }).catch(function (error) {
            console.log(error)
          })
        }
      }
    }
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
