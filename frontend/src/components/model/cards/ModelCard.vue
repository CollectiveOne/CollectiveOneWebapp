<template lang="html">
  <div v-if="cardWrapper" class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showModal"
          :isNew="false"
          :cardWrapperId="cardWrapper.id"
          :inSectionId="inSection.id"
          :inSectionTitle="inSection.title"
          @close="showModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="card-content-container">
      <component
        :is="cardComponent"
        :cardWrapper="cardWrapper"
        :inSection="inSection"
        :forceUpdate="forceUpdate">
      </component>
      <!-- <  :cardWrapper="cardWrapper"></component> -->
    </div>

  </div>
</template>

<script>
import ModelCardSummary from '@/components/model/cards/ModelCardSummary.vue'
import ModelCardAsCard from '@/components/model/cards/ModelCardAsCard.vue'
import ModelCardAsPar from '@/components/model/cards/ModelCardAsPar.vue'

export default {
  name: 'model-card',

  components: {
    'app-model-card-summary': ModelCardSummary,
    'app-model-card-as-card': ModelCardAsCard,
    'app-model-card-as-par': ModelCardAsPar
  },

  props: {
    cardWrapperInit: {
      type: Object,
      default: null
    },
    inSection: {
      type: Object,
      default: null
    },
    type: {
      type: String,
      default: 'card'
    },
    forceUpdate: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      cardWrapper: null,
      showModal: false
    }
  },

  computed: {
    cardComponent () {
      switch (this.type) {
        case 'summary':
          return 'app-model-card-summary'

        case 'card':
          return 'app-model-card-as-card'

        case 'doc':
          return 'app-model-card-as-par'

        default:
          return 'app-model-card-as-card'
      }
    }
  },

  methods: {
    update () {
      this.axios.get('/1/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.cardWrapper = response.data.data
      })
    }
  },

  created () {
    this.cardWrapper = this.cardWrapperInit
  }
}
</script>

<style scoped>

</style>
