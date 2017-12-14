<!-- This wrapper is needed because the model card modal uses
needs the model card component inside, and would crate a recursion -->
<template lang="html">

  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :initiativeId="initiativeId"
          :cardWrapperId="cardWrapper.id"
          :inSectionId="inSectionId"
          :inSectionTitle="inSectionTitle"
          @close="modalClosed()">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="w3-display-container"
      @mouseover="hoverEnter()"
      @mouseleave="hoverLeave()">

      <div @click="showCard()" class="click-area cursor-pointer">
      </div>

      <!-- forward all props  -->
      <app-model-card
        :cardWrapper="cardWrapper"
        :initiativeId="initiativeId"
        :inSectionId="inSectionId"
        :inSectionTitle="inSectionTitle"
        :cardEffect="cardEffect"
        :hoverHighlight="hoverHighlight"
        :floating="floating"
        :highlight="highlight"
        :showFull="showFull"
        @textTooLong="textTooLong = true">
      </app-model-card>

      <transition name="fadeenter">
        <div v-if="showActionButton && enableExpand"
          class="w3-padding model-action-button gray-2-color w3-display-topright">
          <i class="fa fa-expand" aria-hidden="true"></i>
        </div>
      </transition>

      <div v-if="textTooLong" class="">
        <div @click="showMoreClick()" v-if="enableExpand"
          class="w3-padding model-action-button gray-2-color w3-display-bottomright cursor-pointer">
          <i class="fa fa-arrows-v" aria-hidden="true"></i>
        </div>
      </div>

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
    inView: {
      type: Boolean,
      default: false
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
    },
    expandLocally: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showActionButton: false,
      showCardModal: false,
      highlight: false,
      textTooLong: false,
      showFull: false
    }
  },

  methods: {
    modalClosed () {
      this.showCardModal = false
      this.$emit('please-update')
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
    showCard () {
      if (this.expandLocally) {
        this.showCardModal = true
      } else {
        if (this.inView) {
          this.$router.push({
            name: 'ModelCardInView',
            params: {
              initiativeId: this.initiativeId,
              viewId: this.inViewId,
              cardId: this.cardWrapper.id
            }
          })
        } else {
          this.$router.push({
            name: 'ModelCardInSection',
            params: {
              initiativeId: this.initiativeId,
              sectionId: this.inSectionId,
              cardId: this.cardWrapper.id
            }
          })
        }
      }
    }
  }
}
</script>

<style scoped>

.click-area {
  position: absolute;
  height: calc(100% - 40px);
  width: 100%;
}

</style>
