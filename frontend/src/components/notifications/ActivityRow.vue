<template lang="html">
  <tr class="">
    <td class="avatar-col w3-center">
      <app-user-avatar :user="activity.triggerUser" :showName="false" :small="true"></app-user-avatar>
    </td>
    <td class="text-div">
      <span v-if="addContext">
        in <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="addTime">
         {{ getTimeStrSince(activity.timestamp) }} ago.
      </span>

      <span v-if="isInitiativeCreated" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created the new initiative
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="isInitiativeEdited" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> edited
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link> {{ initiativeChanged }}
      </span>

      <span v-if="isSubinitiativeCreated" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created
        <app-initiative-link :initiative="activity.subInitiative"></app-initiative-link> under
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="isInitiativeDeleted" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> deleted
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="isTokensMinted" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> minted
        <b>{{ activity.mint.value }} {{ activity.mint.tokenName }}</b> in
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="isNewPRAssigantionCreated" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created a new peer-reviewed
        <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="isNewDAssigantionCreated" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> made a direct
        <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link> to
        <app-user-link :user="activity.assignation.receivers[0].user"></app-user-link>
      </span>

      <span v-if="isNewPRAssigantionDone" class="">
        Peer-reviewed <app-assignation-link :assignation="activity.assignation"></app-assignation-link> has been done. Created by
        <app-user-link :user="activity.triggerUser"></app-user-link> for
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b> from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>.
      </span>

      <span v-if="isInitiativeTransfer" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> transferred
        <b>{{ activity.transfer.value }} {{ activity.transfer.assetName }}</b> from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link> to
        <app-initiative-link
          :initiativeId="activity.transfer.receiverId"
          :initiativeName="activity.transfer.receiverName">
        </app-initiative-link>
      </span>

      <span v-if="isAssignationRevertOrdered" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> ordered the revert of the
        <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b>
        with motive {{ activity.assignation.motive }}
        from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
      </span>

      <span v-if="isAssignationRevertCancelled" class="">
        Revert order cancelled.
        <app-user-link :user="activity.triggerUser"></app-user-link> ordered to revert the
        <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b>
        with motive {{ activity.assignation.motive }}
        from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        , but the revert was not accepted.
      </span>

      <span v-if="isAssignationReverted" class="">
        Revert order accepted.
        <app-user-link :user="activity.triggerUser"></app-user-link> ordered to revert the
        <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b>
        with motive {{ activity.assignation.motive }}
        from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,
        and the revert has been done.
      </span>

      <span v-if="isAssignationDeleted" class="">
        Ongoing transfer deleted. The
        <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
        <b>{{ activity.assignation.assets[0].value }} {{ activity.assignation.assets[0].assetName }}</b>
        with motive {{ activity.assignation.motive }}
        from
        <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,
        has been deleted.
      </span>

      <span v-if="isModelViewCreated" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created the view
        <app-model-view-link :view="activity.modelView"></app-model-view-link>.
      </span>
      <span v-if="isModelViewEdited" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> edited the view
        <app-model-view-link :view="activity.modelView"></app-model-view-link> title/description.
      </span>
      <span v-if="isModelViewDeleted" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> deleted the view
        <app-model-view-link :view="activity.modelView"></app-model-view-link>.
      </span>
      <span v-if="isModelSectionCreatedOnSection" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created the subsection
        <app-model-section-link :section="activity.modelSection"></app-model-section-link> under
        section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
      </span>
      <span v-if="isModelSectionCreatedOnView" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link> under
        view <app-model-view-link :view="activity.onView"></app-model-view-link>.
      </span>
      <span v-if="isModelSectionEdited" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> edited the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link> title/description.
      </span>
      <span v-if="isModelSectionRemovedFromSection" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> removed the subsection
        <app-model-section-link :section="activity.modelSection"></app-model-section-link>
        from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
      </span>
      <span v-if="isModelSectionRemovedFromView" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> removed the subsection
        <app-model-section-link :section="activity.modelSection"></app-model-section-link>
        from view <app-model-view-link :section="activity.fromView"></app-model-view-link>.
      </span>
      <span v-if="isModelSectionMovedInView" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link> in view
        <app-model-view-link :view="activity.onView"></app-model-view-link>.
      </span>
      <span v-if="isModelSectionMovedFromViewToSection" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link>
         from view <app-model-view-link :view="activity.fromView"></app-model-view-link>
         to section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
      </span>
      <span v-if="isModelSectionMovedFromSectionToSection" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link>
         from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>
         to section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
      </span>
      <span v-if="isModelSectionMovedFromSectionToView" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> moved the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link>
         from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>
         to view <app-model-view-link :view="activity.onView"></app-model-view-link>.
      </span>
      <span v-if="isModelCardWrapperAdded" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> added the card
        <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
         in section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
      </span>
      <span v-if="isModelCardWrapperRemoved" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> removed the card
        <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.fromSection"></app-model-card-link>
         from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
      </span>
      <span v-if="isModelSectionDeleted" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> deleted the section
        <app-model-section-link :section="activity.modelSection"></app-model-section-link>
      </span>
      <span v-if="isModelCardWrapperCreated" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> created the card
        <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
         on section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
      </span>
      <span v-if="isModelCardWrapperEdited" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> edited the card
        <app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link>.
      </span>
      <span v-if="isModelCardWrapperMovedSameSection" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> moved the card
        <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
        within section <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
      </span>
      <span v-if="isModelCardWrapperMovedDiferentSections" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> moved the card
        <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
        from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>
        to section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
      </span>
      <span v-if="isModelCardWrapperDeleted" class="">
        <app-user-link :user="activity.triggerUser"></app-user-link> deleted the card
        <app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link>.
      </span>

      <span v-if="isMessagePosted" class="">
        <vue-markdown class="marked-text" :source="activity.message.text"></vue-markdown>
      </span>
    </td>
  </tr>
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

