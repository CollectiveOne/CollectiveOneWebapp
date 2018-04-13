<template lang="html">
  <div class="cards-container-list">

    <div v-for="(cardWrapper, ix) in cardWrappers"
      :key="cardWrapper.id"
      :class="cardsContainerClasses"
      @dragover.prevent="draggingOver(cardWrapper)"
      @drop.prevent="cardDroped(cardWrapper.id, $event)">

      <div v-if="isDraggingOver(cardWrapper)" class="drop-div">
      </div>

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
    },
    acceptDrop: {
      type: Boolean,
      default: false
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

  data () {
    return {
      draggingOverCardWrapper: null,
      resetIntervalId: 0
    }
  },

  methods: {
    draggingOver (cardWrapper) {
      if (!this.acceptDrop) {
        return
      }

      this.draggingOverCardWrapper = cardWrapper

      /* make sure this resets even if dropped elsewhere */
      clearTimeout(this.resetIntervalId)
      this.resetIntervalId = setTimeout(() => {
        this.draggingOverCardWrapper = null
      }, 500)
    },
    isDraggingOver (cardWrapper) {
      if (!this.acceptDrop) {
        return false
      }
      if (this.draggingOverCardWrapper) {
        return (this.draggingOverCardWrapper.id === cardWrapper.id)
      }
      return false
    },
    cardDroped (onCardWrapperId, event) {
      if (!this.acceptDrop) {
        return
      }

      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      if (dragData.type === 'MOVE_CARD') {
        var moveFlag = false
        if (!event.ctrlKey && dragData.fromSectionId !== '') {
          moveFlag = true
        } else {
          moveFlag = false
        }

        if (moveFlag) {
          /* move from section */
          console.log('moving card ' + dragData.cardWrapperId +
            ' from ' + dragData.fromSectionId +
            ' to ' + this.inSection.id)

          this.axios.put('/1/model/section/' + dragData.fromSectionId +
            '/moveCard/' + dragData.cardWrapperId, {}, {
            params: {
              onSectionId: this.inSection.id,
              onCardWrapperId: onCardWrapperId
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionCards')
          })
        } else {
          /* copy card without removing it */
          console.log('adding card ' + dragData.cardWrapperId +
            ' to ' + this.inSection.id)
          this.axios.put('/1/model/section/' + this.inSection.id +
            '/cardWrapper/' + dragData.cardWrapperId, {}, {
            params: {
              beforeCardWrapperId: onCardWrapperId
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionCards')
          })
        }
      }
      this.$store.commit('setDraggingElement', null)
    }
  },

  created () {
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

.drop-div {
  height: 10px;
  background-color: #828282;
  border-radius: 5px;
  margin-bottom: 10px;
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
