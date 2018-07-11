<template lang="html">

  <div class="user-indicators-container">
    <div class="common-row w3-row" :class="{'common-row-with-gov': showSemaphore}">
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

        <div v-if="showSemaphore" class="w3-right indicator-comp semaphore-container">
          <div class="circle-holder semaphore-red" :class="{'semaphore-red-selected': stateIsRed}"
            @click="setState('RED')">
            <i class="fa fa-circle" aria-hidden="true"></i>
          </div>
          <div class="circle-holder semaphore-yellow" :class="{'semaphore-yellow-selected': stateIsYellow}"
            @click="setState('YELLOW')">
            <i class="fa fa-circle" aria-hidden="true"></i>
          </div>
          <div class="circle-holder semaphore-green" :class="{'semaphore-green-selected': stateIsGreen}"
            @click="setState('GREEN')">
            <i class="fa fa-circle" aria-hidden="true"></i>
          </div>
        </div>

      </div>
    </div>

    <div v-if="showSemaphore" class="governance-row w3-row">
      <app-semaphore-if
        :elementId="cardWrapper.additionId"
        :forceUpdate="forceSemaphoresUpdate">
      </app-semaphore-if>
    </div>

  </div>

</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardControlButtons from '@/components/model/cards/CardControlButtons.vue'
import NotificationsList from '@/components/notifications/NotificationsList.vue'
import InModelSectionsTags from '@/components/model/InModelSectionsTags.vue'
import SemaphoreInterface from '@/components/model/SemaphoreInterface.vue'

export default {

  mixins: [ cardMixin ],

  components: {
    'app-card-control-buttons': CardControlButtons,
    'app-in-model-sections-tags': InModelSectionsTags,
    'app-notifications-list': NotificationsList,
    'app-semaphore-if': SemaphoreInterface
  },

  props: {
    hideInSectionTags: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      forceSemaphoresUpdate: false
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
    },
    setState (value) {
      this.axios.put('/1/model/cardAddition/' + this.cardWrapper.additionId + '/semaphoreState', {}, {
        params: {
          semaphoreState: value,
          elementType: 'CARD_WRAPPER_ADDITION'
        }
      }).then((response) => {
        this.$emit('update')
        this.forceSemaphoresUpdate = !this.forceSemaphoresUpdate
      })
    }
  }

}
</script>

<style scoped>

.user-indicators-container {
  width: 100%;
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

.semaphore-container {
  margin-right: 12px;
}

</style>
