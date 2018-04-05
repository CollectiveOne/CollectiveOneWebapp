<template lang="html">

  <div v-if="section" class="section-container" ref="sectionContainer">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :cardWrapperId="$route.params.cardId"
          :inSectionId="section.id"
          :inSectionTitle="section.Title"
          @close="closeCardModal()">
        </app-model-card-modal>
      </transition>
    </div>

    <div v-if="nestedIn.length > 0 && sortedCards.length > 0"
      class="w3-row blue-color title-row">
      <div v-for="parent in nestedIn.slice(1, nestedIn.length)"
        class="w3-left">
        {{ parent.title }} <i class="fa fa-chevron-right" aria-hidden="true"></i>
      </div>
      <div class="w3-left">
        {{ section.title }}
      </div>
    </div>

    <div class="w3-row">
      <div v-for="(cardWrapper, ix) in sortedCards"
        :key="cardWrapper.id"
        :class="cardsContainerClasses"
        @dragover.prevent
        @drop.prevent="cardDroped(cardWrapper.id, $event)">

        <div class="">
          <app-model-card
            :cardWrapperInit="cardWrapper"
            :inSection="section"
            :cardEffect="cardsAsCards">
          </app-model-card>
        </div>
      </div>
    </div>

    <div class="w3-row">
      <div v-for="subsection in section.subsections"
        :key="subsection.id"
        class="subsection-container"
        @dragover.prevent
        @drop.prevent="subsectionDroped(subsection.id, $event)">

        <app-model-section
          :section="subsection"
          :inElementId="section.id"
          :inElementTitle="section.title"
          dragType="MOVE_SUBSECTION"
          :cardsAsCards="cardsAsCards"
          :nestedIn="nestedIn.concat([section])">
        </app-model-section>
      </div>
    </div>
  </div>

</template>

<script>
import ModelSectionHeader from '@/components/model/ModelSectionHeader.vue'
import ModelCard from '@/components/model/cards/ModelCard.vue'

export default {
  name: 'app-model-section',

  components: {
    'app-model-section-header': ModelSectionHeader,
    'app-model-card': ModelCard
  },

  props: {
    section: {
      type: Object,
      default: null
    },
    nestedIn: {
      type: Array,
      default: () => { return [] }
    },
    inElementId: {
      type: String,
      default: ''
    },
    inElementTitle: {
      type: String,
      default: ''
    },
    dragType: {
      type: String
    },
    floating: {
      type: Boolean,
      default: false
    },
    cardsAsCards: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      loaded: false,
      showSubsections: true,
      showCards: true,
      expanded: true,
      showCardModal: false,
      showCardId: '',
      expandSubSubsecInit: false
    }
  },

  watch: {
    '$route' () {
      this.checkCardSubroute()
    }
  },

  computed: {
    sortedCards () {
      if (!this.sortByLikes) {
        return JSON.parse(JSON.stringify(this.section.cardsWrappers))
      } else {
        var sortedArray = JSON.parse(JSON.stringify(this.section.cardsWrappers))
        return sortedArray.sort((cardA, cardB) => {
          return cardB.nLikes - cardA.nLikes
        })
      }
    },
    nCardWrappers () {
      if (this.section) {
        if (this.section.cardsWrappers) {
          return this.section.cardsWrappers.length
        } else {
          return 0
        }
      } else {
        return 0
      }
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    cardsContainerClasses () {
      return {
        'section-card-col-with-nav': this.cardsAsCards && (this.$store.state.support.expandNav && !this.$store.state.support.windowIsSmall),
        'section-card-col-no-nav': this.cardsAsCards && (!this.$store.state.support.expandNav || this.$store.state.support.windowIsSmall),
        'section-card-par': !this.cardsAsCards
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
          /* add existing card */
          this.axios.put('/1/initiative/' + this.initiativeId +
            '/model/section/' + this.section.id +
            '/cardWrapper/' + dragData.cardWrapperId, {}, {
            params: {
              beforeCardWrapperId: onCardWrapperId
            }
          }).then((response) => {
            this.$store.commit('triggerUpdateModel')
          })
        }
      }
    },
    dragStart (event) {
      var moveSectionData = {
        type: this.dragType,
        sectionId: this.section.id,
        fromElementId: this.inElementId
      }
      event.dataTransfer.setData('text/plain', JSON.stringify(moveSectionData))
    },
    subsectionDroped (onSubsectionId, event) {
      var dragData = JSON.parse(event.dataTransfer.getData('text/plain'))
      var url = ''

      url = '/1/initiative/' + this.initiativeId +
        '/model/section/' + dragData.fromElementId +
        '/moveSubsection/' + dragData.sectionId

      this.axios.put(url, {}, {
        params: {
          onSectionId: this.section.id,
          onSubsectionId: onSubsectionId
        }
      }).then((response) => {
        this.$store.commit('triggerUpdateModel')
      })
    },
    newCardFromBar () {
      this.showCards = true
      this.$emit('new-card')
    },
    newSubsectionFromBar () {
      this.showSubsections = true
      this.$emit('new-subsection')
    },
    checkCardSubroute () {
      this.showCardModal = false
      if (this.$route.name === 'ModelSectionCard') {
        if (this.$route.params.sectionId === this.section.id) {
          this.showCardModal = true
        }
      }
    },
    closeCardModal () {
      this.$router.replace({name: 'ModelSectionCards', query: {levels: this.$route.query.levels ? this.$route.query.levels : 1}})
    }
  },

  created () {
    this.checkCardSubroute()
  }
}
</script>

<style scoped>

.section-container {
}

.cards-container {
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

.section-card-par {
  margin-bottom: 16px;
}

.title-row {
  margin-top: 12px;
  margin-bottom: 12px;
}

.title-row .fa {
  font-size: 12px;
}

.title-row > div {
  margin-right: 5px;
}

.subsections-container {
}

.subsection-container {
}

</style>
