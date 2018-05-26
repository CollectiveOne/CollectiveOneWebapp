<template lang="html">
  <div v-if="cardWrapper" class="card-base-div">

    <div class="card-content-container"
      :draggable="$store.state.support.triggerCardDraggingState"
      @dragstart="dragStart($event)">

      <component
        :is="cardComponent"
        :cardWrapper="cardWrapper"
        :inSection="inSection"
        :forceUpdate="forceUpdate"
        :inCardSelector="inCardSelector"
        @update="update()"
        @updateCards="$emit('updateCards')">
      </component>

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
    },
    inCardSelector: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      cardWrapper: null
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
    },
    dragStart (event) {
      var moveCardData = {
        type: 'MOVE_CARD',
        cardWrapperId: this.cardWrapper.id,
        fromSectionId: this.inSection.id
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveCardData))

      /* have an extended version of the dragged elements */
      var moveCardElement = {
        type: 'MOVE_CARD',
        cardWrapper: this.cardWrapper,
        fromSection: this.inSection
      }
      this.$store.commit('setDraggingElement', moveCardElement)
    }
  },

  created () {
    this.cardWrapper = this.cardWrapperInit
  }
}
</script>

<style>

.card-content-container,
.card-content-container h1,
.card-content-container h2,
.card-content-container h3,
.card-content-container h4,
.card-content-container h5
{
  font-family: 'Open Sans', sans-serif;
}

.card-content-container h1 {
  font-size: 19px;
}

.card-content-container h2 {
  font-size: 17px;
}

.card-content-container h3,
.card-content-container h4,
.card-content-container h5 {
  font-weight: bold;
  font-size: 15px;
}

.card-title {
  color: #15a5cc;
}

</style>


<style scoped>

</style>
