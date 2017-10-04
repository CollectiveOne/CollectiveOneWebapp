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
      <div class="w3-row" v-for="(notification, ix) in notifications" :key="notification.id">
        <app-notification-box :notification="notification"></app-notification-box>
        <hr v-if="ix < notifications.length - 1">
      </div>
      <div v-if="notifications.length === 0" class="w3-large">
        no notifications
      </div>
    </div>

  </div>
</template>

<script>
import NotificationBox from './NotificationBox.vue'

export default {

  components: {
    'app-notification-box': NotificationBox
  },

  props: {
    show: {
      type: Boolean
    }
  },

  data () {
    return {
    }
  },

  computed: {
    notifications () {
      return this.$store.state.user.notifications
    },
    numberOfUnreadNotifications () {
      return this.notifications.filter((e) => {
        return e.state === 'PENDING'
      }).length
    }
  },

  methods: {
    showNotificationsClicked () {
      this.$emit('icon-clicked')
      this.$store.dispatch('notificationsRead')
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
  padding-top: 20px !important;
  padding-bottom: 20px !important;
  max-height: calc(100vh - 80px);
  overflow-y: auto;
}

hr {
  margin-top: 10px;
  margin-bottom: 10px;
}

</style>
