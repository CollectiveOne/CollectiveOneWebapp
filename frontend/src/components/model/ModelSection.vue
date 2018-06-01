cd w<template lang="html">

  <div v-if="section"
    class="section-container" ref="sectionContainer" :id="section.id">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showNewCardModal"
          :isNew="true"
          :inSectionId="section.id"
          :inSectionTitle="section.title"
          @close="showNewCardModal = false"
          @updateCards="updateCards()">
        </app-model-card-modal>
      </transition>
    </div>

    <div v-if="(nestedIn.length > 0 && section.subElementsLoaded) || showThisTitle"
      class="w3-row title-row"
      :class="{'card-container-doc': cardsType === 'doc'}">
      <div class="w3-row blue-color title-text">
        <div v-for="parent in nestedIn.slice(1, nestedIn.length)"
          class="w3-left">
          <router-link v-if="addSectionLinks" class="w3-left" :to="{ name: 'ModelSectionContent', params: {'sectionId': parent.id } }">
             {{ parent.title }}
          </router-link>
          <div v-else class="w3-left">
            <span v-html="headerOpenTag + parent.title + headerCloseTag"></span>
          </div>
          <div class="w3-left chevron-container">
            <span v-if="!isDocView" v-html="faRawHtml"></span>
            <span v-else v-html="headerOpenTag + faRawHtml + headerCloseTag"></span>
          </div>

        </div>
        <div class="w3-left">
          <router-link v-if="addSectionLinks" :to="{ name: 'ModelSectionContent', params: {'sectionId': section.id } }">
            <b>{{ section.title }}</b>
          </router-link>
          <div v-else class="">
            <span v-html="headerOpenTag + '<b>' +  section.title + '</b>' + headerCloseTag"></span>
          </div>
        </div>
      </div>
      <div v-if="hasDescription" class="w3-row description-text light-grey">
        <vue-markdown class="marked-text" :source="section.description"></vue-markdown>
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
        :showNewCardButton="cardsType !== 'doc'"
        @updateCards="updateCards()"
        @create-card="showNewCardModal = true">
      </app-model-cards-container>
    </div>

    <div class="w3-row">
      <div v-for="subsection in section.subsections"
        :key="subsection.id"
        class="subsection-container">

        <app-model-section
          :section="subsection"
          :inSection="section"
          :cardsType="cardsType"
          :nestedIn="nestedIn.concat([section])"
          :cardRouteName="cardRouteName"
          :hideCardControls="hideCardControls"
          :showPrivate="showPrivate"
          :showShared="showShared"
          :showCommon="showCommon"
          @updateCards="updateCards()">
        </app-model-section>
      </div>
    </div>
  </div>

</template>

<script>
import ModelSectionHeader from '@/components/model/ModelSectionHeader.vue'
import ModelCardsContainer from '@/components/model/cards/ModelCardsContainer.vue'

/* this function will append the cards of the toAddList to the baseList
respecting the the onCardWrapperId */
const getConnectedList = function (id, toAddList) {

  let firstElement = toAddList.filter((card) => card.id === id)
  let connectedList = [ firstElement ]

  let connectedToThis = toAddList.filter((card) => card.onCardWrapperId === firstElement.id)

  for (let ix = 0; ix < connectedToThis.length; ix++) {
    /* recursive call to all the cards that links to this one */
    connectedList = connectedList.concat(getConnectedList(connectedToThis[ix].id, toAddList))
  }

  return conectedList
}

