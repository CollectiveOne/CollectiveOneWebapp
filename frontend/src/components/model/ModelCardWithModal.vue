<!-- This wrapper is needed because the model card modal uses
needs the model card component inside, and would crate a recursion -->
<template lang="html">

  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <div v-if="showCardModal" class="">
          <app-model-card-modal
            :isNew="false"
            :initiativeId="initiativeId"
            :cardWrapperId="cardWrapper.id"
            :inSectionId="inSectionId"
            :inSectionTitle="inSectionTitle"
            @close="showCardModal = false">
          </app-model-card-modal>
        </div>
      </transition>
    </div>
    
    <div class="w3-display-container"
      @mouseover="hoverEnter()"
      @mouseleave="hoverLeave()">

      <!-- forward all props  -->
      <app-model-card
        :cardWrapper="cardWrapper"
        :initiativeId="initiativeId"
        :inSectionId="inSectionId"
        :inSectionTitle="inSectionTitle"
        :cardEffect="cardEffect"
        :hoverHighlight="hoverHighlight"
        :floating="floating"
        :highlight="highlight">
      </app-model-card>

      <transition name="fadeenter">
        <div v-if="showActionButton && enableExpand"
          @click="showCardModal = true"
          class="w3-button model-action-button gray-2-color w3-display-topright">
          <i class="fa fa-expand" aria-hidden="true"></i>
        </div>
      </transition>

    </div>

  </div>

</template>

<script>
import ModelCardModal from '@/components/model/modals/ModelCardModal.vue'
import ModelCard from '@/components/model/ModelCard.vue'

export default {
  components: {
    'app-model-card-modal': ModelCardModal,
    'app-model-card': ModelCard
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
    enableExpand: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showActionButton: false,
      showCardModal: false,
      highlight: false
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
    }
  }
}
</script>

<style lang="css">
</style>
