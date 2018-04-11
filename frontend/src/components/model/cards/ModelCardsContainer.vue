<template lang="html">
  <div class="cards-container-list">

    <div v-for="(cardWrapper, ix) in cardWrappers"
      :key="cardWrapper.id"
      :class="cardsContainerClasses"
      @dragover.prevent
      @drop.prevent="cardDroped(cardWrapper.id, $event)">

      <div class="card-container-in-list">
        <app-model-card
          :cardWrapperInit="cardWrapper"
          :inSection="inSection"
          :type="cardsType"
          @updateCards="$emit('updateCards')">
        </app-model-card>
      </div>
    </div>
  </div>
</template>

<script>
import ModelCard from '@/components/model/cards/ModelCard.vue'

export default {
  components: {
    'app-model-card': ModelCard
  },

  props: {
    cardWrappers: {
      type: Array,
      default: () => { return [] }
    },
    inSection: {
      type: Object,
      default: null
    },
    cardsType: {
      type: String,
      default: 'card'
    }
  },

  computed: {
    cardsContainerClasses () {
      return {
        'section-card-col-with-nav': this.cardsType === 'card' && (this.$store.state.support.expandNav && !this.$store.state.support.windowIsSmall),
        'section-card-col-no-nav': this.cardsType === 'card' && (!this.$store.state.support.expandNav || this.$store.state.support.windowIsSmall),
        'section-card-par': !this.cardsType !== 'card'
      }
    }
  },

  methods: {
    cardDroped (onCardWrapperId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      if (dragData.type === 'MOVE_CARD') {
        if (dragData.fromSectionId !== '' && !event.ctrlKey) {
          /* move from section */
          this.axios.put('/1/initiative/' + this.initiativeId +
            '/model/section/' + dragData.fromSectionId +
            '/moveCard/' + dragData.cardWrapperId, {}, {
            params: {
              onSectionId: this.section.id,
              onCardWrapperId: onCardWrapperId
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateModel')
          })
        } else {
          /* copy card without removing it */
          this.axios.put('/1/initiative/' + this.initiativeId +
            '/model/section/' + this.inSection.id +
            '/cardWrapper/' + dragData.cardWrapperId, {}, {
            params: {
              beforeCardWrapperId: onCardWrapperId
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateModel')
          })
        }
      }
    }
  }
}
</script>

<style scoped>

.card-container-in-list {
  margin-bottom: 12px;
}

.section-card-col-with-nav, .section-card-col-no-nav {
  margin-bottom: 20px;
  display: inline-block;
}

@media screen and (min-width: 1700px) {
  .section-card-col-no-nav {
    width: calc(20% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 1300px) and (max-width: 1699px) {
  .section-card-col-no-nav {
    width: calc(25% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 900px) and (max-width: 1299px) {
  .section-card-col-no-nav {
    width: calc(33% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 600px) and (max-width: 899px) {
  .section-card-col-no-nav {
    width: calc(50% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (max-width: 599px) {
  .section-card-col-no-nav {
    width: calc(100% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 1700px) {
  .section-card-col-with-nav {
    width: calc(25% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 1200px) and (max-width: 1699px) {
  .section-card-col-with-nav {
    width: calc(33% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 900px) and (max-width: 1199px) {
  .section-card-col-with-nav {
    width: calc(50% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (max-width: 899px) {
  .section-card-col-with-nav {
    width: calc(100% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

</style>
