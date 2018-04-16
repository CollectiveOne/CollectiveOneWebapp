<template lang="html">
  <div class="">

    <button class="bell-button w3-button w3-padding-large w3-display-container w3-xlarge" title="Notifications"
      @click="showNotificationsClicked()">

      <i class="fa fa-bell-o w3-display-center"></i>
      <i v-if="numberOfUnreadNotifications > 0" class="fa fa-circle w3-display-topright circle">
        <span class="circle-text w3-display-middle">{{ numberOfUnreadNotifications }}</span>
      </i>
    </button>

    <div v-show="showTable"
      v-click-outside="clickOutsideNotifications"
      class="notifications-container w3-white w3-card-4 w3-bar-block w3-center">
      <app-activity-table :activities="activities"></app-activity-table>
      <button v-if="!allShown"
        @click="showMore()"
        class="w3-margin-top w3-margin-bottom w3-button app-button-light" type="button" name="button">show more...</button>
    </div>

  </div>
</template>

<script>
import ActivityTable from './ActivityTable.vue'

export default {

  components: {
    'app-activity-table': ActivityTable
  },

  data () {
    return {
      showTable: false,
      preventClickOutside: true,
      notifications: [],
      currentPage: 0,
      showingMoreNotifications: false,
      allShown: false
    }
  },

  computed: {
    activities () {
      return this.notifications.map(function (n) { return n.activity })
    },
    numberOfUnreadNotifications () {
      return this.notifications.filter((e) => {
        return e.state === 'PENDING'
      }).length
    }
  },

  watch: {
    '$store.state.user.triggerUpdateNotifications' () {
      this.updateNotifications()
    }
  },

  methods: {
    pushDesktopNotification (pushMessage, pushIcon) {
      var notify = new Notification('CollectiveOne (' + this.numberOfUnreadNotifications + ')', {
        body: pushMessage,
        icon: pushIcon
      })
      setTimeout(notify.close.bind(notify), 5000)
    },
    updateNotifications () {
      if (!this.showingMoreNotifications) {
        /* dont update if the user is scrolling down de notifications */
        this.axios.get('/1/user/notifications', {
          params: {
            page: 0,
            size: 10
          }
        }).then((response) => {
          /* check that new notifications arrived */
          this.allShown = false
          this.notifications = response.data.data
        }).then((response) => {
          /* push desktop notification */
          for (var i = 0; i < this.notifications.length; i++) {
            if (this.notifications[i].pushState === 'PENDING') {
              this.pushDesktopNotification(this.notifications[i].pushMessage, this.notifications[i].activity.triggerUser.pictureUrl)
            }
          }
        }).then((response) => {
          /* update notification state */
          this.notificationsPushed()
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    addNotifications () {
      this.showingMoreNotifications = true
      this.axios.get('/1/user/notifications', {
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

    notificationsRead () {
      /* notifications read */
      if (this.$store.state.user.profile) {
        this.axios.put('/1/user/notifications/read', {}).then((response) => {
          this.updateNotifications()
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    notificationsPushed () {
      /* notifications notified */
      if (this.$store.state.user.profile) {
        this.axios.put('/1/user/notifications/pushed', {}).then((response) => {
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
      if (!this.showingMoreNotifications) {
        this.notificationsRead()
      }
    },
    show () {
      this.showTable = true
      this.updateNotifications()
    }
  }
}
</script>

<style scoped>

.bell-button {
  height: 64px;
}

.fa-bell-o {
  transform: rotate(30deg);
}

.circle {
  margin-top: 8px;
  margin-right: 10px;
  color: rgb(249, 48, 48);
  font-size: 20px;
}

.circle-text {
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  color: white;
}

.number {
  background-color: rgb(101, 172, 255);
  font-weight: bold;
}

.notifications-container {
  width:420px;
  position: absolute;
  margin-left: -212px;
  max-height: calc(100vh - 80px);
  overflow-y: auto;
}

hr {
  margin-top: 10px;
  margin-bottom: 10px;
}

</style>
