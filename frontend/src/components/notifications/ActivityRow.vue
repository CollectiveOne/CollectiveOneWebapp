<template lang="html">
  <tr class="">
    <td class="avatar-col w3-center">
      <app-user-avatar :user="activity.triggerUser" :showName="false" :small="true"></app-user-avatar>
    </td>
    <td class="text-div wrap-long cursor-pointer w3-display-container"
      @mouseover="hovering = true"
      @mouseleave="hovering = false"
      @click="clicked = !clicked">

      <div class="top-line w3-small">
        <span v-if="addContext">
          in <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>
        <b><app-user-link :user="activity.triggerUser"></app-user-link></b>
        <span v-if="addTime">
           , {{ getTimeStrSince(activity.timestamp) }} ago.
        </span>
      </div>

      <div class="" :class="{'event-summary': !isMessagePosted && showMessages}">
        <span v-if="isInitiativeCreated" class="">
          created the new initiative <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>

        <span v-if="isInitiativeEdited" class="">
          edited <app-initiative-link :initiative="activity.initiative"></app-initiative-link> {{ initiativeChanged }}
        </span>

        <span v-if="isSubinitiativeCreated" class="">
          created <app-initiative-link :initiative="activity.subInitiative"></app-initiative-link> under
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>

        <span v-if="isInitiativeDeleted" class="">
          deleted <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>

        <span v-if="isTokenCreated" class="">
          created a new token type called <b>{{ activity.mint.tokenName }}</b> in
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,
          and minted <b>{{ activity.mint.value.toFixed(2) }}</b> units.
        </span>

        <span v-if="isTokensMinted" class="">
          minted <b>{{ activity.mint.value.toFixed(2) }} {{ activity.mint.tokenName }}</b> in
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>

        <span v-if="isNewPRAssigantionCreated" class="">
          created a new peer-reviewed <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b> from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>

        <span v-if="isNewDAssigantionCreated" class="">
          made a direct <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b> from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link> to
          <span v-for="(receiver, ix) in activity.assignation.receivers">{{ ix > 0 ? ', ' : '' }}<app-user-link :user="receiver.user"></app-user-link></span>
        </span>

        <span v-if="isNewPRAssigantionDone" class="">
          Peer-reviewed <app-assignation-link :assignation="activity.assignation"></app-assignation-link> has been done. Created by
          <app-user-link :user="activity.triggerUser"></app-user-link> for
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b> from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>.
        </span>

        <span v-if="isInitiativeTransfer" class="">
          transferred <b>{{ activity.transfer.value.toFixed(2) }} {{ activity.transfer.assetName }}</b> from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link> to
          <app-initiative-link
            :initiativeId="activity.transfer.receiverId"
            :initiativeName="activity.transfer.receiverName">
          </app-initiative-link>
        </span>

        <span v-if="isAssignationRevertOrdered" class="">
          ordered the revert of the <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b>
          with motive {{ activity.assignation.motive }}
          from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
        </span>

        <span v-if="isAssignationRevertCancelled" class="">
          Revert order cancelled. Order to revert the
          <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b>
          with motive {{ activity.assignation.motive }}
          from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
          , but the revert was not accepted.
        </span>

        <span v-if="isAssignationReverted" class="">
          Revert order accepted. Order to revert the
          <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b>
          with motive {{ activity.assignation.motive }}
          from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,
          and the revert has been done.
        </span>

        <span v-if="isAssignationDeleted" class="">
          Ongoing transfer deleted. The
          <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
          <b>{{ activity.assignation.assets[0].value.toFixed(2) }} {{ activity.assignation.assets[0].assetName }}</b>
          with motive {{ activity.assignation.motive }}
          from
          <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,
          has been deleted.
        </span>

        <span v-if="isModelSectionCreatedOnSection" class="">
          created the subsection <app-model-section-link :section="activity.modelSection"></app-model-section-link> under
          section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
        </span>
        <span v-if="isModelSectionCreatedOnInitiative" class="">
          created the section <app-model-section-link :section="activity.modelSection"></app-model-section-link> under
          initiative <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,.
        </span>
        <span v-if="isModelSectionEdited" class="">
          edited the section <app-model-section-link :section="activity.modelSection"></app-model-section-link> title/description.
        </span>
        <span v-if="isModelSectionRemovedFromSection" class="">
          removed the subsection <app-model-section-link :section="activity.modelSection"></app-model-section-link>
          from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
        </span>
        <span v-if="isModelSectionMovedFromSectionToSection" class="">
          moved the section <app-model-section-link :section="activity.modelSection"></app-model-section-link>
           from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>
           to section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
        </span>
        <span v-if="isModelSectionMoved" class="">
          moved the section <app-model-section-link :section="activity.modelSection"></app-model-section-link>.
        </span>
        <span v-if="isModelCardWrapperAdded" class="">
          added the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
           in section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
        </span>
        <span v-if="isModelCardWrapperRemoved" class="">
          removed the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.fromSection"></app-model-card-link>
           from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
        </span>
        <span v-if="isModelSectionDeleted" class="">
          deleted the section <app-model-section-link :section="activity.modelSection"></app-model-section-link>
        </span>
        <span v-if="isModelCardWrapperCreated" class="">
          created the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
           on section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
        </span>
        <span v-if="isModelCardWrapperMadePersonal" class="">
          made the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
           on section <app-model-section-link :section="activity.onSection"></app-model-section-link> visible.
        </span>
        <span v-if="isModelCardWrapperMadeCommon" class="">
          made the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
           on section <app-model-section-link :section="activity.onSection"></app-model-section-link> common.
        </span>
        <span v-if="isModelCardWrapperEdited" class="">
          edited the card <app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link>.
        </span>
        <span v-if="isModelCardWrapperMovedSameSection" class="">
          moved the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
          within section <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
        </span>
        <span v-if="isModelCardWrapperMovedDiferentSections" class="">
          moved the card <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
          from section <app-model-section-link :section="activity.fromSection"></app-model-section-link>
          to section <app-model-section-link :section="activity.onSection"></app-model-section-link>.
        </span>
        <span v-if="isModelCardWrapperDeleted" class="">
          deleted the card <app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link>.
        </span>

        <span v-if="isMessagePosted && (!showMessages || isExternalMessage)" :class="{'event-summary': isExternalMessage}">
          commented in
          <span v-if="isMessageInCardWrapper"><app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link> card.</span>
          <span v-if="isMessageInCardWrapperOnSection"><app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link></span>
          <span v-if="isMessageInSection"><app-model-section-link :section="activity.modelSection"></app-model-section-link> section.</span>
          <span v-if="isMessageInInitiative"><app-initiative-link :initiative="activity.initiative"></app-initiative-link> initiative.</span>
        </span>
        <span v-if="isMessagePosted && showMessages" class="">
          <div>
            <vue-markdown class="marked-text message-container" :source="activity.message.text"></vue-markdown>
          </div>
        </span>
      </div>

      <div class="control-btns-row w3-display-topright" v-if="isMessagePosted && showMessages">
        <transition name="fadeenter">
          <div v-if="hovering">
            <div v-if="authorIsLoggedUser" @click="$emit('edit-message', activity.message)"
              class="w3-button light-grey">
              <i class="fa fa-pencil"></i> edit
            </div>
            <div v-if="true" @click="$emit('reply-to-message', activity)"
              class="w3-button light-grey">
              <i class="fa fa-reply"></i> reply
            </div>
          </div>
        </transition>
      </div>

    </td>
  </tr>
