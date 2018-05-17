<template lang="html">
  <div class="w3-display-container w3-card-4 w3-topbar w3-round-large" :class="containerClass"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-row card-container cursor-pointer" :class="{'limit-height': !showFull}"
      ref="cardContent">

      <div class="w3-row" @click="cardClicked()">
        <div v-if="hasImage"
          class="w3-row w3-center w3-display-container image-container">
          <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
        </div>

        <div class="card-container-padded">
          <div v-if="card.title !== ''" class="w3-row card-title">
            {{ card.title }}
          </div>

          <div ref="cardText"
            class="w3-row card-text">
            <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
          </div>
        </div>
      </div>

    </div>

    <div class="w3-row bottom-row light-grey">

      <div v-if="textTooLong()" class="expand-height-button">
        <div @click="showMoreClick()"
          class="w3-padding gray-2-color cursor-pointer">
          <i class="fa fa-arrows-v" aria-hidden="true"></i>
        </div>
      </div>

      <div v-if="cardWrapper.creator !== null" class="w3-left text-div">
        <app-user-avatar :user="cardWrapper.creator" :showName="false" :small="true"></app-user-avatar>
      </div>

      <div v-if="editors.length > 0" class="w3-left editors-div">
        <div class="w3-left" v-for="editor in editors">
          <app-user-avatar v-if="editor.c1Id !== cardWrapper.creator.c1Id" :user="editor" :showName="false" :small="true"></app-user-avatar>
        </div>
      </div>

      <div v-if="cardWrapper.inSections.length > 0 && !hideCardControls" class="w3-margin-left w3-left">
        <div v-for="inSection in cardWrapper.inSections" :key="inSection.id"
          v-if="showThisTag(inSection)" class="w3-left insection-tag-container">
          <div class="">
            <app-model-section-tag :section="inSection"></app-model-section-tag>
          </div>
        </div>
      </div>

      <div v-if="!inCardSelector" class="w3-right">

        <div v-if="!hideCardControls && $store.state.user.authenticated" class="w3-right gray-1-color control-div">
          <app-card-control-buttons
            :cardWrapper="cardWrapper"
            :inSection="inSection"
            @update="$emit('update')"
            @updateCards="$emit('updateCards')">
          </app-card-control-buttons>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="cardClicked()">
          <app-indicator
            contextType="MODEL_CARD"
            :contextElementId="cardWrapper.id"
            :size="18"
            type="messages"
            :forceUpdate="forceUpdate">
          </app-indicator>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
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

    <transition name="fadeenter">
      <div v-if="hovering && !inCardSelector"
        class="w3-padding gray-2-color w3-display-topright cursor-pointer"
        @click="cardClicked()">
        <i class="fa fa-expand" aria-hidden="true"></i>
      </div>
    </transition>

  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardControlButtons from '@/components/model/cards/CardControlButtons.vue'

export default {

  name: 'model-card-as-card',

  mixins: [ cardMixin ],

  components: {
    'app-card-control-buttons': CardControlButtons
  },

  props: {
  },

  data () {
    return {
      hovering: false,
      showFull: false
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
    },
    hasImage () {
      return this.card.imageFile !== null
    }
  },

  methods: {
    textTooLong () {
      if (!this.$refs.cardText) {
        return false
      }
      if (this.$refs.cardText.scrollHeight > 250) {
        return true
      } else {
        return this.$refs.cardText.clientHeight < this.$refs.cardText.scrollHeight
      }
    },
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    },
    showMoreClick () {
      this.showFull = !this.showFull
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

.card-container {
  max-height: 750px;
  transition: max-height 1000ms ease;
  overflow-y: auto;
}

.card-container-padded {
  padding: 8px 6px 12px 12px !important;
}

.limit-height {
  max-height: 250px;
  overflow: hidden;
}

.card-container-slim {
  padding: 0px 0px 0px 0px !important;
}

.top-row {
  padding: 6px 3px 6px 3px;
}

.details-row {
  margin-top: 3px;
}

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  max-height: 150px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.image-container-doc {
  height: 350px;
  margin-bottom: 16px;
}

.image-container-doc img {
  height: 100%;
}

.card-text {
  overflow-y: hidden;
}

.card-text-ascard-no-image {
  max-height: 250px;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

.bottom-row {
  position: relative;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  padding: 3px 6px;
  min-height: 30px;
}

.insection-tag-container {
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 5px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

.control-div {
  padding: 3px 6px;
}

.expand-height-button {
  position: absolute;
  left: calc(100% - 38px);
  top: -38px;
  background-color: rgba(218, 218, 218, 0.5);
}

</style>
