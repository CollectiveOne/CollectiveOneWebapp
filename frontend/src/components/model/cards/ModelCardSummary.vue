<template lang="html">
  <div class="card-summary-container w3-leftbar w3-card w3-row" :class="containerClass"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-col m8 left-div">
      <div v-if="!inCardSelector" @click="cardClicked()" class="w3-left expand-btn cursor-pointer">
        <i class="fa fa-expand" aria-hidden="true"></i>
      </div>

      <div class="w3-left text-div">
        {{ cardShortTitle }}
      </div>

      <div v-if="cardWrapper.inModelSections.length > 0 && !hideCardControls" class="w3-right text-div">
        <app-in-model-sections-tags
          :inModelSections="cardWrapper.inModelSections"
          :hideSectionId="inSectionId">
        </app-in-model-sections-tags>
      </div>
    </div>

    <div class="w3-col m4 right-div">

      <div v-if="!inCardSelector" class="">
        <app-card-user-indicators
          :cardWrapper="cardWrapper"
          :inSection="inSection"
          :hideCardControls="hideCardControls"
          :inCardSelector="inCardSelector"
          :cardRouteName="cardRouteName"
          :hideInSectionTags="true"
          @update="$emit('update')"
          @createNew="$emit('createNew')"
          @edit="$emit('edit')"
          @startConsent="$emit('startConsent')"
          @updateCards="$emit('updateCards')">
        </app-card-user-indicators>
      </div>

    </div>
  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import InModelSectionsTags from '@/components/model/InModelSectionsTags.vue'
import CardUserIndicators from '@/components/model/cards/CardUserIndicators.vue'

export default {

  name: 'model-card-summary',

  mixins: [ cardMixin ],

  components: {
    'app-in-model-sections-tags': InModelSectionsTags,
    'app-card-user-indicators': CardUserIndicators
  },

  props: {
  },

  data () {
    return {
      hovering: false
    }
  },

  computed: {
    cardShortTitle () {
      return this.card.title !== '' ? this.card.title : (this.card.text.length < 40 ? this.card.text : this.card.text.slice(0, 40) + '...')
    },
    card () {
      return this.cardWrapper.card
    },
    hasImage () {
      return this.card.imageFile !== null
    }
  },

  methods: {
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    }
  },

  created () {
  }
}
</script>

<style scoped>

.card-summary-container {
  padding: 6px 0px;
}

.left-div {
  padding: 0px 12px;
}

.right-div {
  padding: 0px 12px;
  border-left-style: solid;
  border-width: 1px;
  border-color: #d0d0d0;
}

.expand-btn {
  width: 28px;
  margin-right: 10px;
  background-color: #d6d6d6;
  text-align: center;
  padding: 3px 6px;
  border-radius: 3px;
}

.expand-btn:hover {
  background-color: #a3a0a0;
}

.control-div {
  padding: 3px 6px;
}

.text-div {
  padding-top: 3px;
}

.editors-div {
  margin-left: 10px;
  padding-left: 8px;
  border-left-style: dotted;
  border-color: #c1c1c1;
  border-width: 1px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

</style>
