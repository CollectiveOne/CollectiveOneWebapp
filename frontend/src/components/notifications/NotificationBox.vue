<template lang="html">
  <div class="w3-row">
    <div class="w3-col s3">
      <app-user-avatar :user="notification.activity.triggerUser" :showName="false" :small="true"></app-user-avatar>
    </div>
    <div class="w3-col s9 text-div">
      <div v-if="isInitiativeCreated" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created the new initiative
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
      </div>

      <div v-if="isInitiativeEdited" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> edited
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link> {{ initiativeChanged }}
      </div>

      <div v-if="isSubinitiativeCreated" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created
        <app-initiative-link :initiative="notification.activity.subInitiative"></app-initiative-link> under
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
      </div>

      <div v-if="isTokensMinted" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> minted
        <b>{{ notification.activity.mint.value }} {{ notification.activity.mint.tokenName }}</b> in
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
      </div>

      <div v-if="isNewPRAssigantionCreated" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created a new peer-reviewed
        assignation of <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
      </div>

    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserLink from '@/components/elementLinks/UserLink.vue'
import InitiativeLink from '@/components/elementLinks/InitiativeLink.vue'

export default {
  props: {
    notification: {
      type: Object
    }
  },

  components: {
    'app-user-avatar': UserAvatar,
    'app-user-link': UserLink,
    'app-initiative-link': InitiativeLink
  },

  computed: {
    isInitiativeCreated () {
      return this.notification.activity.type === 'INITIATIVE_CREATED'
    },
    isInitiativeEdited () {
      return this.notification.activity.type === 'INITIATIVE_EDITED'
    },
    isSubinitiativeCreated () {
      return this.notification.activity.type === 'SUBINITIATIVE_CREATED'
    },
    isTokensMinted () {
      return this.notification.activity.type === 'TOKENS_MINTED'
    },
    isNewPRAssigantionCreated () {
      return this.notification.activity.type === 'PR_ASSIGNATION_CREATED'
    },
    initiativeChanged () {
      let activity = this.notification.activity
      var nameChanged = false
      var driverChanged = false

      if (activity.initiative.meta.name !== activity.oldName) {
        nameChanged = true
      }

      if (activity.initiative.meta.driver !== activity.oldDriver) {
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
  font-size: 14px;
  text-align: left;
  padding-right: 10px;
}

</style>
