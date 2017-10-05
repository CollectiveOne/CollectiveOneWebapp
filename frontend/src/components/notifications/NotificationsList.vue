<template lang="html">
  <div class="">
    <button class="bell-button w3-button w3-padding-large w3-display-container w3-xlarge" title="Notifications"
      @click="showNotificationsClicked()">
      <i class="fa fa-bell-o w3-display-center"></i>
      <i v-if="numberOfUnreadNotifications > 0" class="fa fa-circle w3-display-topright circle">
        <span class="circle-text w3-display-middle">{{ numberOfUnreadNotifications }}</span>
      </i>
    </button>

    <div v-if="show" class="notifications-container w3-white w3-card-4 w3-bar-block" style="width:300px">
      <app-activity-table :activities="activities"></app-activity-table>
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
    show: {
      type: Boolean
    }
  },

  data () {
    return {
      notifications: []
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
    updateNotifications () {
      /* get notifications */
      if (this.$store.state.user.authenticated) {
        if (this.$store.state.user.profile) {
          this.axios.get('/1/user/notifications').then((response) => {
            this.notifications = response.data.data
          }).catch(function (error) {
            console.log(error)
          })
        }
      }
    },

    notificationsRead () {
      /* notifications read */
      if (this.$store.state.user.profile) {
        this.axios.put('/1/user/notifications/read', {}).then((response) => {
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    showNotificationsClicked () {
      this.$emit('icon-clicked')
      this.notificationsRead()
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
