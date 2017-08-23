<template lang="html">
  <div class="">
    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showCardModal"
          :cardId="card.id" :initiativeId="initiativeId"
          :key="card.id"
          @close="showCardModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="card-container w3-leftbar w3-border-thik w3-display-container w3-border-top"
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

  props: {
    card: {
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
  padding: 5px 5px 5px 16px;
  border-top-color: #f2f2f2 !important;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}



</style>
