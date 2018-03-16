<template lang="html">
  <div v-if="cardWrapper" class="">

    <div class="w3-display-container"
      :class="{ 'w3-card-4 w3-topbar border-blue w3-round-large': cardEffect }"
      @mouseover="hovering = true"
      @mouseleave="hovering = false">

      <div class="w3-row card-container cursor-pointer w3-display-container"
        ref="cardContent">

        <div class="w3-col s12">
          <div v-if="hasImage"
            class="w3-row image-container w3-center w3-display-container"
            @click="$emit('expand-modal')">
            <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
          </div>

          <div :class="{'card-container-padded': cardEffect, 'card-container-slim': !cardEffect }"
            @click="$emit('expand-modal')">
            <div v-if="card.title !== ''" class="w3-row">
              <b>{{ card.title }}</b>
            </div>

            <div ref="cardText"
              class="w3-row card-text"
              :style="cardTextStyle">
              <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
            </div>
          </div>

          <div v-if="textTooLong() && cardEffect" class="">
            <div @click="showMoreClick()"
              class="w3-padding model-action-button gray-2-color w3-display-bottomright cursor-pointer">
              <i class="fa fa-arrows-v" aria-hidden="true"></i>
            </div>
          </div>

        </div>
      </div>

      <div v-if="cardEffect" class="w3-row bottom-row light-grey">

          <div v-if="cardWrapper.inSections.length > 0" class="">
            <div v-for="inSection in cardWrapper.inSections" :key="inSection.id"
              v-if="showThisTag(inSection)" class="w3-left insection-tag-container">
              <div class="">
                <router-link :to="{ name: 'ModelSection', params: { sectionId: inSection.id } }"
                  class="gray-1 w3-tag w3-round w3-small">
                  {{ inSection.title }}
                </router-link>
              </div>
            </div>
          </div>

          <div class="w3-right cursor-pointer indicator-comp"
            @click="$emit('expand-modal', true)">
            <app-indicator
              :initiativeId="initiativeId"
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
              :initiativeId="initiativeId"
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

      <transition name="fadeenter">
        <div v-if="hovering && enableExpand"
          class="w3-padding model-action-button gray-2-color w3-display-topright cursor-pointer"
          @click="$emit('expand-modal')">
          <i class="fa fa-expand" aria-hidden="true"></i>
        </div>
      </transition>

    </div>
  </div>
</template>

<script>
import { dateString } from '@/lib/common.js'
import smoothHeight from 'vue-smooth-height'

export default {
  name: 'model-card',

  mixins: [smoothHeight],

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    initiativeId: {
      type: String,
      default: ''
    },
    inSectionId: {
      type: String,
      default: ''
    },
    inSectionTitle: {
      type: String,
      default: ''
    },
    cardEffect: {
      type: Boolean,
      default: true
    },
    floating: {
      type: Boolean,
      default: false
    },
    enableExpand: {
      type: Boolean,
      default: true
    },
    forceUpdate: {
      type: Boolean,
      default: true
    }
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
    showDetails () {
      if (this.cardEffect) {
        return this.cardWrapper.stateControl
      }
    },
    stateClass () {
      switch (this.cardWrapper.state) {
        case 'REALITY':
          return {
            'success-panel': true
          }

        case 'PLAN':
          return {
            'warning-panel': true
          }
      }
      return {}
    },
    hasImage () {
      return this.card.imageFile !== null
    },
    cardTextStyle () {
      if (this.cardEffect && !this.showFull) {
        if (this.hasImage) {
          return {
            maxHeight: '100px'
          }
        } else {
          return {
            maxHeight: '250px'
          }
        }
      } else {
        return {}
      }
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    hoverEnter () {
      this.showActionButton = true
      if (this.hoverHighlight) {
        this.highlight = true
      }
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    },
    showMoreClick () {
      this.showFull = !this.showFull
    },
    showThisTag (inSection) {
      /* hide current section tag */
      if (this.floating) {
        return true
      } else {
        return inSection.id !== this.inSectionId
      }
    },
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
    toggleLike () {
      this.axios.put('/1/initiative/' + this.initiativeId + '/model/card/' + this.cardWrapper.id + '/like',
        {}, {
          params: {
            likeStatus: !this.cardWrapper.userLiked
          }
        }).then((response) => {
          this.$emit('please-update')
        })
    }
  },

  mounted () {
    this.$registerElement({
      el: this.$refs.cardText
    })

    this.$registerElement({
      el: this.$refs.cardContent
    })
  }
}
</script>

<style scoped>

.card-container {
}

.card-container-padded {
  padding: 8px 6px 12px 12px !important;
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

</style>