const appendCardsToBase = function (baseList, toAddList) {

  let relativeToBase = toAddList.filter((cardToAdd) => {
    return baseList.findIndex((cardInBase) => cardToAdd.onCardWrapperId === cardInBase.id) !== -1
  })

  /* add those relative to base list  */
  relativeToBase.forEach((cardRelativeToBase) => {

    /* get entire list of cards connected to that card */
    let connectedCards = getConnectedList(cardRelativeToBase.id, toAddList)

    /* add all the connected array at the index  */
    let ixOnCard = baseList.findIndex((cardInBase) => cardInBase.id === cardRelativeToBase.id)
    baseList.slice(0, ixOnCard).concat(cardRelativeToBase.reverse()).concat(baseList.slice(ixOnCard))

    toAddList.
  })

  /**/
}

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
    inSection: {
      type: Object,
      default: null
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
    },
    showPrivate: {
      type: Boolean,
      default: true
    },
    showShared: {
      type: Boolean,
      default: true
    },
    showCommon: {
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
      showCardId: '',
      expandSubSubsecInit: false,
      subscription: null,
      showNewCardModal: false
    }
  },

  computed: {
    nestedLevel () {
      return this.nestedIn.length + 1
    },
    isDocView () {
      return this.cardsType === 'doc'
    },
    isReadFriendly () {
      return this.cardRouteName === 'ModelSectionReadCard'
    },
    addSectionLinks () {
      return !this.isReadFriendly
    },
    headerOpenTag () {
      if (this.isDocView) {
        return '<h' + (this.nestedLevel < 6 ? this.nestedLevel : 6) + '>'
      } else {
        return ''
      }
    },
    headerCloseTag () {
      if (this.cardsType !== 'doc') {
        return ''
      }

      return '</h' + (this.nestedLevel < 6 ? this.nestedLevel : 6) + '>'
    },
    hasDescription () {
      if (this.section.description) {
        return this.section.description !== ''
      }``
      return false
    },
    sortedCards () {
      let allCardWrappers = []

      if (this.showCommon) {
        allCardWrappers = this.section.cardsWrappersCommon.slice()
      }

      if (this.showPrivate) {

        getConnectedList(allCardWrappers, this.section.cardsWrappersPrivate.slice())
      }

      if (this.showShared) {
        for (let ix in this.section.cardsWrappersShared) {
          let cardWrapperPrivate = this.section.cardsWrappersShared[ix]
          let ixFound = allCardWrappers.findIndex((e) => {
            return e.id === cardWrapperPrivate.onCardWrapperId
          })
          if (ixFound !== -1) {
            let ixInsert = cardWrapperPrivate.isBefore ? ixFound : ixFound + 1
            allCardWrappers.splice(ixInsert, 0, cardWrapperPrivate)
          } else {
            allCardWrappers.push(cardWrapperPrivate)
          }
        }
      }

      return allCardWrappers
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    faRawHtml () {
      return '<i class="fa fa-chevron-right" aria-hidden="true"></i>'
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
      console.log('updating cards')
      this.axios.get('/1/model/section/' + this.section.id + '/cardWrappers').then((response) => {
        if (response.data.result === 'success') {
          this.section.cardsWrappersCommon = response.data.data.cardsWrappersCommon
          this.section.cardsWrappersPrivate = response.data.data.cardsWrappersPrivate
          this.section.cardsWrappersShared = response.data.data.cardsWrappersShared
        }
      })
    },
    subscribeSocket () {
      if (this.subscription === null) {
        this.subscription = this.$store.dispatch('subscribe', {
          url: '/channel/activity/model/section/' + this.section.id,
          onMessage: (tick) => {
            var message = tick.body
            console.log('mesage on section: ' + this.section.id + ' - MSG: ' + message)
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

<style>

.card-container-doc,
.card-container-doc h1,
.card-container-doc h2,
.card-container-doc h3,
.card-container-doc h4,
.card-container-doc h5
{
font-family: 'Merriweather', serif;
}

.card-container-doc {
  font-size: 19px;
  line-height: 32px;
}

.card-container-doc h1 {
  font-size: 32px;
}

.card-container-doc h2 {
  font-size: 26px;
}

.card-container-doc h3,
.card-container-doc h4,
.card-container-doc h5 {
  /*font-weight: bold;*/
  font-size: 22px;
}

</style>

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

.title-text > * {
  display: inline-block;
}

.title-text .w3-left > * {
  display: inline-block;
  vertical-align: baseline;
}

.chevron-container {
  padding: 0px 8px;
  font-size: 15px;
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
