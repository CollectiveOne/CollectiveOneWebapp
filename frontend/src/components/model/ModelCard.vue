<template lang="html">
  <div v-if="cardWrapper" class=""
    draggable="true"
    @dragstart="dragStart($event)">
    <!-- TODO: cannot configure a clearn dragenter dragleave sequence. They
    are continuosly called even when dragging over.
    @dragover.prevent
    @dragenter.prevent="dragEnter($event)"
    @dragleave.prevent="dragLeave()"
    @drop="dragDrop()"> -->

    <div class="" :class="{ 'w3-card-2': cardEffect, 'highlight': highlight }">

      <div v-if="showDetails" class="w3-row light-grey">
        <div class="w3-col s6">
          <div class="w3-tag" :class="stateClass">
            {{ cardWrapper.state }}
          </div>
        </div>
        <div class="w3-col s6">
          {{ dateString(cardWrapper.targetDate) }}
        </div>
      </div>

      <div v-if="floating" class="">
        <div class="top-row light-grey">
          <i>in:</i>
          <div v-for="inSection in cardWrapper.inSections" class="insection-tag-container">
            <router-link :to="{ name: 'ModelSection', params: { sectionId: inSection.id } }"
              class="gray-1 w3-tag w3-round w3-small">
              {{ inSection.title }}
            </router-link>
          </div>
        </div>
      </div>

      <div class="w3-row card-container"
        :class="{'card-container-padded': cardEffect, 'card-container-slim': !cardEffect }">

        <div v-if="card.title !== ''" class="">
          <b>{{ card.title }}</b>
        </div>

        <div class="card-text">
          <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
        </div>

      </div>

      <div v-if="cardEffect" class="w3-row">
        <div v-if="!floating" class="">
          <div v-if="cardWrapper.inSections.length > 1" class="bottom-row light-grey">
            <i>also in:</i>
            <div v-for="inSection in cardWrapper.inSections" class="insection-tag-container">
              <div v-if="inSection.id !== inSectionId" class="">
                <router-link :to="{ name: 'ModelSection', params: { sectionId: inSection.id } }"
                  class="gray-1 w3-tag w3-round w3-small">
                  {{ inSection.title }}
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { dateString } from '@/lib/common.js'

export default {
  name: 'model-card',

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
    hoverHighlight: {
      type: Boolean,
      default: false
    },
    floating: {
      type: Boolean,
      default: false
    },
    highlight: {
      type: Boolean,
      default: false
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
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    dragStart (event) {
      var moveCardData = {
        type: 'MOVE_CARD',
        cardWrapperId: this.cardWrapper.id,
        fromSectionId: this.inSectionId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveCardData))
    }
  }

}
</script>

<style scoped>

.card-container {
}

.card-container-padded {
  padding: 8px 0px 12px 12px !important;
}

.card-container-slim {
  padding: 0px 0px 0px 0px !important;
}

.top-row {
  padding: 6px 3px 6px 3px;
}

.card-text {
  max-height: 250px;
  overflow-y: auto;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

.highlight {
  background-color: #e7e8ec !important;
}

.bottom-row {
  padding: 8px 5px 3px 5px;
}

.insection-tag-container {
  display: inline-block;
  margin-left: 5px;
  margin-bottom: 5px;
}

</style>
