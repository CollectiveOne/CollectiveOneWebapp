<template lang="html">

  <div class="user-indicators-container">
    <div class="common-row w3-row" :class="{'common-row-with-gov': showConsentPositions}">
      <div class="w3-left">
        <div v-if="cardWrapper.creator !== null" class="w3-left text-div">
          <app-user-avatar :user="cardWrapper.creator" :showName="false" :small="true"></app-user-avatar>
        </div>

        <div v-if="editors.length > 0" class="w3-left editors-div">
          <div class="w3-left" v-for="editor in editors">
            <app-user-avatar v-if="editor.c1Id !== cardWrapper.creator.c1Id" :user="editor" :showName="false" :small="true"></app-user-avatar>
          </div>
        </div>

        <div v-if="cardWrapper.inModelSections.length > 0 && !hideCardControls && !hideInSectionTags" class="w3-margin-left w3-left">
          <app-in-model-sections-tags
            :inModelSections="cardWrapper.inModelSections"
            :hideSectionId="inSectionId">
          </app-in-model-sections-tags>
        </div>
      </div>

      <div class="w3-right">
        <div class="w3-right notification-div">
          <app-notifications-list
            :element="cardWrapper"
            contextType="MODEL_CARD">
          </app-notifications-list>
        </div>

        <div v-if="!hideCardControls && this.isLoggedAnEditor" class="w3-right gray-1-color control-div">
          <app-card-control-buttons
            :cardWrapper="cardWrapper"
            :inSection="inSection"
            @update="$emit('update')"
            @createNew="$emit('createNew')"
            @edit="$emit('edit')"
            @startConsent="$emit('setConsent', true)"
            @stopConsent="$emit('setConsent', false)"
            @updateCards="$emit('updateCards')">
          </app-card-control-buttons>
        </div>

        <div v-if="!isPrivate" class="w3-right cursor-pointer indicator-comp"
          @click="cardClicked()">
          <app-indicator
            contextType="MODEL_CARD"
            :contextElementId="cardWrapper.id"
            :size="18"
            type="messages"
            :forceUpdate="forceUpdate">
          </app-indicator>
        </div>

        <div v-if="showLikes" class="w3-right cursor-pointer indicator-comp"
          @click="toggleLike()">
          <app-indicator
            contextType="MODEL_CARD"
            :contextElementId="cardWrapper.id"
            :size="18"
            type="likes"
            :selected="cardWrapper.userLiked"
            :autoUpdate="false"
            :countInit="cardWrapper.nLikes">
          </app-indicator>
        </div>

      </div>
    </div>

    <div v-if="showConsentPositions" class="governance-row w3-row">
      <app-consent-positions
        :elementId="cardWrapper.additionId"
        :ownPosition="cardWrapper.ownPosition"
        :disabled="!consentIsOpened"
        @update="$emit('update')">
      </app-consent-positions>
    </div>

  </div>

</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardControlButtons from '@/components/model/cards/CardControlButtons.vue'
import NotificationsList from '@/components/notifications/NotificationsList.vue'
import InModelSectionsTags from '@/components/model/InModelSectionsTags.vue'
import ConsentPositions from '@/components/model/ConsentPositions.vue'

export default {

  mixins: [ cardMixin ],

  components: {
    'app-card-control-buttons': CardControlButtons,
    'app-in-model-sections-tags': InModelSectionsTags,
    'app-notifications-list': NotificationsList,
    'app-consent-positions': ConsentPositions
  },

  props: {
    hideInSectionTags: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      forceConsentUpdate: false
    }
  },

  computed: {
    stateIsRed () {
      return this.cardWrapper.semaphoreState ? this.cardWrapper.semaphoreState === 'RED' : false
    },
    stateIsYellow () {
      return this.cardWrapper.semaphoreState ? this.cardWrapper.semaphoreState === 'YELLOW' : false
    },
    stateIsGreen () {
      return this.cardWrapper.semaphoreState ? this.cardWrapper.semaphoreState === 'GREEN' : false
    }
  },

  methods: {
    toggleLike () {
      this.axios.put('/1/model/card/' + this.cardWrapper.id + '/like',
        {}, {
          params: {
            likeStatus: !this.cardWrapper.userLiked
          }
        }).then((response) => {
          this.$emit('update')
        })
    }
  }

}
</script>

<style scoped>

.user-indicators-container {
  width: 100%;
  font-family: 'Raleway', sans-serif;
  font-size: 15px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

.control-div {
  padding: 3px 6px;
}

.common-row-with-gov {
  padding-bottom: 3px;
}

.governance-row {
  border-top-style: solid;
  border-width: 1px;
  border-color: #cbcfd2;
}

.circle-holder {
  display: inline-block;
  padding-top: 2px;
  margin-right: 8px;
  cursor: pointer;
  text-align: center;
}

</style>
