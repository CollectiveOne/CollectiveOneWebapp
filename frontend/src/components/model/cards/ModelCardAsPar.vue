<template lang="html">
  <div class="card-container-doc"
    @mouseover="mouseOver()"
    @mouseleave="mouseLeave()">

    <div class="w3-row">

      <div class="w3-col s12">

        <div :class="containerClassPar" @click="clicked = !clicked">
          <div v-if="card.title !== ''" class="w3-row card-title">
            {{ card.title }}
          </div>

          <div ref="cardText"
            class="w3-row card-text">
            <vue-markdown class="marked-text" :source="card.text" :anchorAttributes="{target: '_blank'}"></vue-markdown>
          </div>
        </div>

        <div v-if="hasImage"
          class="w3-row w3-center w3-display-container image-container">
          <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
        </div>


        <transition name="fadeenter">
          <div v-if="$store.state.user.authenticated && clicked" class="user-indicators-div">
            <app-card-user-indicators
              :cardWrapper="cardWrapper"
              :inSection="inSection"
              :hideCardControls="hideCardControls"
              :inCardSelector="inCardSelector"
              :cardRouteName="cardRouteName"
              @update="$emit('update')"
              @createNew="$emit('createNew')"
              @edit="$emit('edit')"
              @updateCards="$emit('updateCards')">
            </app-card-user-indicators>
          </div>
        </transition>

      </div>
    </div>
  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardUserIndicators from '@/components/model/cards/CardUserIndicators.vue'

export default {

  name: 'model-card-as-par',

  mixins: [ cardMixin ],

  components: {
    'app-card-user-indicators': CardUserIndicators
  },

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    forceUpdate: {
      type: Boolean,
      default: true
    },
    inSection: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      hovering: false,
      clicked: false
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
    },
    hasImage () {
      return this.card.imageFile !== null
    },
    containerClassPar () {
      let baseClass = this.containerClass
      baseClass['card-container-padded'] = !this.hideCardControls

      return baseClass
    }
  },

  methods: {
    mouseOver () {
    },
    mouseLeave () {
    }
  },

  mounted () {
  }
}
</script>

<style>

</style>

<style scoped>

.card-container-padded {
  border-left-style: solid;
  padding-left: 6px;
}

.card-container-doc {
  position: relative;
}

.user-indicators-div {
  position: absolute;
  top: 0px;
  right: 0px;
}

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-top: 16px;
  margin-bottom: 16px;
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

</style>