</template>

<script>
import UserLink from '@/components/global/UserLink.vue'
import InitiativeLink from '@/components/global/InitiativeLink.vue'
import AssignationLink from '@/components/global/AssignationLink.vue'

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
    },
    showMessages: {
      type: Boolean,
      default: false
    },
    contextElementId: {
      type: String,
      default: ''
    }
  },

  components: {
    'app-user-link': UserLink,
    'app-initiative-link': InitiativeLink,
    'app-assignation-link': AssignationLink,
    'app-model-section-link': ModelSectionLink,
    'app-model-card-link': ModelCardInSectionLink,
    'app-model-card-alone-link': ModelCardAloneLink
  },

  data () {
    return {
      hovering: false,
      clicked: false
    }
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
    isTokenCreated () {
      return this.activity.type === 'TOKEN_CREATED'
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
    isModelSectionCreatedOnSection () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onSection !== null
    },
    isModelSectionCreatedOnInitiative () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onSection === null
    },
    isModelSectionEdited () {
      return this.activity.type === 'MODEL_SECTION_EDITED'
    },
    isModelSectionRemovedFromSection () {
      return this.activity.type === 'MODEL_SECTION_REMOVED' &&
        this.activity.fromSection !== null
    },
    isModelSectionMoved () {
      return this.activity.type === 'MODEL_SECTION_MOVED' &&
        (this.activity.fromSection === null ||
        this.activity.onSection === null)
    },
    isModelSectionMovedFromSectionToSection () {
      return this.activity.type === 'MODEL_SECTION_MOVED' &&
        this.activity.fromSection !== null &&
        this.activity.onSection !== null
    },
    isModelNewSubsection () {
      return this.activity.type === 'MODEL_SECTION_CREATED' &&
        this.activity.onSection !== null
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
    isModelCardWrapperMadePersonal () {
      return this.activity.type === 'MODEL_CARDWRAPPER_MADE_PERSONAL'
    },
    isModelCardWrapperMadeCommon () {
      return this.activity.type === 'MODEL_CARDWRAPPER_MADE_COMMON'
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
    isMessageInCardWrapper () {
      return this.isMessagePosted && (this.activity.modelCardWrapper !== null) && (this.activity.onSection === null)
    },
    isMessageInCardWrapperOnSection () {
      return this.isMessagePosted && (this.activity.modelCardWrapper !== null) && (this.activity.onSection !== null)
    },
    isMessageInSection () {
      return this.isMessagePosted && (this.activity.modelSection !== null)
    },
    isMessageInInitiative () {
      return this.isMessagePosted && (!this.isMessageInSection && !this.isMessageInCardWrapperOnSection && !this.isMessageInCardWrapper)
    },
    isExternalMessage () {
      if (!this.isMessagePosted || !this.showMessages) {
        return false
      } else {
        if (this.isMessageInCardWrapper || this.isMessageInCardWrapperOnSection) {
          return this.activity.modelCardWrapper.id !== this.contextElementId
        } else if (this.isMessageInSection) {
          return this.activity.modelSection.id !== this.contextElementId
        } else if (this.isMessageInInitiative) {
          return this.activity.initiative.id !== this.contextElementId
        }
      }
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
    },
    authorIsLoggedUser () {
      if (this.isMessagePosted) {
        if (this.$store.state.user.profile) {
          return this.$store.state.user.profile.c1Id === this.activity.message.author.c1Id
        }
      }
      return false
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
  padding-top: 6px;
  vertical-align: top !important;
  width: 60px;
}

.text-div {
  font-size: 14px;
  text-align: left;
  padding-right: 10px;
  padding-top: 3px;
}

.edit-btn {
  margin-right: 4px;
}

.event-summary {
  background-color: #eff3f6;
  margin-top: 6px;
  border-radius: 6px;
  padding: 3px 6px;
  font-style: italic;
}

.control-btns-row .w3-button {
  padding: 1px 16px !important;
}

.message-container {
  font-family: 'Open Sans', sans-serif;
}

</style>
