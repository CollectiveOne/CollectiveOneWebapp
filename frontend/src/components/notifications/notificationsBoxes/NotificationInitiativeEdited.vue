<template lang="html">
  <div class="w3-row w3-small">
    <div class="w3-col s3">
      <app-user-avatar :user="notification.activity.triggerUser" :showName="false" :small="true"></app-user-avatar>
    </div>
    <div class="w3-col s9 text-div">
      <app-user-link :user="notification.activity.triggerUser"></app-user-link> edited
      <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link> {{ changed }}
    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserLink from '@/components/elementLinks/UserLink.vue'
import InitiativeLink from '@/components/elementLinks/InitiativeLink.vue'

export default {

  components: {
    'app-user-avatar': UserAvatar,
    'app-user-link': UserLink,
    'app-initiative-link': InitiativeLink
  },

  props: {
    notification: {
      type: Object
    }
  },

  computed: {
    changed () {
      let activity = this.notification.activity
      var nameChanged = false
      var driverChanged = false

      if (activity.initiative.name !== activity.oldName) {
        nameChanged = true
      }

      if (activity.initiative.driver !== activity.oldDriver) {
        driverChanged = true
      }

      if (nameChanged && driverChanged) {
        return ' name and driver.'
      }

      if (nameChanged) {
        return ' name.'
      }

      if (driverChanged) {
        return ' driver.'
      }

      return ''
    }
  }
}
</script>

<style scoped>

.text-div {
  padding-right: 5px;
}

</style>
