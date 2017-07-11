<template lang="html">
  <div class="w3-row w3-small">
    <div class="w3-col s3">
      <app-user-avatar :user="notification.activity.subInitiative.creator" :showName="false" :small="true"></app-user-avatar>
    </div>
    <div class="w3-col s9 text-div">
      <app-user-link :user="notification.activity.triggerUser"></app-user-link> created
      <app-initiative-link :initiative="notification.activity.subInitiative"></app-initiative-link> under
      <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
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
  }
}
</script>

<style scoped>

.text-div {
  padding-right: 5px;
}

</style>
