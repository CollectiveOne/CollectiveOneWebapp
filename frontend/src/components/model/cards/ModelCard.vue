<template lang="html">
  <div v-if="cardWrapper" class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showModal"
          :isNew="false"
          :cardWrapperId="cardWrapper.id"
          :inSectionId="inSectionId"
          :inSectionTitle="inSectionTitle"
          @close="showModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="card-content-container">
      <app-model-card-as-card :cardWrapper="cardWrapper"></app-model-card-as-card>
      <!-- <component :is="cardComponent" :cardWrapper="cardWrapper"></component> -->
    </div>

  </div>
</template>

<script>
import ModelCardSummary from '@/components/model/cards/ModelCardSummary.vue'
import ModelCardAsCard from '@/components/model/cards/ModelCardAsCard.vue'
import ModelCardAsPar from '@/components/model/cards/ModelCardAsPar.vue'

import smoothHeight from 'vue-smooth-height'

export default {
  name: 'model-card',

  components: {
    'app-model-card-summary': ModelCardSummary,
    'app-model-card-as-card': ModelCardAsCard,
    'app-model-card-as-par': ModelCardAsPar
  },

  mixins: [ smoothHeight ],

  props: {
    cardWrapperInit: {
      type: Object,
      default: null
    },
    inSectionId: {
      type: String,
      default: ''
    },
    inSectionTitle: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'card'
    },
    floating: {
      type: Boolean,
      default: false
    },
    enableExpand: {
      type: Boolean,
      default: true
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
    cardClicked () {
      this.$router.push({name: 'ModelSectionCard', params: { cardId: this.cardWrapper.id }, query: {levels: this.$route.query.levels ? this.$route.query.levels : 1}})
    },
    showThisTag (inSection) {
      /* hide current section tag */
      if (this.floating) {
        return true
      } else {
        return inSection.id !== this.inSectionId
      }
    },
    textTooLong () {
      if (!this.$refs.cardText) {
        return false
      }
      if (this.$refs.cardText.scrollHeight > 250) {
        return true
      } else {
        return this.$refs.cardText.clientHeight < this.$refs.cardText.scrollHeight
      }
    },
    toggleLike () {
      this.axios.put('/1/model/card/' + this.cardWrapper.id + '/like',
        {}, {
          params: {
            likeStatus: !this.cardWrapper.userLiked
          }
        }).then((response) => {
          this.update()
        })
    }
  },

  created () {
    this.cardWrapper = this.cardWrapperInit
  },

  mounted () {
    this.$registerElement({
      el: this.$refs.cardText
    })

    this.$registerElement({
      el: this.$refs.cardContent
    })
  }
}
</script>

<style scoped>

.card-container {
}

.card-container-padded {
  padding: 8px 6px 12px 12px !important;
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

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  max-height: 150px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
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

.bottom-row {
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

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

</style>
