<template lang="html">
  <div class="w3-row">
    <div class="w3-col s3 w3-center avatar-col">
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

      <div v-if="isInitiativeDeleted" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> deleted
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
        Peer-reviewed <app-assignation-link :assignation="notification.activity.assignation"></app-assignation-link> has been done. Created by
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> for
        <b>{{ notification.activity.assignation.assets[0].value }} {{ notification.activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="notification.activity.initiative"></app-initiative-link>.
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

      <div v-if="isModelViewCreated" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created the view
        <app-model-view-link :view="notification.activity.modelView"></app-model-view-link>.
      </div>
      <div v-if="isModelViewEdited" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> edited the view
        <app-model-view-link :view="notification.activity.modelView"></app-model-view-link> title/description.
      </div>
      <div v-if="isModelViewDeleted" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> deleted the view
        <app-model-view-link :view="notification.activity.modelView"></app-model-view-link>.
      </div>
      <div v-if="isModelSectionCreatedOnSection" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created the subsection
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link> under
        section <app-model-section-link :section="notification.activity.onSection"></app-model-section-link>.
      </div>
      <div v-if="isModelSectionCreatedOnView" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link> under
        view <app-model-view-link :view="notification.activity.onView"></app-model-view-link>.
      </div>
      <div v-if="isModelSectionEdited" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> edited the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link> title/description.
      </div>
      <div v-if="isModelSectionRemovedFromSection" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> removed the subsection
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link>
        from section <app-model-section-link :section="notification.activity.fromSection"></app-model-section-link>.
      </div>
      <div v-if="isModelSectionRemovedFromView" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> removed the subsection
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link>
        from view <app-model-view-link :section="notification.activity.fromView"></app-model-view-link>.
      </div>
      <div v-if="isModelSectionMovedInView" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link> in view
        <app-model-view-link :view="notification.activity.onView"></app-model-view-link>.
      </div>
      <div v-if="isModelSectionMovedFromViewToSection" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link>
         from view <app-model-view-link :view="notification.activity.fromView"></app-model-view-link>
         to section <app-model-section-link :section="notification.activity.onSection"></app-model-section-link>.
      </div>
      <div v-if="isModelSectionMovedFromSectionToSection" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link>
         from section <app-model-section-link :section="notification.activity.fromSection"></app-model-section-link>
         to section <app-model-section-link :section="notification.activity.onSection"></app-model-section-link>.
      </div>
      <div v-if="isModelSectionMovedFromSectionToView" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link>
         from section <app-model-section-link :section="notification.activity.fromSection"></app-model-section-link>
         to view <app-model-view-link :view="notification.activity.onView"></app-model-view-link>.
      </div>
      <div v-if="isModelCardWrapperAdded" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> added the card
        <app-model-card-link :cardWrapper="notification.activity.modelCardWrapper" :onSection="notification.activity.onSection"></app-model-card-link>
         in section <app-model-section-link :section="notification.activity.onSection"></app-model-section-link>.
      </div>
      <div v-if="isModelCardWrapperRemoved" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> removed the card
        <app-model-card-link :cardWrapper="notification.activity.modelCardWrapper" :onSection="notification.activity.fromSection"></app-model-card-link>
         from section <app-model-section-link :section="notification.activity.fromSection"></app-model-section-link>.
      </div>
      <div v-if="isModelSectionDeleted" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> deleted the section
        <app-model-section-link :section="notification.activity.modelSection"></app-model-section-link>
      </div>
      <div v-if="isModelCardWrapperCreated" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> created the card
        <app-model-card-link :cardWrapper="notification.activity.modelCardWrapper" :onSection="notification.activity.onSection"></app-model-card-link>
         on section <app-model-section-link :section="notification.activity.onSection"></app-model-section-link>.
      </div>
      <div v-if="isModelCardWrapperEdited" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> edited the card
        <app-model-card-alone-link :cardWrapper="notification.activity.modelCardWrapper"></app-model-card-alone-link>.
      </div>
      <div v-if="isModelCardWrapperMovedSameSection" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> moved the card
        <app-model-card-link :cardWrapper="notification.activity.modelCardWrapper" :onSection="notification.activity.onSection"></app-model-card-link>
        within section <app-model-section-link :section="notification.activity.fromSection"></app-model-section-link>.
      </div>
      <div v-if="isModelCardWrapperMovedDiferentSections" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> moved the card
        <app-model-card-link :cardWrapper="notification.activity.modelCardWrapper" :onSection="notification.activity.onSection"></app-model-card-link>
        from section <app-model-section-link :section="notification.activity.fromSection"></app-model-section-link>
        to section <app-model-section-link :section="notification.activity.onSection"></app-model-section-link>.
      </div>
      <div v-if="isModelCardWrapperDeleted" class="">
        <app-user-link :user="notification.activity.triggerUser"></app-user-link> deleted the card
        <app-model-card-alone-link :cardWrapper="notification.activity.modelCardWrapper"></app-model-card-alone-link>.
      </div>

    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserLink from '@/components/global/UserLink.vue'
import InitiativeLink from '@/components/global/InitiativeLink.vue'
import AssignationLink from '@/components/global/AssignationLink.vue'

import ModelViewLink from '@/components/global/ModelViewLink.vue'
import ModelSectionLink from '@/components/global/ModelSectionLink.vue'
import ModelCardInSectionLink from '@/components/global/ModelCardInSectionLink.vue'
import ModelCardAloneLink from '@/components/global/ModelCardAloneLink.vue'

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
    'app-assignation-link': AssignationLink,
    'app-model-view-link': ModelViewLink,
    'app-model-section-link': ModelSectionLink,
    'app-model-card-link': ModelCardInSectionLink,
    'app-model-card-alone-link': ModelCardAloneLink
  },

  computed: {
    isInitiativeCreated () {
      return this.notification.activity.type === 'INITIATIVE_CREATED'
    },
    isInitiativeEdited () {
      return this.notification.activity.type === 'INITIATIVE_EDITED'
    },
    isInitiativeDeleted () {
      return this.notification.activity.type === 'INITIATIVE_DELETED'
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
    isModelViewCreated () {
      return this.notification.activity.type === 'MODEL_VIEW_CREATED'
    },
    isModelViewEdited () {
      return this.notification.activity.type === 'MODEL_VIEW_EDITED'
    },
    isModelViewDeleted () {
      return this.notification.activity.type === 'MODEL_VIEW_DELETED'
    },
    isModelSectionCreatedOnSection () {
      return this.notification.activity.type === 'MODEL_SECTION_CREATED' &&
        this.notification.activity.onSection !== null
    },
    isModelSectionCreatedOnView () {
      return this.notification.activity.type === 'MODEL_SECTION_CREATED' &&
        this.notification.activity.onView !== null
    },
    isModelSectionEdited () {
      return this.notification.activity.type === 'MODEL_SECTION_EDITED'
    },
    isModelSectionRemovedFromSection () {
      return this.notification.activity.type === 'MODEL_SECTION_REMOVED' &&
        this.notification.activity.fromSection !== null
    },
    isModelSectionRemovedFromView () {
      return this.notification.activity.type === 'MODEL_SECTION_REMOVED' &&
        this.notification.activity.fromView !== null
    },
    isModelSectionMovedInView () {
      return this.notification.activity.type === 'MODEL_SECTION_MOVED' &&
        this.notification.activity.fromView !== null &&
        this.notification.activity.onView !== null
    },
    isModelSectionMovedFromViewToSection () {
      return this.notification.activity.type === 'MODEL_SECTION_MOVED' &&
        this.notification.activity.fromView !== null &&
        this.notification.activity.onSection !== null
    },
    isModelSectionMovedFromSectionToSection () {
      return this.notification.activity.type === 'MODEL_SECTION_MOVED' &&
        this.notification.activity.fromSection !== null &&
        this.notification.activity.onSection !== null
    },
    isModelSectionMovedFromSectionToView () {
      return this.notification.activity.type === 'MODEL_SECTION_MOVED' &&
        this.notification.activity.fromSection !== null &&
        this.notification.activity.onView !== null
    },
    isModelNewSubsection () {
      return this.notification.activity.type === 'MODEL_SECTION_CREATED' &&
        this.notification.activity.onSection !== null
    },
    isModelNewSection () {
      return this.notification.activity.type === 'MODEL_SECTION_CREATED' &&
        this.notification.activity.onView !== null
    },
    isModelCardWrapperAdded () {
      return this.notification.activity.type === 'MODEL_CARDWRAPPER_ADDED' &&
        this.notification.activity.onSection !== null
    },
    isModelCardWrapperRemoved () {
      return this.notification.activity.type === 'MODEL_CARDWRAPPER_REMOVED' &&
        this.notification.activity.fromSection !== null
    },
    isModelSectionDeleted () {
      return this.notification.activity.type === 'MODEL_SECTION_DELETED'
    },
    isModelCardWrapperCreated () {
      return this.notification.activity.type === 'MODEL_CARDWRAPPER_CREATED' &&
        this.notification.activity.onSection !== null
    },
    isModelCardWrapperEdited () {
      return this.notification.activity.type === 'MODEL_CARDWRAPPER_EDITED'
    },
    isModelCardWrapperMovedSameSection () {
      if (this.notification.activity.fromSection !== null && this.notification.activity.onSection !== null) {
        return this.notification.activity.type === 'MODEL_CARDWRAPPER_MOVED' &&
          this.notification.activity.fromSection.id === this.notification.activity.onSection.id
      }
      return false
    },
    isModelCardWrapperMovedDiferentSections () {
      if (this.notification.activity.fromSection !== null && this.notification.activity.onSection !== null) {
        return this.notification.activity.type === 'MODEL_CARDWRAPPER_MOVED' &&
          this.notification.activity.fromSection.id !== this.notification.activity.onSection.id
      }
      return false
    },
    isModelCardWrapperDeleted () {
      return this.notification.activity.type === 'MODEL_CARDWRAPPER_DELETED'
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

a {
  text-decoration: underline !important;
}

.avatar-col {
  padding-top: 3px;
}

.text-div {
  font-size: 14px;
  text-align: left;
  padding-right: 10px;
}

</style>
