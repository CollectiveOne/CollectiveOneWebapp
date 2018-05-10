import Vue from 'vue'
import router from '@/router'

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
          context.commit('addPushedId', notification.id)

          let ok = true
          /* dont show messages push notifications if current section
             is the section in which the message is written. */
          if (notification.activity.type === 'MESSAGE_POSTED') {
            if (notification.activity.modelSection) {
              if (context.rootState.model.currentSectionGenealogy) {
                if (notification.activity.modelSection.id === context.rootState.model.currentSectionGenealogy.section.id) {
                  if (router.app.$route.name === 'ModelSectionMessages') {
                    ok = false
                  }
                }
              }
            }
          }

          if (ok) {
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
}

export default {
  state,
  getters,
  mutations,
  actions
}
