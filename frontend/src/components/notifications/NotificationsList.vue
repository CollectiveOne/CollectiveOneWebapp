<template lang="html">
  <div :class="{'compact':isMainNav}">

    <div v-if="notifications.length > 0" class="bell-button cursor-pointer w3-display-container"
      @click="showNotificationsClicked()">
      <i v-if="onlyNotificationsUnder" class="fa fa-circle-o circle-o"></i>
      <i v-else class="fa fa-circle circle"></i>
    </div>

    <div v-show="showTable"
      v-click-outside="clickOutsideNotifications"
      class="notifications-container w3-white w3-card-4 w3-bar-block">
      <div class="w3-row-padding w3-border-bottom">
        <div class="w3-col text-div" :class="isMainNav?'s7':'s8'">
          {{ notifications.length }} new events under {{ element.title }}
        </div>
        <button class="w3-col  w3-margin-top w3-margin-bottom w3-button app-button" :class="isMainNav?'s5':'s4'"
          @click="notificationsRead()">
          mark as read
        </button>
      </div>

      <app-activity-table :activities="activities"></app-activity-table>

      <div class="w3-row w3-center">
        <button v-if="!allShown"
          id="T_showMoreButton"
          @click="showMore()"
          class="w3-margin-top w3-margin-bottom w3-button app-button-light" type="button" name="button">
          show more...
        </button>
      </div>
    </div>

  </div>
</template>

<script>
import ActivityTable from './ActivityTable.vue'

export default {

  components: {
    'app-activity-table': ActivityTable
  },

  props: {
    element: {
      type: Object,
      default: null
    },
    contextType: {
      type: String,
      default: 'MODEL_SECTION'
    },
    isMainNav: {
      type: Boolean,
      default: false
    },
    isSelected: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showTable: false,
      notifications: [],
      currentPage: 0,
      showingMoreNotifications: false,
      allShown: false
    }
  },

  computed: {
    contextElementId () {
      return this.element.id
    },
    activities () {
      return this.notifications.map(function (n) { return n.activity })
    },
    url () {
      return '/1/notifications/' + this.contextType + '/' + this.contextElementId
    },
    triggerUpdateNotifications () {
      return this.$store.state.support.triggerUpdateNotifications
    },
    notificationsHere () {
      let notificationsHere = []
      for (let ix in this.notifications) {
        if (this.notifications[ix].activity.modelSection) {
          if (this.notifications[ix].activity.modelSection.id === this.contextElementId) {
            notificationsHere.push(this.notifications[ix])
          }
        }
      }
      return notificationsHere
    },
    onlyNotificationsUnder () {
      if (this.notifications.length > 0) {
        return this.notificationsHere.length === 0
      }
      return false
    }
  },

  watch: {
    triggerUpdateNotifications () {
      /* only update if there is notifications to be removed */
      if (this.notifications.length > 0) {
        this.updateNotifications()
      }
    },
    '$store.state.socket.connected' () {
      this.handleSocket()
    }
  },

  methods: {
    updateNotifications () {
      if (!this.showingMoreNotifications) {
        /* dont update if the user is scrolling down de notifications */
        this.axios.get(this.url, {
          params: {
            page: 0,
            size: 10
          }
        }).then((response) => {
          /* check that new notifications arrived */
          this.notifications = response.data.data
          this.allShown = this.notifications.length < 10

          this.$store.dispatch('addPushNotifications', this.notifications)
          if (this.isSelected) {
            /* autoread notifications of this section */
            this.messageNotificationsRead(this.notificationsHere)
          }
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    addNotifications () {
      this.showingMoreNotifications = true
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10
        }
      }).then((response) => {
        /* check that new notifications arrived */
        if (response.data.data.length < 10) {
          this.allShown = true
        }

        this.notifications = this.notifications.concat(response.data.data)
      }).catch(function (error) {
        console.log(error)
      })
    },

    allNotificationsRead () {
      this.axios.put(this.url + '/read', {}).then((response) => {
          /* check that new notifications arrived */
          this.$store.commit('triggerUpdateNotifications')
          this.updateNotifications()
          this.hide()
        }).catch(function (error) {
          console.log(error)
        })
    },

    messageNotificationsRead (notificationListIn) {
      let notificationsList = notificationListIn || []
      let messagesList = notificationsList.filter((e) => { return e.activity.type === 'MESSAGE_POSTED' })
      let idsList = messagesList.map((e) => e.id)
      if (idsList.length > 0) {
        this.axios.put('/1/notifications/read', idsList).then((response) => {
          /* check that new notifications arrived */
          this.$store.commit('triggerUpdateNotifications')
          this.updateNotifications()
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    showMore () {
      this.currentPage += 1
      this.addNotifications()
    },

    clickOutsideNotifications () {
      // console.log('clickOutsideNotifications')
      if (!this.preventClickOutside) {
        if (this.showTable) {
          this.hide()
        }
      }
    },
    showNotificationsClicked () {
      if (!this.showTable) {
        this.show()
        this.preventClickOutside = true
        setTimeout(() => {
          this.preventClickOutside = false
        }, 500)
      } else {
        this.hide()
      }
    },
    hide () {
      this.showTable = false
      this.showingMoreNotifications = false
    },
    show () {
      this.showTable = true
    },
    handleSocket () {
      let url = ''
      switch (this.contextType) {
        case 'MODEL_CARD':
          url = '/channel/activity/model/card/' + this.contextElementId
          break

        case 'MODEL_SECTION':
          url = '/channel/activity/model/section/' + this.contextElementId
          break

        case 'INITIATIVE':
          url = '/channel/activity/model/initaitive/' + this.contextElementId
          break
      }

      this.subscription = this.$store.dispatch('subscribe', {
        url: url,
        onMessage: (tick) => {
          var message = tick.body
          if (message === 'UPDATE') {
            this.updateNotifications()
          }
        }
      })
    }
  },

  created () {
    this.updateNotifications()
    this.handleSocket()
  },

  beforeDestroy () {
    this.$store.dispatch('unsubscribe', this.subscription)
  }
}
</script>

<style scoped>

.text-div {
  padding: 16px 12px;
  text-align: right;
}

.bell-button {
  width: 30px;
  text-align: center;
}

.circle {
  color: #b91414;
  font-size: 10px;
}

.circle-o {
  color: #b35454;
  font-size: 10px;
}

.notifications-container {
  width:420px;
  position: absolute;
  margin-left: -212px;
  max-height: calc(100vh - 80px);
  overflow-y: auto;
  z-index: 2;
}

.compact .notifications-container {
  width: 292px;
  left: -32px;
}

</style>
