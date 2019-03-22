<template lang="html">
  <div class="model-section-elements">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :cardWrapperId="$route.params.cardId"
          :inSectionId="sectionId"
          :inSectionTitle="sectionTitle"
          @close="closeCardModal()"
          @update="resetCards()"
          @updateCards="resetCards()">
        </app-model-card-modal>
      </transition>

      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showNewCardModal"
          :isNew="true"
          :inSectionId="section.id"
          :inSectionTitle="section.title"
          @close="showNewCardModal = false"
          @updateCards="update()">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="model-section-elements-container">

      <app-section-control-row
        :orderType="orderType"
        :showMessages="showMessages"
        :showEvents="showEvents"
        @messagesContent="messagesContent()"
        @summaryView="summaryView()"
        @cardView="cardView()"
        @docView="docView()"
        @levelDown="levelDown()"
        @levelUp="levelUp()"
        @toggleInifinteLevels="toggleInifinteLevels()"
        @showPrivateClick="showPrivateClick()"
        @showSharedClick="showSharedClick()"
        @showCommonClick="showCommonClick()"
        @sectionOrder="sectionOrder()"
        @aggregatedOrder="aggregatedOrder()"
        @showMessages="showMessages = !showMessages"
        @showEvents="showEvents = !showEvents"
        @searchQueryUpdated="newCardQuery = $event"
        @updateQuery="updateQuery()"
        @cardsSortByUpdated="cardSortBy = $event"
        @downloadContent="downloadContent()"
        @enableDraggable="enableDraggable()">
      </app-section-control-row>

      <div class="elements-container"
        :class="{'large-padding': !windowIsSmall, 'small-padding': windowIsSmall}">

        <app-message-thread
          v-if="isMessagesContent"
          contextType="MODEL_SECTION"
          :contextElementId="currentSectionId"
          :showMessages="showMessages"
          :showEvents="showEvents"
          :levels="999">
        </app-message-thread>

        <app-model-section
          v-if="isCardsContent && isSectionsOrder"
          :section="section"
          :cardsType="cardsType"
          :showPrivate="showPrivate"
          :showShared="showShared"
          :showCommon="showCommon">
        </app-model-section>

        <app-model-cards-container
          v-if="isCardsContent && !isSectionsOrder"
          :cardWrappers="cardWrappers"
          :inSection="section"
          :cardsType="cardsType"
          :acceptDrop="false">
        </app-model-cards-container>

        <div v-if="!isSectionsOrder && thereAreMore" class="w3-row w3-center w3-margin-top w3-margin-bottom">
          <button @click="showMore()" class="w3-button app-button-light" type="button" name="button">show more</button>
        </div>

        <div v-if="loading" class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../assets/loading.gif" alt="">
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { sectionElementsMixin } from '@/components/model/SectionElementsMixin.js'
import MessageThread from '@/components/notifications/MessageThread'
import ModelSection from '@/components/model/ModelSection'
import SectionControlRow from '@/components/model/SectionControlRow'
import ModelCardsContainer from '@/components/model/cards/ModelCardsContainer'
import { getSortedCardWrappers, getSortedSubsections } from '@/lib/sortAlgorithm.js'
import { saveAs } from 'file-saver'

const sectionToMarkdown = function (section, level, showCommon, showShared, showPrivate) {
  var text = ''
  /* write section on text as reference */
  text += Array(level + 1).join('#') + ' '
  level = level < 5 ? level + 1 : level
  text += section.title + '\r\n'

  if (section.description !== '') {
    text += (section.description ? section.description : '') + '\r\n'
  }

  let sortedCardWrappers = getSortedCardWrappers(section, showCommon, showShared, showPrivate)

  for (let ix in sortedCardWrappers) {
    let cardWrapper = sortedCardWrappers[ix]
    if (cardWrapper.card.title !== '') {
      text += Array(level + 1).join('#') + ' '
      level = level < 5 ? level + 1 : level
      text += cardWrapper.card.title + '\r\n'
    }
    text += cardWrapper.card.text + '\r\n'
  }

  let sortedSubsections = getSortedSubsections(section, showCommon, showShared, showPrivate)

  for (let ix in sortedSubsections) {
    text += sectionToMarkdown(sortedSubsections[ix], level, showCommon, showShared, showPrivate)
  }

  return text
}

