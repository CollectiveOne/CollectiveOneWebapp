<template lang="html">
  <div class="w3-display-container w3-card-4 w3-topbar w3-round-large" :class="containerClass"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="card-content-container" :class="{'limit-height': !showFull}">
      <div class=""
        ref="cardContent" >
        <app-model-card-as-card-content
         :card="card"
         :showFull="showFull"
         @card-clicked="cardClicked()">
        </app-model-card-as-card-content>
      </div>
    </div>

    <div class="w3-row bottom-row light-grey">

      <div v-if="textTooLong" class="expand-height-button">
        <div @click="showMoreClick()"
          class="w3-padding gray-2-color cursor-pointer">
          <i class="fa fa-arrows-v" aria-hidden="true"></i>
        </div>
      </div>

      <div v-if="!inCardSelector" class="w3-row">
        <app-card-user-indicators
          :cardWrapper="cardWrapper"
          :inSection="inSection"
          :hideCardControls="hideCardControls"
          :inCardSelector="inCardSelector"
          :cardRouteName="cardRouteName"
          @update="$emit('update')"
          @createNew="$emit('createNew')"
          @edit="$emit('edit')"
          @setConsent="$emit('setConsent', $event)"
          @updateCards="$emit('updateCards')">
        </app-card-user-indicators>
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
import ModelCardAsCardContent from '@/components/model/cards/ModelCardAsCardContent.vue'
import CardUserIndicators from '@/components/model/cards/CardUserIndicators.vue'

export default {

  name: 'model-card-as-card',

  mixins: [ cardMixin ],

  components: {
    'app-card-user-indicators': CardUserIndicators,
    'app-model-card-as-card-content': ModelCardAsCardContent
  },

  props: {
  },

  data () {
    return {
      hovering: false,
      showFull: false,
      textTooLong: false
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
    }
  },

  methods: {
    checkTextTooLong () {
      if (!this.$refs.cardContent) {
        this.textTooLong = false
      }
      console.log(this.$refs.cardContent.scrollHeight)
      if (this.$refs.cardContent.scrollHeight > 250) {
        this.textTooLong = true
      } else {
        this.textTooLong = this.$refs.cardContent.clientHeight < this.$refs.cardContent.scrollHeight
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
    this.$nextTick(() => {
      this.checkTextTooLong()
    })
  }
}
</script>

<style scoped>

.card-content-container {
  max-height: 80vh;
  transition: max-height 1000ms ease;
  overflow-y: auto;
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

.image-container-doc {
  height: 350px;
  margin-bottom: 16px;
}

.image-container-doc img {
  height: 100%;
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

.expand-height-button {
  position: absolute;
  left: calc(100% - 38px);
  top: -38px;
  background-color: rgba(218, 218, 218, 0.5);
}

</style>
