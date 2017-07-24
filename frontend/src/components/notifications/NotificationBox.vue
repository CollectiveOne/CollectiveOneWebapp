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
        <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> of
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
      </div>

      <div v-if="isNewDAssigantionCreated" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> made a direct
        <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> of
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link> to
        <app-user-link :user="notification.activity.assignation.receivers[0].user"></app-user-link>
      </div>

      <div v-if="isNewPRAssigantionDone" class="">
        Peer-reviewed <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> created by
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> for
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link> has been closed
      </div>

      <div v-if="isInitiativeTransfer" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> transferred
        <b>{{ notification.activity.transfer.value }} {{ notification.activity.transfer.assetName }}</b> from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link> to
        <app-initiative-link
          :initiativeId="notification.activity.transfer.receiverId"
          :initiativeName="notification.activity.transfer.receiverName">
        </app-initiative-link>
      </div>

      <div v-if="isAssignationRevertOrdered" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> ordered the revert of the
        <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> of
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b>
        with motive {{ notification.activity.assignation.motive }}
        from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
      </div>

      <div v-if="isAssignationRevertCancelled" class="">
        Revert order cancelled.
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> ordered to revert the
        <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> of
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b>
        with motive {{ notification.activity.assignation.motive }}
        from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>
        , but the revert was not accepted.
      </div>

      <div v-if="isAssignationReverted" class="">
        Revert order accepted.
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> ordered to revert the
        <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> of
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b>
        with motive {{ notification.activity.assignation.motive }}
        from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>,
        and the revert has been done.
      </div>

      <div v-if="isAssignationDeleted" class="">
        Ongoing transfer deleted. The
        <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> of
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b>
        with motive {{ notification.activity.assignation.motive }}
        from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>,
        has been deleted.
      </div>

    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserLink from '@/components/global/UserLink.vue'
import InitiativeLink from '@/components/global/InitiativeLink.vue'
import AssignationLink from '@/components/global/AssignationLink.vue'

export default {
  props: {
    notification: {
      type: Object
    }
  },

  components: {
    'app-user-avatar': UserAvatar,
    'app-user-link': UserLink,
    'app-initiative-link': InitiativeLink,
    'app-assignation-link': AssignationLink
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
    isNewPRAssigantionDone () {
      return this.notification.activity.type === 'PR_ASSIGNATION_DONE'
    },
    isNewDAssigantionCreated () {
      return this.notification.activity.type === 'D_ASSIGNATION_CREATED'
    },
    isInitiativeTransfer () {
      return this.notification.activity.type === 'INITIATIVE_TRANSFER'
    },
    isAssignationRevertOrdered () {
      return this.notification.activity.type === 'ASSIGNATION_REVERT_ORDERED'
    },
    isAssignationRevertCancelled () {
      return this.notification.activity.type === 'ASSIGNATION_REVERT_CANCELLED'
    },
    isAssignationReverted () {
      return this.notification.activity.type === 'ASSIGNATION_REVERTED'
    },
    isAssignationDeleted () {
      return this.notification.activity.type === 'ASSIGNATION_DELETED'
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