export default {

  mixins: [sectionElementsMixin],

  components: {
    'app-model-section': ModelSection,
    'app-model-cards-container': ModelCardsContainer,
    'app-message-thread': MessageThread,
    'app-section-control-row': SectionControlRow
  },

  data () {
    return {
      section: null,
      showCardModal: false,
      showNewCardModal: false,
      loading: false,
      sectionLoadedOnce: false,
      sectionLoadedOnceLevels: 0,
      orderType: 'sections',
      showMessages: true,
      showEvents: false,
      newCardQuery: '',
      cardQuery: '',
      cardSortBy: 'CREATION_DATE_DESC',
      page: 0,
      pageSize: 10,
      thereAreMore: true,
      cardWrappers: []
    }
  },

  computed: {
    sectionId () {
      return this.section !== null ? this.section.id : ''
    },
    sectionTitle () {
      return this.section !== null ? this.section.title : ''
    },
    currentSectionId () {
      return this.$route.params.sectionId
    },
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
    }
  },

  watch: {
    '$route.params.sectionId' () {
      console.log('update due to section id watch')
      this.sectionLoadedOnce = false
      this.section = null
      this.update()
    },
    '$route.name' () {
      console.log('checking card subroute due to route name watch')
      this.checkCardSubroute()
    },
    levels () {
      console.log('udpating due to levels watch')
      this.update()
    },
    cardSortBy () {
      if (!this.isSectionsOrder) {
        this.resetCards()
      } else {
        this.updateSection()
      }
    },
    '$store.state.support.triggerUpdateSectionCards' () {
      this.resetCards()
    },
    section () {
      console.log('checking card subroute due to section update')
      this.checkCardSubroute()
    }
  },

  methods: {
    enableDraggable () {
      this.$store.commit('triggerCardDraggingState')
    },
    messagesContent () {
      if (this.$route.name !== 'ModelSectionMessages') {
        this.$router.push({ name: 'ModelSectionMessages' })
      }
    },
    cardsContent () {
      if (this.$route.name !== 'ModelSectionCards') {
        this.$router.push({ name: 'ModelSectionCards' })
      }
    },
    sectionOrder () {
      this.orderType = 'sections'
      this.update()
    },
    aggregatedOrder () {
      this.orderType = 'aggregated'
      this.resetCards()
    },
    levelUp () {
      if (!this.infiniteLevels) {
        this.$store.commit('levelUp')
      }
    },
    levelDown () {
      if (!this.infiniteLevels) {
        this.$store.commit('levelDown')
      }
    },
    toggleInifinteLevels () {
      if (this.isSectionsOrder) {
        this.$store.commit('toggleInifinteLevels')
      }
    },
    summaryView () {
      if (!this.sectionLoadedOnce || (this.sectionLoadedOnceLevels !== this.levels)) {
        this.updateSection()
      }
      this.$store.commit('setCardsType', 'summary')
      if (this.$route.name !== 'ModelSectionCards') {
        this.$router.push({ name: 'ModelSectionCards' })
      }
    },
    cardView () {
      if (!this.sectionLoadedOnce || (this.sectionLoadedOnceLevels !== this.levels)) {
        this.updateSection()
      }
      this.$store.commit('setCardsType', 'card')
      if (this.$route.name !== 'ModelSectionCards') {
        this.$router.push({ name: 'ModelSectionCards' })
      }
    },
    docView () {
      if (!this.sectionLoadedOnce || (this.sectionLoadedOnceLevels !== this.levels)) {
        this.updateSection()
      }
      this.$store.commit('setCardsType', 'doc')
      if (this.$route.name !== 'ModelSectionCards') {
        this.$router.push({ name: 'ModelSectionCards' })
      }
    },
    showPrivateClick () {
      this.$store.commit('toggleShowPrivate')
    },
    showSharedClick () {
      this.$store.commit('toggleShowShared')
    },
    showCommonClick () {
      this.$store.commit('toggleShowCommon')
    },
    showAllClick () {
      this.$store.commit('showAllTypes')
    },
    showMore () {
      this.page = this.page + 1
      this.getMoreCards()
    },
    resetCards () {
      this.page = 0
      this.thereAreMore = true
      this.cardWrappers = []
      this.getMoreCards()
    },
    getMoreCards () {
      console.log('getting more cards')
      this.loading = true
      if (this.currentSectionId) {
        this.axios.get('/1/model/section/' + this.currentSectionId + '/cardWrappers/search',
          {
            params: {
              query: this.cardQuery,
              page: this.page,
              pageSize: this.pageSize,
              sortBy: this.cardSortBy,
              levels: 999
            }
          }).then((response) => {
          this.loading = false
          if (response.data.result === 'success') {
            if (response.data.data.content.length < this.pageSize) {
              this.thereAreMore = false
            }
            this.cardWrappers = this.cardWrappers.concat(response.data.data.content)
          }
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    updateSection () {
      this.loading = true
      if (this.currentSectionId) {
        this.axios.get('/1/model/section/' + this.currentSectionId, { params: { levels: this.levels } }).then((response) => {
          this.loading = false
          this.sectionLoadedOnce = true
          this.sectionLoadedOnceLevels = this.levels
          if (response.data.result === 'success') {
            this.section = response.data.data
            this.checkCardSubroute()
          }
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    updateQuery () {
      this.cardQuery = this.newCardQuery
      this.update()
    },
    resetQuery () {
      this.newCardQuery = ''
      this.updateQuery()
    },
    update () {
      if (!this.isMessagesContent) {
        if (this.isSectionsOrder) {
          this.updateSection()
        } else {
          this.resetCards()
        }
      }
    },
    checkCardSubroute () {
      this.showCardModal = false
      if (this.$route.name === 'ModelSectionCard') {
        if (this.section) {
          this.showCardModal = true
        } else {
          if (!this.sectionLoadedOnce) {
            this.updateSection()
          }
        }
      }
    },
    closeCardModal () {
      this.$router.replace({ name: 'ModelSectionCards' })
    },
    downloadContent () {
      let fileContent = new Blob(
        [sectionToMarkdown(this.section, 1, this.showCommon, this.showShared, this.showPrivate)],
        { type: 'text/plain;charset=utf-8' })
      saveAs(fileContent, this.section.title + '.md')
    }
  },

  created () {
    this.update()
    this.checkCardSubroute()
  }

}
</script>

<style scoped>

.model-section-elements {
  height: 100%;
}

.model-section-elements-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.elements-container {
  flex: 1 1 0;
  min-height: 0;
  overflow-y: auto;
}

.large-padding {
  padding: 12px 24px;
}

.small-padding {
  padding: 3px 6px;
}

</style>
