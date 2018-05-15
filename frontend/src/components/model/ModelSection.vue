<template lang="html">

  <div v-if="section" class="section-container" ref="sectionContainer" :id="section.id">

    <div v-if="(nestedIn.length > 0 && section.subElementsLoaded) || showThisTitle"
      class="w3-row title-row">
      <div class="w3-row blue-color title-text">
        <div v-for="parent in nestedIn.slice(1, nestedIn.length)"
          class="w3-left">
          {{ parent.title }} <i class="fa fa-chevron-right" aria-hidden="true"></i>
        </div>
        <div class="w3-left">
          <b>{{ section.title }}</b>
        </div>
      </div>
      <div v-if="section.description !== ''" class="w3-row description-text light-grey">
        {{ section.description }}
      </div>

    </div>

    <div class="w3-row">
      <app-model-cards-container
        :cardWrappers="sortedCards"
        :cardsType="cardsType"
        :inSection="section"
        :acceptDrop="true"
        :cardRouteName="cardRouteName"
        :hideCardControls="hideCardControls"
        @updateCards="updateCards()">
      </app-model-cards-container>
    </div>

    <div class="w3-row">
      <div v-for="subsection in section.subsections"
        :key="subsection.id"
        class="subsection-container">

        <app-model-section
          :section="subsection"
          :inElementId="section.id"
          :inElementTitle="section.title"
          :cardsType="cardsType"
          :nestedIn="nestedIn.concat([section])"
          :cardRouteName="cardRouteName"
          :hideCardControls="hideCardControls"
          @updateCards="updateCards()">
        </app-model-section>
      </div>
    </div>
  </div>

</template>

<script>
import ModelSectionHeader from '@/components/model/ModelSectionHeader.vue'
import ModelCardsContainer from '@/components/model/cards/ModelCardsContainer.vue'

export default {
  name: 'app-model-section',

  components: {
    'app-model-section-header': ModelSectionHeader,
    'app-model-cards-container': ModelCardsContainer
  },

  props: {
    section: {
      type: Object,
      default: null
    },
    showThisTitle: {
      type: Boolean,
      default: false
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
    cardsType: {
      type: String,
      default: 'card'
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

  data () {
    return {
      loaded: false,
      showSubsections: true,
      showCards: true,
      expanded: true,
      showCardId: '',
      expandSubSubsecInit: false,
      subscription: null
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
    }
  },

  watch: {
    '$store.state.support.triggerUpdateSectionCards' () {
      this.updateCards()
    },
    section () {
      this.subscribeSocket()
    }
  },

  methods: {
    updateCards () {
      this.axios.get('/1/model/section/' + this.section.id + '/cardWrappers').then((response) => {
        if (response.data.result === 'success') {
          this.section.cardsWrappers = response.data.data
        }
      })
    },
    subscribeSocket () {
      if (this.subscription === null) {
        this.subscription = this.$store.dispatch('subscribe', {
          url: '/channel/activity/model/section/' + this.section.id,
          onMessage: (tick) => {
            var message = tick.body
            if (message === 'UPDATE') {
              this.updateCards()
            }
          }
        })
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
    newCardFromBar () {
      this.showCards = true
      this.$emit('new-card')
    },
    newSubsectionFromBar () {
      this.showSubsections = true
      this.$emit('new-subsection')
    },
    handleSocket () {
      if (this.section) {
        console.log(this.section.id)
        let url = '/channel/activity/model/section/' + this.section.id
        // console.log('url is' + url)
        this.subscription = this.$store.dispatch('subscribe', {
          url: url,
          onMessage: (tick) => {
            var message = tick.body
            console.log('sagar')
            if (message === 'UPDATE') {
              this.updateCards()
            }
          }
        })
        setInterval(() => {
          console.log(this.subscription.id + 'sagar')
        }, 1000)
      }
    }
  },

  created () {
    if (this.section) {
      this.subscribeSocket()
    }
  },

  beforeDestroy () {
    if (this.subscription) {
      this.$store.dispatch('unsubscribe', this.subscription)
    }
  }
}
</script>

<style scoped>

.section-container {
  font-family: 'Open Sans', sans-serif;
}

.title-row {
  margin-top: 12px;
  margin-bottom: 12px;
}

.title-text {
  font-size: 17px;
}

.title-row .fa {
  font-size: 12px;
}

.title-row > div {
  margin-right: 5px;
}

.description-text {
  padding: 3px 12px;
  margin-top: 6px;
  margin-bottom: 6px;
}

</style>
