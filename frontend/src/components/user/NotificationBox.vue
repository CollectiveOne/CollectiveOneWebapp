<template lang="html">
  <component :is="notificationComponent" :notification="notification" class="d2-color"></component>
</template>

<script>
import NotificationSubinitiativeCreated from './NotificationSubinitiativeCreated.vue'
export default {
  props: {
    notification: {
      type: Object
    }
  },

  components: {
    'app-notification-subinitiative-created': NotificationSubinitiativeCreated
  },

  computed: {
    notificationComponent () {
      switch (this.notification.activity.type) {
        case 'SUBINITIATIVE_CREATED':
          return 'app-notification-subinitiative-created'
      }
    }
  }
}
</script>

<style scoped>
</style>
