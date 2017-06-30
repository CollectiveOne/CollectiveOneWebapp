<template lang="html">
  <component :is="notificationComponent" :notification="notification" class="d2-color"></component>
</template>

<script>
import NotificationSubinitiativeCreated from './notifications/NotificationSubinitiativeCreated.vue'
import NotificationInitiativeEdited from './notifications/NotificationInitiativeEdited.vue'

export default {
  props: {
    notification: {
      type: Object
    }
  },

  components: {
    'app-notification-subinitiative-created': NotificationSubinitiativeCreated,
    'app-notification-initiative-edited': NotificationInitiativeEdited
  },

  computed: {
    notificationComponent () {
      switch (this.notification.activity.type) {
        case 'SUBINITIATIVE_CREATED':
          return 'app-notification-subinitiative-created'
        case 'INITIATIVE_EDITED':
          return 'app-notification-initiative-edited'
      }
    }
  }
}
</script>

<style scoped>
</style>
