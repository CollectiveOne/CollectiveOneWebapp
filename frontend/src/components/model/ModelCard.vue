<template lang="html">
  <div class="">
    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showCardModal"
          :cardWrapperId="cardWrapper.id" :initiativeId="initiativeId"
          :key="cardWrapper.id"
          @close="showCardModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="w3-card-2 card-container w3-display-container"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div v-if="card.title !== ''" class="">
        <b>{{ card.title }}</b>
      </div>

      <div class="card-text">
        <p>{{ card.text }}</p>
      </div>

      <transition name="fadeenter">
        <div v-if="showActionButton"
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

export default {
  name: 'model-card',

  components: {
    'app-model-card-modal': ModelCardModal
  },

  data () {
    return {
      showActionButton: false,
      showCardModal: false
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
    }
  },

  props: {
    cardWrapper: {
      type: Object,
      default: null
    },
    initiativeId: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>

.card-container {
  padding: 8px 16px 16px 16px !important;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

</style>
