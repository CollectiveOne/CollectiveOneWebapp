<template lang="html">
  <tr :class="rowClass">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-move-message-modal
          v-if="showMoveMessageModal"
          :activity="activity"
          @close="closeMoveModal()">
        </app-move-message-modal>
      </transition>
    </div>

    <td class="avatar-col w3-center">
      <app-user-avatar :user="activity.triggerUser" :showName="false" :small="true"></app-user-avatar>
    </td>
    <td class="text-div wrap-long w3-display-container"
      @mouseover="hovering = true"
      @mouseleave="hovering = false">

      <div class="top-line w3-row">
        <div class="w3-small w3-left event-trigger-user">
          <b><app-user-link :user="activity.triggerUser"></app-user-link></b>
          <span v-if="addTime">
             , {{ $t('notifications.TIME_AGO', { time: getTimeStrSince(activity.timestamp) }) }}
          </span>

          <span v-if="outsideOfThisContext">
            , in
            <app-model-section-tag
              style="display: inline-block; margin-left: 4px;"
              :section="applicableOnSection">
            </app-model-section-tag>
          </span>
        </div>

        <div class="event-summary w3-small w3-left" :class="{'event-summary-solid': !showMessagesText, 'event-summary-light': showMessagesText}">
          <span v-if="isInitiativeCreated" class="">
             {{ $t('notifications.CREATED_INIT') }} <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
          </span>

          <span v-if="isInitiativeEdited" class="">

            {{ $t('notifications.EDITED') }} <app-initiative-link :initiative="activity.initiative"></app-initiative-link> {{ initiativeChanged }}
          </span>

          <span v-if="isSubinitiativeCreated" class="">
            {{ $t('notifications.CREATED') }} <app-initiative-link :initiative="activity.subInitiative"></app-initiative-link>
            {{ $t('notifications.UNDER') }} <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
          </span>

          <span v-if="isInitiativeDeleted" class="">
            {{ $t('notifications.DELETED') }} <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
          </span>

          <span v-if="isTokenCreated" class="">
            <span v-html="$t('notifications.CREATED_TOKEN_IN', { name: activity.mint.tokenName })"></span>
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,
            <span v-html="$t('notifications.AND_MINTED', { amount: activity.mint.value.toFixed(2) })"></span>
          </span>

          <span v-if="isTokensMinted" class="">
            {{ $t('notifications.MINTED_AMOUNT', { amount: activity.mint.value.toFixed(2), name: activity.mint.tokenName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
          </span>

          <span v-if="isNewPRAssigantionCreated" class="">
            {{ $t('notifications.CREATED_NEW_PR') }} <app-assignation-link :assignation="activity.assignation"></app-assignation-link>
            {{ $t('notifications.AMOUNT_OF_FROM'), { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName } }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
          </span>

          <span v-if="isNewDAssigantionCreated" class="">
            {{ $t('notifications.MADE_DIRECT') }} <app-assignation-link :assignation="activity.assignation"></app-assignation-link> of
            {{ $t('notifications.AMOUNT_OF_FROM'), { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName } }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
            {{ $t('notifications.TO') }}
            <span v-for="(receiver, ix) in activity.assignation.receivers">{{ ix > 0 ? ', ' : '' }}<app-user-link :user="receiver.user"></app-user-link></span>
          </span>

          <span v-if="isNewPRAssigantionDone" class="">
            {{ $t('notifications.PEER_REVIEWED') }}
            <app-assignation-link :assignation="activity.assignation"></app-assignation-link>
            {{ $t('notifications.HAS_BEEN_DONE') }}.
            {{ $t('notifications.CREATED_BY') }}
            <app-user-link :user="activity.triggerUser"></app-user-link>
            {{ $t('notifications.AMOUNT_OF_FROM', { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>.
          </span>

          <span v-if="isInitiativeTransfer" class="">
            {{ $t('notifications.TRANSFERRED') }}
            {{ $t('notifications.AMOUNT_OF_FROM', { amount:  activity.transfer.value.toFixed(2), name: activity.transfer.assetName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
            {{ $t('notifications.TO') }}
            <app-initiative-link :initiativeId="activity.transfer.receiverId" :initiativeName="activity.transfer.receiverName"> </app-initiative-link>
          </span>

          <span v-if="isAssignationRevertOrdered" class="">
            {{ $t('notifications.ORDERED_THE_REVERT_OF') }}
            <app-assignation-link :assignation="activity.assignation"></app-assignation-link>
            {{ $t('notifications.OF') }}
            {{ $t('notifications.AMOUNT_OF_FROM', { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
            {{ $t('notifications.WITH_MOTIVE', { motive: activity.assignation.motive }) }}
          </span>

          <span v-if="isAssignationRevertCancelled" class="">
            {{ $t('notifications.REVERT_ORDER_CANCELLED') }}
            <app-assignation-link :assignation="activity.assignation"></app-assignation-link>
            {{ $t('notifications.OF') }}
            {{ $t('notifications.AMOUNT_OF_FROM', { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
            {{ $t('notifications.WITH_MOTIVE', { motive: activity.assignation.motive }) }},
            {{ $t('notifications.REVERT_NOT_ACCEPTED') }}.
          </span>

          <span v-if="isAssignationReverted" class="">
            {{ $t('notifications.REVERT_ORDER_ACCEPTED') }}
            <app-assignation-link :assignation="activity.assignation"></app-assignation-link>
            {{ $t('notifications.OF') }}
            {{ $t('notifications.AMOUNT_OF_FROM', { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
            {{ $t('notifications.REVERT_ACCEPTED') }}.
          </span>

          <span v-if="isAssignationDeleted" class="">
            {{ $t('notifications.ONGOING_TRANSFER') }}
            <app-assignation-link :assignation="activity.assignation"></app-assignation-link>
            {{ $t('notifications.OF') }}
            {{ $t('notifications.AMOUNT_OF_FROM', { amount:  activity.assignation.assets[0].value.toFixed(2), name: activity.assignation.assets[0].assetName }) }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>
            {{ $t('notifications.WITH_MOTIVE', { motive: activity.assignation.motive }) }},
            {{ $t('notifications.HAS_BEEN_DELETED') }}.
          </span>

          <span v-if="isModelSectionCreatedOnSection" class="">
            {{ $t('notifications.CREATED_SUBSECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>
            {{ $t('notifications.UNDER_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>.
          </span>
          <span v-if="isModelSectionCreatedOnInitiative" class="">
            {{ $t('notifications.CREATED_SECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>
            {{ $t('notifications.UNDER_INITIATIVE') }}
            <app-initiative-link :initiative="activity.initiative"></app-initiative-link>,.
          </span>
          <span v-if="isModelSectionEdited" class="">
            {{ $t('notifications.EDITED_SECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>
            {{ $t('notifications.TITLE_DESCRIPTION') }}.
          </span>
          <span v-if="isModelSectionRemovedFromSection" class="">
            {{ $t('notifications.REMOVED_SUBSECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>
            {{ $t('notifications.FROM_SECTION') }}
            <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
          </span>
          <span v-if="isModelSectionMovedFromSectionToSection" class="">
            {{ $t('notifications.MOVED_THE_SECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>
            {{ $t('notifications.FROM_SECTION') }}
            <app-model-section-link :section="activity.fromSection"></app-model-section-link>
            {{ $t('notifications.TO_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>.
          </span>
          <span v-if="isModelSectionMoved" class="">
            {{ $t('notifications.MOVED_THE_SECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>.
          </span>

          <span v-if="isModelCardWrapperCreated" class="">
            {{ $t('notifications.CREATED_THE_CARD', { scope: cardWrapperScope }) }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.IN_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>.
          </span>
          <span v-if="isModelCardWrapperAdded" class="">
            {{ $t('notifications.ADDED_THE_CARD', { scope: cardWrapperScope }) }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.IN_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>.
          </span>
          <span v-if="isModelCardWrapperRemoved" class="">
            {{ $t('notifications.REMOVED_THE_CARD', { scope: cardWrapperScope }) }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.fromSection"></app-model-card-link>
            {{ $t('notifications.FROM_SECTION') }}
            <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
          </span>
          <span v-if="isModelSectionDeleted" class="">
            {{ $t('notifications.DELETED_SECTION') }}
            <app-model-section-link :section="activity.modelSection"></app-model-section-link>
          </span>

          <span v-if="isModelCardWrapperMadeShared" class="">
            {{ $t('notifications.MADE_THE_CARD') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.IN_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>
            {{ $t('notifications.A_SHARED_CARD') }}.
          </span>
          <span v-if="isModelCardWrapperMadeCommon" class="">
            {{ $t('notifications.MADE_THE_CARD') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.IN_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>
            {{ $t('notifications.A_COMMON_CARD') }}.
          </span>
          <span v-if="isModelCardWrapperEdited" class="">
            {{ $t('notifications.EDITED_THE_CARD') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>.
          </span>
          <span v-if="isModelCardWrapperMovedSameSection" class="">
            {{ $t('notifications.MOVED_CARD') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.WITHIN_SECTION') }}
            <app-model-section-link :section="activity.fromSection"></app-model-section-link>.
          </span>
          <span v-if="isModelCardWrapperMovedDiferentSections" class="">
            {{ $t('notifications.MOVED_CARD') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.FROM_SECTION') }}
            <app-model-section-link :section="activity.fromSection"></app-model-section-link>
            {{ $t('notifications.TO_SECTION') }}
            <app-model-section-link :section="activity.onSection"></app-model-section-link>.
          </span>
          <span v-if="isModelCardWrapperDeleted" class="">
            {{ $t('notifications.DELETED_CARD') }}
            <app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link>.
          </span>
          <span v-if="isConsentStatusOpened" class="">
            {{ $t('notifications.STARTED_CONSENT') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>.
          </span>
          <span v-if="isConsentStatusClosed" class="">
            {{ $t('notifications.ENDED_CONSENT') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>.
          </span>
          <span v-if="isConsentStatusReopened" class="">
            {{ $t('notifications.REOPENED_CONSENT') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>.
          </span>
          <span v-if="isConsentPositionStated" class="">
            {{ $t('notifications.POSITION_SET', { position: getConsentPositionText(activity.positionColor) }) }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
          </span>
          <span v-if="isConsentPositionChanged" class="">
            {{ $t('notifications.POSITION_CHANGED') }}
            <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            {{ $t('notifications.TO_POSITION', { position: getConsentPositionText(activity.positionColor) }) }}.
          </span>

          <span v-if="isMessagePosted && (isMessageInCardWrapper || isMessageInCardWrapperOnSection)">
            <span v-if="loggedUserMentioned">{{ $t('notifications.MENTIONED_YOU') }}</span>
            <span v-else> <b>{{ $t('notifications.IN_CARD') }}: </b></span>

            <span v-if="isMessageInCardWrapper">
              <app-model-card-alone-link :cardWrapper="activity.modelCardWrapper"></app-model-card-alone-link>.
            </span>
            <span v-if="isMessageInCardWrapperOnSection">
              <app-model-card-link :cardWrapper="activity.modelCardWrapper" :onSection="activity.onSection"></app-model-card-link>
            </span>
          </span>
        </div>
      </div>

      <div v-if="isMessagePosted && showMessagesText" class="w3-row cursor-pointer" @click="replyToMessage(false)">
        <div v-if="!messageDeleted" class="">
          <vue-markdown class="marked-text message-container" :source="activity.message.text" :anchorAttributes="{target: '_blank'}"></vue-markdown>
        </div>
        <div v-else class="">
          [deleted]
        </div>
      </div>

      <div class="message-options" v-if="isMessagePosted && showMessagesText">
        <transition name="fadeenter">
          <div v-if="$store.state.user.authenticated && !messageDeleted">

            <popper :append-to-body="true" trigger="click" :options="popperOptions" :toggleShow="toggleMessageOptions" class="">
              <div class="">
                <app-drop-down-menu
                  class="drop-menu"
                  @reply="replyToMessage(true)"
                  @edit="editMessage()"
                  @move="moveMessage()"
                  @convert-to-card="convertMessageToCard()"
                  @remove="removeIntent = true"
                  :items="menuItems">
                </app-drop-down-menu>

                <div class="w3-card w3-white drop-menu">

                  <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
                    <div class="w3-padding w3-round light-grey w3-margin-bottom">
                      <p v-html="$t('notifications.DELETE_MSG_WARNING')"></p>
                    </div>
                    <button
                      class="w3-button light-grey"
                      @click="removeIntent = false">{{ $t('general.CANCEL') }}
                    </button>
                    <button
                      class="w3-button button-blue"
                      @click="removeMessage()">{{ $t('general.CONFIRM') }}
                    </button>
                  </div>

                </div>

              </div>

              <div slot="reference" class="expand-btn w3-large fa-button gray-2-color">
                <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
              </div>
            </popper>
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
import MoveMessageModal from '@/components/modal/MoveMessageModal.vue'
import ModelSectionTag from '@/components/model/ModelSectionTag.vue'

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
    showMessagesText: {
      type: Boolean,
      default: false
    },
    contextElementId: {
      type: String,
      default: ''
    },
    addInAppState: {
      type: Boolean,
      default: false
    }
  },

  components: {
    'app-user-link': UserLink,
    'app-initiative-link': InitiativeLink,
    'app-assignation-link': AssignationLink,
    'app-model-section-link': ModelSectionLink,
    'app-model-card-link': ModelCardInSectionLink,
    'app-model-card-alone-link': ModelCardAloneLink,
    'app-move-message-modal': MoveMessageModal,
    'app-model-section-tag': ModelSectionTag
  },

  data () {
    return {
      hovering: false,
      toggleMessageOptions: false,
      showMoveMessageModal: false,
      showConvertMessageModal: false,
      removeIntent: false
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
    cardWrapperScope () {
      if (this.activity.modelCardWrapper) {
        switch (this.activity.modelCardWrapper.scope) {
          case 'PRIVATE':
            return this.$t('model.PRIVATE')

          case 'SHARED':
            return this.$t('model.SHARED')

          case 'COMMON':
            return this.$t('model.COMMON')
        }
      }
      return ''
    },
    isModelCardWrapperCreated () {
      return this.activity.type === 'MODEL_CARDWRAPPER_CREATED'
    },
    isModelCardWrapperMadeShared () {
      return this.activity.type === 'MODEL_CARDWRAPPER_MADE_SHARED'
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
    isConsentStatusOpened () {
      return this.activity.type === 'CONSENT_STATUS_OPENED'
    },
    isConsentStatusClosed () {
      return this.activity.type === 'CONSENT_STATUS_CLOSED'
    },
    isConsentStatusReopened () {
      return this.activity.type === 'CONSENT_STATUS_REOPENED'
    },
    isConsentPositionStated () {
      return this.activity.type === 'CONSENT_POSITION_STATED'
    },
    isConsentPositionChanged () {
      return this.activity.type === 'CONSENT_POSITION_CHANGED'
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
      if (!this.isMessagePosted || !this.showMessagesText) {
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
    messageDeleted () {
      if (this.activity.message) {
        return this.activity.message.status === 'DELETED'
      }
      return false
    },
    outsideOfThisContext () {
      if (this.applicableOnSection) {
        return this.applicableOnSection.id !== this.contextElementId
      }
      return false
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
        return this.$t('notifications.NAME_AND_DESC')
      }

      if (nameChanged) {
        return this.$t('general.NAME')
      }

      if (driverChanged) {
        return this.$t('general.DESCRIPTION')
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
    },

    loggedUserMentioned () {
      if (this.$store.state.user.profile) {
        let c1Id = this.$store.state.user.profile.c1Id
        let ix = this.activity.mentionedUsers.findIndex((user) => {
          return user.c1Id === c1Id
        })
        return ix !== -1
      }
    },

    rowClass () {
      let rowClass = {}
      if (this.addInAppState) {
        rowClass['w3-leftbar'] = this.activity.inAppState === 'PENDING'
        rowClass['not-read-color'] = this.activity.inAppState === 'PENDING'
      }
      return rowClass
    },

    popperOptions () {
      return {
        placement: 'left',
        modifiers: {
          preventOverflow: {
            enabled: true
          }
        }
      }
    },

    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },

    menuItems () {
      let items = []

      items.push({
        text: this.$t('notifications.REPLY'),
        value: 'reply',
        faIcon: 'fa-reply'
      })

      if (this.authorIsLoggedUser) {
        items.push({
          text: this.$t('general.EDIT'),
          value: 'edit',
          faIcon: 'fa-pencil'
        })
      }

      if (this.isLoggedAnEditor) {
        items.push({
          text: this.$t('general.MOVE'),
          value: 'move',
          faIcon: 'fa-arrow-circle-right'
        })
      }

      if (this.isLoggedAnEditor) {
        items.push({
          text: this.$t('notifications.CONVERT_TO_CARD'),
          value: 'convert-to-card',
          faIcon: 'fa-square-o'
        })
      }

      if (this.authorIsLoggedUser) {
        items.push({
          text: this.$t('general.REMOVE'),
          value: 'remove',
          faIcon: 'fa-times'
        })
      }

      return items
    },

    applicableOnSection () {
      return this.activity.modelCardWrapper != null ? (this.activity.onSection != null ? this.activity.onSection : this.activity.modelSection) : this.activity.modelSection
    }
  },
  methods: {
    closeMoveModal () {
      this.showMoveMessageModal = false
      this.$emit('reset-activity')
    },
    closeConvertModal () {
      this.showConvertMessageModal = false
      this.$emit('reset-activity')
    },
    editMessage () {
      this.$emit('edit-message', this.activity.message)
      this.toggleMessageOptions = !this.toggleMessageOptions
    },
    replyToMessage (toggleFlag) {
      this.$emit('reply-to-message', this.activity)
      if (toggleFlag) {
        this.toggleMessageOptions = !this.toggleMessageOptions
      }
    },
    moveMessage () {
      this.toggleMessageOptions = !this.toggleMessageOptions
      this.showMoveMessageModal = true
    },
    convertMessageToCard () {
      this.toggleMessageOptions = !this.toggleMessageOptions

      var cardDto = {
        title: '',
        text: this.activity.message.text,
        newScope: 'COMMON'
      }

      var toSectionId = this.applicableOnSection.id

      /* create new card */
      this.sendingData = true
      this.axios.post('/1/model/section/' + toSectionId + '/cardWrapper', cardDto, {}).then((response) => {
        if (response.data.result === 'success') {
          this.$emit('reset-activity')
          this.$store.commit('triggerUpdateSectionCards')
        } else {
          console.log(response.data.message)
        }
      }).catch((error) => {
        console.log(error)
      })
    },
    removeMessage () {
      this.toggleMessageOptions = !this.toggleMessageOptions

      this.axios.delete('/1/messages/' + this.activity.message.id, {}).then((response) => {
        if (response.data.result === 'success') {
          this.$emit('reset-activity')
        } else {
          console.log(response.data.message)
        }
      }).catch((error) => {
        console.log(error)
      })
    },
    getTimeStrSince (v) {
      return getTimeStrSince(v)
    },
    getConsentPositionText (color) {
      switch (color) {
        case 'GREEN':
          return this.$t('model.SUPPORT')

        case 'YELLOW':
          return this.$t('model.UNDECIDED')

        case 'RED':
          return this.$t('model.OBJECT')
      }

      return ''
    }
  },

  mounted () {
  }
}
</script>

<style >

.message-container {
  font-family: 'Open Sans', sans-serif;
}

.message-container img {
  max-width: 100%;
  max-height: 1000px;
}

</style>

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

.event-trigger-user {
  padding: 3px 0px;
  margin-right: 12px;
}

.event-summary {
  border-radius: 6px;
  padding: 3px 6px;
  font-style: italic;
}

.event-summary-light {
  background-color: #fafafa;
  color: #637484;
}

.event-summary-solid {
  background-color: #eff3f6;
  color: black;
  width: 100%;
}

.drop-menu {
  width: 200px;
  text-align: left;
  font-size: 15px;
}

.message-options {
  position: absolute;
  right: 5px;
  top: 5px;
}

.message-options .expand-btn {
  width: 30px;
  text-align: center;
}

.not-read-color {
  border-left-color: #b35454 !important;
}

</style>
