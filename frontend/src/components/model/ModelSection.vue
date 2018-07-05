<template lang="html">

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
      <div v-if="!readOnly && section.inSections.length > 1" class="w3-row">
        <div class="w3-left w3-margin-right">
          <i>also in</i>
        </div>
        <div class="w3-left">
          <app-in-model-sections-tags
            :inModelSections="section.inSections"
            :hideSectionId="inSection.id">
          </app-in-model-sections-tags>
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
        :hideCardControls="readOnly"
        @updateCards="updateCards()"
        @createNew="createNew"
        @edit="edit"
        @create-card="showNewCardModal = true">
      </app-model-cards-container>
    </div>

    <div class="w3-row">
      <div v-for="subsection in sortedSubsections"
        :key="subsection.id"
        class="subsection-container">

        <app-model-section
          :section="subsection"
          :inSection="section"
          :cardsType="cardsType"
          :nestedIn="nestedIn.concat([section])"
          :cardRouteName="cardRouteName"
          :readOnly="readOnly"
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
import InModelSectionsTags from '@/components/model/InModelSectionsTags.vue'
import { getSortedCardWrappers, getSortedSubsections } from '@/lib/sortAlgorithm.js'

export default {
  name: 'app-model-section',

  components: {
    'app-model-section-header': ModelSectionHeader,
    'app-model-cards-container': ModelCardsContainer,
    'app-in-model-sections-tags': InModelSectionsTags
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
    },
    readOnly: {
      type: Boolean,
      default: false
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
      showNewCardModal: false,
      createNewCard: false,
      editCard: false,
      targetCard: {
        type: 'new',
        cardWrapper: null
      }
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
      }
      return false
    },
    sortedCards () {
      let allCardWrappers = getSortedCardWrappers(this.section, this.showCommon, this.showShared, this.showPrivate)

      //  new-card-editor for creating new cards
      if (this.createNewCard) {
        let index = allCardWrappers.findIndex(x => x.id === this.targetCard.cardWrapper.id)
        if (this.$store.state.support.createNewCardLocation === 'before') {
          index -= 1
        }
        allCardWrappers.splice(index + 1, 0, this.targetCard)
        this.createNewCard = false
      }
      //  new-card-editor for editing existing cards
      if (this.editCard) {
        let index = allCardWrappers.findIndex(x => x.id === this.targetCard.cardWrapper.id)
        this.targetCard.type = 'edit'
        allCardWrappers[index] = this.targetCard
        this.editCard = false
      }

      return allCardWrappers
    },
    sortedSubsections () {
      return getSortedSubsections(this.section, this.showCommon, this.showShared, this.showPrivate)
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
    edit (value) {
      this.targetCard.cardWrapper = value
      this.targetCard.type = 'edit'
      this.editCard = true
    },
    createNew (value) {
      this.targetCard.cardWrapper = value
      this.targetCard.type = 'newCard'
      this.createNewCard = true
    },
    updateCards () {
      console.log('updating cards')
      this.axios.get('/1/model/section/' + this.section.id + '/cardWrappers').then((response) => {
        if (response.data.result === 'success') {
          //  set new card parameters to default
          this.createNewCard = false
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
