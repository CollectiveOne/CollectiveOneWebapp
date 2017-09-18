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

    <hr v-if="draggingOver">

    <div class="w3-display-container"
      :class="{ 'w3-card-2': cardEffect, 'highlight': highlight }"
      @mouseover="hoverEnter()"
      @mouseleave="hoverLeave()">

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

      <div class="w3-row"
        :class="{'card-container-padded': cardEffect, 'card-container-slim': !cardEffect }">

        <div v-if="card.title !== ''" class="">
          <b>{{ card.title }}</b>
        </div>

        <div class="card-text">
          <p>{{ card.text }}</p>
        </div>

      </div>

      <div v-if="cardEffect" class="w3-row">
        <div v-if="cardWrapper.inSections.length > 1" class="bottom-row light-grey">
          <i>also in:</i>
          <div v-for="inSection in cardWrapper.inSections" class="insection-tag-container">
            <div v-if="inSection.id !== sectionId" class="">
              <router-link :to="{ name: 'ModelSection', params: { sectionId: inSection.id } }"
                class="gray-1 w3-tag w3-round w3-small">
                {{ inSection.title }}
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <transition name="fadeenter">
        <div v-if="showActionButton && enableExpand"
          @click="showCardModal()"
          class="w3-button model-action-button gray-2-color w3-display-topright">
          <i class="fa fa-expand" aria-hidden="true"></i>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import { dateString } from '@/lib/common.js'

export default {
  name: 'model-card',

  components: {
  },

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    initiativeId: {
      type: String,
      default: ''
    },
    sectionId: {
      type: String,
      default: ''
    },
    cardEffect: {
      type: Boolean,
      default: true
    },
    enableExpand: {
      type: Boolean,
      default: true
    },
    hoverHighlight: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showActionButton: false,
      highlight: false,
      draggingOver: false,
      enterCounter: []
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
    dateString (v) {
      return dateString(v)
    },
    showCardModal () {
      this.$emit('show-card-modal', {
        new: false,
        cardWrapperId: this.cardWrapper.id,
        initiativeId: this.initiativeId,
        sectionId: this.sectionId
      })
    },
    dragStart (event) {
      var moveCardData = {
        type: 'MOVE_CARD',
        cardWrapperId: this.cardWrapper.id,
        fromSectionId: this.sectionId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveCardData))
    },
    dragEnter (event) {
      this.draggingOver = true
    },
    dragLeave () {
      this.draggingOver = false
    },
    dragDrop () {
      this.draggingOver = false
    }
  }

}
</script>

<style scoped>

.card-container-padded {
  padding: 8px 16px 10px 16px !important;
}

.card-container-slim {
  padding: 0px 0px 0px 0px !important;
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
