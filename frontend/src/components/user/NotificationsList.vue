<template lang="html">
  <div class="">
    <button class="w3-button w3-padding-large" title="Notifications" @click="showNotificationsClicked()">
      <i class="fa fa-bell"></i>
      <span class="w3-badge w3-right w3-small w3-green">{{ numberOfUnreadNotifications }}</span>
    </button>
    <div v-if="showNotifications" class="w3-dropdown-content w3-card-4 w3-bar-block" style="width:300px">
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {

  data () {
    return {
      showNotifications: false
    }
  },

  computed: {
    notifications () {
      return this.$store.state.user.notifications
    },
    numberOfUnreadNotifications () {
      return this.notifications.length
    }
  },

  methods: {
    ...mapActions('notificationsRead'),

    showNotificationsClicked () {
      this.showNotifications = true
      this.notificationsRead()
    }
  }
}
</script>

<style lang="css">
</style>
