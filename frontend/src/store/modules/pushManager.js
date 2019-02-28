import Vue from 'vue';
import router from '@/router'
import { i18n } from '@/lang/lang_init'
import { swRegistration } from '@/registerServiceWorker.js'
import { showNotification } from '@/lib/notifHelper.js'

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
      if (notification.pushState === 'PENDING' && notification.inAppState === 'PENDING') {
        if (!context.state.pushedIds.includes(notification.id)) {
          context.commit('addPushedId', notification.id)

          let ok = true
          /* dont show messages push notifications if current section
             is the section in which the message is written. */
          if (notification.activity.type === 'MESSAGE_POSTED' && context.rootState.support.windowIsFocus) {
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
            if (!context.rootState.support.windowIsFocus) {
              console.log('Creating Notification')
              showNotification(swRegistration, notification)

            } else {
              console.log('notifications not pushed because window is focused')
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
}

export default {
  state,
  getters,
  mutations,
  actions
}
