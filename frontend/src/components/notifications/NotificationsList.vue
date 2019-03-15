<template lang="html">
  <div class="notifications-container">

    <popper :append-to-body="true" trigger="click" :options="popperOptions" :toggleShow="toggleShow" class="">
      <div class="notifications-list-container w3-white w3-card-4 w3-bar-block noselect w3-topbar border-blue-app">

        <div class="close-div w3-xlarge cursor-pointer gray-1-color"
          @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-row w3-border-bottom notifications-header">
          <div class="w3-row">
           Your notifications under "{{ elementTitle }}"
          </div>
          <div class="w3-row-padding notif-control-row">
            <div class="w3-col s4"
              @click="showAll()">
              <button class="w3-button w3-round"
                :class="{'app-button': showingAll, 'app-button-light': !showingAll}">
                All
              </button>
            </div>
            <div class="w3-col s4"
              @click="showUnreadOnly()">
              <button class="w3-button w3-round"
                :class="{'app-button': !showingAll, 'app-button-light': showingAll}">
                {{ totalUnread }} unread
              </button>
            </div>
            <div class="w3-col s4"
              @click="allNotificationsRead()">
              <button class="w3-button w3-round app-button-green">
                mark as <br>read
              </button>
            </div>
          </div>
        </div>

        <div v-if="!loading" class="">
          <app-activity-table
            v-if="activities.length > 0"
            :activities="activities"
            :addInAppState="true"
            :fullWidthCard="true"
            :showCardsPreview="contextType === 'MODEL_SECTION' || contextType === 'INITIATIVE'">
          </app-activity-table>
          <div v-else class="w3-padding w3-center">
            <i>{{ $t('general.NO_RESULTS_FOUND') }}</i>
          </div>
        </div>
        <div v-else class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../assets/loading.gif" alt="">
        </div>

        <div class="w3-row w3-center">
          <button v-if="!allShown"
            @click="showMore()"
            class="w3-margin-top w3-margin-bottom w3-button app-button-light" type="button" name="button">
            show more...
          </button>
        </div>
      </div>

      <div slot="reference" class="icon-button cursor-pointer w3-display-container"
        @click="showNotificationsClicked()">
        <span v-if="totalUnread === 0">
          <i class="fa fa-circle-o circle-empty"></i>
        </span>
        <span v-else>
          <i v-if="onlyNotificationsUnder" class="fa fa-circle-o circle-o"></i>
          <i v-else class="fa fa-circle circle"></i>
        </span>
      </div>
    </popper>

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
    isSelected: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showTable: false,
      loading: false,
      notifications: [],
      totalUnread: 0,
      currentPage: 0,
      showingAll: true,
      showingMoreNotifications: false,
      allShown: false,
      toggleShow: false,
      subscription: null
    }
  },

  computed: {
    elementTitle () {
      switch (this.contextType) {
        case 'MODEL_CARD':
          return 'this card'

        case 'MODEL_SECTION':
          return this.element.title

        case 'INITIATIVE':
          return this.element.meta.name
      }

      return 'this element'
    },
    contextElementId () {
      return this.element.id
    },
    activities () {
      let activities = []
      this.notifications.forEach((notification) => {
        let activity = notification.activity
        activity.inAppState = notification.inAppState
        activities.push(activity)
      })
      return activities
    },
    url () {
      return '/1/notifications/' + this.contextType + '/' + this.contextElementId
    },
    triggerUpdateNotifications () {
      return this.$store.state.support.triggerUpdateNotifications
    },
    notificationsHere () {
      let notificationsHere = []

      switch (this.contextType) {
        case 'MODEL_CARD':
          for (let ix in this.notifications) {
            if (this.notifications[ix].activity.modelCardWrapper) {
              if (this.notifications[ix].activity.modelCardWrapper.id === this.contextElementId) {
                notificationsHere.push(this.notifications[ix])
              }
            }
          }
          break

        case 'MODEL_SECTION':
          for (let ix in this.notifications) {
            if (this.notifications[ix].activity.modelSection) {
              if (this.notifications[ix].activity.modelSection.id === this.contextElementId) {
                notificationsHere.push(this.notifications[ix])
              }
            }
          }
          break

        case 'INITIATIVE':
          for (let ix in this.notifications) {
            if (this.notifications[ix].activity.initiative) {
              if (this.notifications[ix].activity.initiative.id === this.contextElementId) {
                notificationsHere.push(this.notifications[ix])
              }
            }
          }
      }

      return notificationsHere
    },
    notificationsHereMessages () {
      return this.notificationsHere.filter((e) => { return e.activity.type === 'MESSAGE_POSTED' })
    },
    onlyNotificationsUnder () {
      if (this.notifications.length > 0) {
        return this.notificationsHere.length === 0
      }
      return false
    },
    popperOptions () {
      return {
        placement: 'right',
        modifiers: {
          preventOverflow: {
            enabled: true,
            boundariesElement: 'viewport'
          }
        }
      }
    }
  },

  watch: {
    triggerUpdateNotifications () {
      /* only update if there is notifications to be removed */
      if (this.notifications.length > 0) {
        // console.log('updating notifications in ' + this.url + ' due to triggerUpdateNotifications watch')
        this.updateNotifications()
      }
    },
    '$store.state.socket.connected' () {
      this.handleSocket()
    }
  },

  methods: {
    showAll () {
      this.showingAll = true
      this.updateNotifications()
    },
    showUnreadOnly () {
      this.showingAll = false
      this.updateNotifications()
    },
    updateNotifications (markMessagesRead) {
      markMessagesRead = markMessagesRead || false
      if (!this.showingMoreNotifications) {
        /* dont update if the user is scrolling down de notifications */
        /* delay the update to let other requests go before (like adding a card and so on) */
        setTimeout(() => {
          this.updateNotificationsDelayed(markMessagesRead)
        }, 5000)
      }
    },

    updateNotificationsDelayed (markMessagesRead) {
      this.loading = true
      this.axios.get(this.url, {
        params: {
          page: 0,
          size: 10,
          onlyUnread: !this.showingAll
        }
      }).then((response) => {
        this.loading = false
        /* check that new notifications arrived */
        this.notifications = response.data.data.notifications
        this.totalUnread = response.data.data.totalUnread

        this.allShown = this.notifications.length < 10

        if (markMessagesRead) {
          if (this.isSelected && this.$route.name === 'ModelSectionMessages') {
            /* autoread message notifications of this section */
            this.messageNotificationsRead()
          }
        }

        /* push all notifications */
        // console.log('pushing notifications ' + this.notifications.length + ' from ' + this.url)
        this.$store.dispatch('addPushNotifications', this.notifications)
      }).catch(function (error) {
        console.log(error)
      })
    },

    addNotifications () {
      this.showingMoreNotifications = true
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10,
          onlyUnread: false
        }
      }).then((response) => {
        /* check that new notifications arrived */
        if (response.data.data.length < 10) {
          this.allShown = true
        }

        this.notifications = this.notifications.concat(response.data.data.notifications)
        this.totalUnread = response.data.data.totalUnread
      }).catch(function (error) {
        console.log(error)
      })
    },

    allNotificationsRead () {
      this.axios.put(this.url + '/read', {}).then((response) => {
        /* check that new notifications arrived */
        this.toggleShow = !this.toggleShow
        this.$store.commit('triggerUpdateNotifications')
        // console.log('updating notifications in ' + this.url + ' due to allNotificationsRead response')
        this.updateNotifications()
        this.hide()
      }).catch(function (error) {
        console.log(error)
      })
    },

    messageNotificationsRead () {
      let idsList = this.notificationsHereMessages.map((e) => e.id)
      if (idsList.length > 0) {
        this.axios.put('/1/notifications/read', idsList).then((response) => {
          /* check that new notifications arrived */
          this.$store.commit('triggerUpdateNotifications')
          // console.log('updating notifications in ' + this.url + ' due to messageNotificationsRead response')
          this.updateNotifications(false)
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

    closeThis () {
      this.toggleShow = !this.toggleShow
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
          url = '/channel/activity/model/initiative/' + this.contextElementId
          break
      }

      this.subscription = this.$store.dispatch('subscribe', {
        url: url,
        onMessage: (tick) => {
          // console.log(tick)
          var message = tick.body
          if (message === 'UPDATE') {
            // console.log('updating notifications in ' + this.url + ' due to socket message')
            this.updateNotifications(true)
          }
        }
      })
    }
  },

  created () {
    // console.log('updating notifications in ' + this.url + ' due to component creation')
    this.updateNotifications(true)
    this.handleSocket()
  },

  beforeDestroy () {
    this.$store.dispatch('unsubscribe', this.subscription)
  }
}
</script>

<style scoped>

.close-div {
  position: absolute;
  top: 0px;
  right: 0px;
  width: 35px;
  height: 35px;
  transition:all 0.3s ease-out;
}

.close-div:hover {
  color: #15a5cc !important;
}

.notif-control-row button {
    width: 100%;
    height: 48px;
}

.notifications-header {
  padding: 12px 6px 6px 6px;
}

.icon-button {
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

.circle-empty {
  color: #cbcfd2;
  font-size: 10px;
}

.notifications-container {
}

.notifications-list-container {
  max-width: 400px;
  max-height: 60vh;
  overflow: auto;
  z-index: 20;
}

.compact .notifications-container {
  width: 292px;
  left: -32px;
}

</style>