import { getTimeStrSince } from '@/lib/common.js'

export default {
  props: {
    activity: {
      type: Object
    },
    addContext: {
      type: Boolean,
      default: true
    },
    addTime: {
      type: Boolean,
      default: true
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
      return this.activity.type === 'INITIATIVE_CREATED'
    },
    isInitiativeEdited () {
      return this.activity.type === 'INITIATIVE_EDITED'
    },
    isInitiativeDeleted () {
      return this.activity.type === 'INITIATIVE_DELETED'
    },
    isSubinitiativeCreated () {
      return this.activity.type === 'SUBINITIATIVE_CREATED'
    },
    isTokensMinted () {
      return this.activity.type === 'TOKENS_MINTED'
    },
    isNewPRAssigantionCreated () {
      return this.activity.type === 'PR_ASSIGNATION_CREATED'
    },
    isNewPRAssigantionDone () {
      return this.activity.type === 'PR_ASSIGNATION_DONE'
    },
    isNewDAssigantionCreated () {
      return this.activity.type === 'D_ASSIGNATION_CREATED'
    },
    isInitiativeTransfer () {
      return this.activity.type === 'INITIATIVE_TRANSFER'
    },
    isAssignationRevertOrdered () {
      return this.activity.type === 'ASSIGNATION_REVERT_ORDERED'
    },
    isAssignationRevertCancelled () {
      return this.activity.type === 'ASSIGNATION_REVERT_CANCELLED'
    },
    isAssignationReverted () {
      return this.activity.type === 'ASSIGNATION_REVERTED'
    },
    isAssignationDeleted () {
      return this.activity.type === 'ASSIGNATION_DELETED'
    },
    isModelViewCreated () {
      return this.activity.type === 'MODEL_VIEW_CREATED'
    },
    isModelViewEdited () {
      return this.activity.type === 'MODEL_VIEW_EDITED'
    },
    isModelViewDeleted () {
      return this.activity.type === 'MODEL_VIEW_DELETED'
    },
    isModelSectionCreatedOnSection () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onSection !== null
    },
    isModelSectionCreatedOnView () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onView !== null
    },
    isModelSectionEdited () {
      return this.activity.type === 'MODEL_SECTION_EDITED'
    },
    isModelSectionRemovedFromSection () {
      return this.activity.type === 'MODEL_SECTION_REMOVED' &&
        this.activity.fromSection !== null
    },
    isModelSectionRemovedFromView () {
      return this.activity.type === 'MODEL_SECTION_REMOVED' &&
        this.activity.fromView !== null
    },
    isModelSectionMovedInView () {
      return this.activity.type === 'MODEL_SECTION_MOVED' &&
        this.activity.fromView !== null &&
        this.activity.onView !== null
    },
    isModelSectionMovedFromViewToSection () {
      return this.activity.type === 'MODEL_SECTION_MOVED' &&
        this.activity.fromView !== null &&
        this.activity.onSection !== null
    },
    isModelSectionMovedFromSectionToSection () {
      return this.activity.type === 'MODEL_SECTION_MOVED' &&
        this.activity.fromSection !== null &&
        this.activity.onSection !== null
    },
    isModelSectionMovedFromSectionToView () {
      return this.activity.type === 'MODEL_SECTION_MOVED' &&
        this.activity.fromSection !== null &&
        this.activity.onView !== null
    },
    isModelNewSubsection () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onSection !== null
    },
    isModelNewSection () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onView !== null
    },
    isModelCardWrapperAdded () {
      return this.activity.type === 'MODEL_CARDWRAPPER_ADDED' &&
        this.activity.onSection !== null
    },
    isModelCardWrapperRemoved () {
      return this.activity.type === 'MODEL_CARDWRAPPER_REMOVED' &&
        this.activity.fromSection !== null
    },
    isModelSectionDeleted () {
      return this.activity.type === 'MODEL_SECTION_DELETED'
    },
    isModelCardWrapperCreated () {
      return this.activity.type === 'MODEL_CARDWRAPPER_CREATED' &&
        this.activity.onSection !== null
    },
    isModelCardWrapperEdited () {
      return this.activity.type === 'MODEL_CARDWRAPPER_EDITED'
    },
    isModelCardWrapperMovedSameSection () {
      if (this.activity.fromSection !== null && this.activity.onSection !== null) {
        return this.activity.type === 'MODEL_CARDWRAPPER_MOVED' &&
          this.activity.fromSection.id === this.activity.onSection.id
      }
      return false
    },
    isModelCardWrapperMovedDiferentSections () {
      if (this.activity.fromSection !== null && this.activity.onSection !== null) {
        return this.activity.type === 'MODEL_CARDWRAPPER_MOVED' &&
          this.activity.fromSection.id !== this.activity.onSection.id
      }
      return false
    },
    isModelCardWrapperDeleted () {
      return this.activity.type === 'MODEL_CARDWRAPPER_DELETED'
    },
    isMessagePosted () {
      return this.activity.type === 'MESSAGE_POSTED'
    },
    initiativeChanged () {
      var nameChanged = false
      var driverChanged = false

      if (this.activity.initiative.meta.name !== this.activity.oldName) {
        nameChanged = true
      }

      if (this.activity.initiative.meta.driver !== this.activity.oldDriver) {
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
  },
  methods: {
    getTimeStrSince (v) {
      return getTimeStrSince(v)
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
  width: 60px;
}

.text-div {
  font-size: 14px;
  text-align: left;
  padding-right: 10px;
}

</style>
