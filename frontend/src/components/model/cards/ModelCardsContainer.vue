<template lang="html">
  <div class="cards-container-list">

    <div v-for="(cardWrapper, ix) in cardWrappers"
      :key="cardWrapper.id"
      class="section-card"
      :class="cardsContainerClasses"
      @dragover.prevent="draggingOver($event, cardWrapper)"
      @drop.prevent="cardDroped(cardWrapper.id, $event)">

      <div v-if="isDraggingOver(cardWrapper) && draggingOverCardWrapperBefore" class="drop-div">
      </div>

      <div class="card-container-in-list">
        <app-model-card
          :cardWrapperInit="cardWrapper"
          :inSection="inSection"
          :type="cardsType"
          :cardRouteName="cardRouteName"
          :hideCardControls="hideCardControls"
          @edit="$emit('edit', cardWrapper)"
          @createNew="$emit('createNew', cardWrapper)"
          @updateCards="$emit('updateCards')">
        </app-model-card>
      </div>

      <div v-if="isDraggingOver(cardWrapper) && draggingOverCardWrapperAfter" class="drop-div">
      </div>

    </div>

    <div v-if="!hideCardControls && isLoggedAnEditor" :class="cardsContainerClasses" class=""
      @dragover.prevent="draggingOver()"
      @drop.prevent="cardDroped('', $event)">

      <div v-if="draggingOverCreateCard" class="drop-div">
      </div>

      <div v-if="!creatingNewCard" class="">
        <popper :append-to-body="true" trigger="click":options="popperOptions" class="">
          <app-drop-down-menu
            class="drop-menu"
            @createNew="creatingNewCard = true"
            @addExisting="addExisting()"
            :items="newCardItems">
          </app-drop-down-menu>

          <button slot="reference" class="w3-button app-button-light new-card-button">
            <i class="fa fa-plus" aria-hidden="true"></i>
          </button>
        </popper>
      </div>
      <div v-else class="card-container-in-list">
        <app-model-card-editor
          :isNew="true"
          :inSection="inSection"
          :type="cardsType"
          @edit="creatingNewCard = false"
          @updateCards="cardCreated()">
        </app-model-card-editor>
      </div>
    </div>

  </div>
</template>

<script>
import ModelCard from '@/components/model/cards/ModelCard.vue'
import ModelCardEditor from '@/components/model/cards/ModelCardEditor.vue'

export default {
  components: {
    'app-model-card': ModelCard,
    'app-model-card-editor': ModelCardEditor
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
      default: 'new'
    },
    acceptDrop: {
      type: Boolean,
      default: false
    },
    hideCardControls: {
      type: Boolean,
      default: false
    },
    cardRouteName: {
      type: String,
      default: 'ModelSectionCard'
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    cardsContainerClasses () {
      return {
        'section-card-col-with-nav': this.cardsType === 'card' && (this.$store.state.support.expandModelNav && !this.$store.state.support.windowIsSmall),
        'section-card-col-no-nav': this.cardsType === 'card' && (!this.$store.state.support.expandModelNav || this.$store.state.support.windowIsSmall),
        'section-card-par': !this.cardsType !== 'card'
      }
    },
    draggingElement () {
      return this.$store.state.support.draggingElement
    },
    lastCardWrapper () {
      let cardWrapper = {
        cardWrapper: this.cardWrappers[this.cardWrappers.length - 1]
      }
      return cardWrapper
    },
    newCardItems () {
      let menuItems = []

      menuItems.push({ text: 'create new', value: 'createNew', faIcon: 'fa-plus' })
      menuItems.push({ text: 'add existing', value: 'addExisting', faIcon: 'fa-plus' })

      return menuItems
    },
    popperOptions () {
      return {
        placement: 'right',
        modifiers: {
          preventOverflow: {
            enabled: false
          }
        }
      }
    }
  },

  data () {
    return {
      creatingNewCard: false,
      draggingOverCardWrapper: null,
      draggingOverCreateCard: false,
      draggingOverCardWrapperBefore: false,
      draggingOverCardWrapperAfter: false,
      resetIntervalId: 0
    }
  },

  methods: {
    cardCreated () {
      this.creatingNewCard = false
      this.$emit('updateCards')
    },
    addExisting () {
      this.$emit('create-card', { addExistingInit: true })
    },
    draggingOver (event, cardWrapper) {
      if (!this.acceptDrop) {
        return
      }

      if (cardWrapper == null) {
        this.draggingOverCreateCard = true
      } else {
        this.draggingOverCreateCard = false
      }

      if ((this.draggingElement.cardWrapper.scope === 'COMMON' && cardWrapper.scope === 'COMMON') ||
        (this.draggingElement.cardWrapper.scope !== 'COMMON') ||
        this.draggingElement == null) {
        this.draggingOverCardWrapper = cardWrapper
      } else {
        this.draggingOverCardWrapper = null
      }

      let ratioY = (event.screenY - event.currentTarget.getBoundingClientRect().top - 100) / event.currentTarget.offsetHeight
      if (ratioY < 0.5) {
        if (this.draggingElement.cardWrapper.scope !== 'COMMON' && cardWrapper.scope === 'COMMON') {
          /* Non common cards can only be placed after common cards */
          this.draggingOverCardWrapper = null
          this.draggingOverCardWrapperBefore = false
          this.draggingOverCardWrapperAfter = false
        } else {
          this.draggingOverCardWrapperBefore = true
          this.draggingOverCardWrapperAfter = false
        }
      } else {
        this.draggingOverCardWrapperBefore = false
        this.draggingOverCardWrapperAfter = true
      }
      /* make sure this resets even if dropped elsewhere */
      clearTimeout(this.resetIntervalId)
      this.resetIntervalId = setTimeout(() => {
        this.draggingOverCardWrapper = null
        this.draggingOverCreateCard = false
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
              onCardWrapperId: onCardWrapperId,
              isBefore: this.draggingOverCardWrapperBefore
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
              onCardWrapperId: onCardWrapperId,
              isBefore: this.draggingOverCardWrapperBefore,
              scope: dragData.scope
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateSectionCards')
          })
        }
      }
      this.$store.commit('setDraggingElement', null)
    }
  },

  watch: {
    'inSection.id' () {
      if (this.$route.query.createCard === this.inSection.id) {
        this.$emit('createNew', this.cardWrappers[this.cardWrappers.length - 1])
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

.section-card {
  /*position: relative;*/
}

.drop-div {
  height: 10px;
  background-color: #828282;
  border-radius: 5px;
  margin-bottom: 10px;
}

.create-card-div {
  margin-top: 20px;
  max-width: 350px;
}

.new-card-button {
  height: 30px;
  width: 30px;
  border-radius: 15px !important;
  padding: 0px !important;
  color: white !important;
  background-color: #cccccc !important;
  margin-left: 15px;
}

.drop-menu {
  width: 180px;
  text-align: left;
  font-size: 15px;
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
