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
          @updateCards="resetCards()">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="slider-container">
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
      <div class="w3-row controls-row small-scroll">

        <div class="control-group">
          <div @click="messagesContent()" class="w3-left control-btn" :class="{'control-btn-selected': isMessagesContent}">
            <img src="./../../assets/chat-icon.svg" alt="">
          </div>
          <div @click="summaryView()" class="w3-left control-btn" :class="{'control-btn-selected': isSummary}">
            <img src="./../../assets/rows-icon.svg" alt="">
          </div>
          <div @click="cardView()" class="w3-left control-btn" :class="{'control-btn-selected': isCard}">
            <img src="./../../assets/cards-icon.svg" alt="">
          </div>
          <div @click="docView()" class="w3-left control-btn" :class="{'control-btn-selected': isDoc}">
            <img src="./../../assets/doc-icon.svg" alt="">
          </div>
        </div>

        <div class="control-group noselect">
          <div class="w3-left zoom-controls">
            <div class="w3-left zoom-controls-enabled">
              <div @click="levelDown()" class="w3-left cursor-pointer arrow-div">
                <img src="./../../assets/zoom-in-icon.svg" alt="">
              </div>
              <div class="w3-left number-div">
                {{ levels !== 999 ? levels : '&#x221e;' }}
              </div>
              <div @click="levelUp()" class="w3-left cursor-pointer arrow-div">
                <img src="./../../assets/zoom-out-icon.svg" alt="">
              </div>
              <div v-if="infiniteLevels" class="zoom-controls-cover">
              </div>
            </div>
            <div @click="levelsInfinite()" class="w3-left cursor-pointer arrow-div w3-border-left" :class="{'control-btn-selected': infiniteLevels}">
              <img src="./../../assets/infinite-icon.svg" alt="">
            </div>
          </div>
        </div>

        <div v-if="isCardsContent" class="control-group">
          <div class="">
            <div @click="sectionOrder()" class="w3-left control-btn" :class="{'control-btn-selected': isSectionsOrder}">
              <img src="./../../assets/network-icon.svg" alt="">
            </div>
            <div @click="aggregatedOrder()" class="w3-left control-btn" :class="{'control-btn-selected': !isSectionsOrder}">
              <img src="./../../assets/search-icon.svg" alt="">
            </div>
          </div>
        </div>

        <div v-if="!isCardsContent" class="control-group">
          <div @click="isOnlyMessages = true" class="w3-left control-btn" :class="{'control-btn-selected': isOnlyMessages}">
            <img src="./../../assets/chat-icon-2.svg" alt="">
          </div>
          <div @click="isOnlyMessages = false" class="w3-left control-btn" :class="{'control-btn-selected': !isOnlyMessages}">
            <img src="./../../assets/all-events-icon.svg" alt="">
          </div>
        </div>

        <div class="control-group slider-container">
          <transition name="slideRightLeft">
            <div v-if="isCardsContent && !isSectionsOrder" class="">
              <div class="">
                <div class="">
                  <input ref="inputQuery" v-model="newCardQuery" class="w3-input"
                    type="text" name="" value="" placeholder="search">
                </div>
                <div @click="updateQuery()" class="control-btn control-btn-selected">
                  <i class="fa fa-refresh" aria-hidden="true"></i>
                </div>
              </div>

              <div class="w3-margin-left">
                <select v-model="cardSortBy" class="w3-input">
                  <option value="CREATION_DATE_DESC">Last Created</option>
                  <option value="EDITION_DATE_DESC">Last Edited</option>
                  <option value="CREATOR">Author</option>
                </select>
              </div>
            </div>
          </transition>
        </div>

        <div v-if="isCardsContent && isSectionsOrder" class="control-group">
          <div class="">
            <div @click="downloadContent()" class="w3-left control-btn">
              <img src="./../../assets/download-icon.svg" alt="">
            </div>
          </div>
        </div>

        <div v-if="false" class="control-group text-details">
          <span v-if="isCardsContent">
            <span v-if="!isSectionsOrder">
              you are
              <span v-if="cardQuery !== ''" class="">
                searching by
                <span class="cursor-pointer" @click="resetQuery()">
                  <i><b>"{{ cardQuery }}"</b></i> <i class="fa fa-times-circle" aria-hidden="true"></i>
                </span>
              </span>
              <span v-else class="">
                searching all cards
              </span>
              under the
              <br>"{{ sectionTitle }}" section.
            </span>
            <span v-else>
              you are seeing all cards up to {{ levels }} levels under the <br>"{{ sectionTitle }}" section and respecting their in-section order.
            </span>
          </span>
          <span v-else>
            you are seeing
            <span v-if="isOnlyMessages">all messages</span>
            <span v-else>all events</span>
            up to {{ levels }} levels under the <br>"{{ sectionTitle }}" section.
          </span>

        </div>
      </div>

      <div class="elements-container">
        <app-message-thread
          v-if="isMessagesContent"
          contextType="MODEL_SECTION"
          :contextElementId="currentSectionId"
          :onlyMessages="isOnlyMessages"
          :levels="levels">
        </app-message-thread>

        <app-model-section
          v-if="isCardsContent && isSectionsOrder"
          :section="section"
          :cardsType="cardsType">
        </app-model-section>

        <div v-if="isCardsContent && isSectionsOrder && !sectionHasContent && !loading && section" class="w3-row w3-center">
          <div class="w3-row">
            <i>no cards have been created in "{{ section.title }}"</i>
          </div>
          <div @click="showNewCardModal = true" class="control-btn w3-row w3-margin-top">create one</div>
          <div v-if="levels === 1" class="w3-row w3-margin-top">or</div>
          <div v-if="levels === 1" @click="levelUp()" class="control-btn w3-row w3-margin-top">look one level further</div>
        </div>

        <app-model-cards-container
          v-if="isCardsContent && !isSectionsOrder"
          :cardWrappers="cardWrappers"
          :inSection="section"
          :cardsType="cardsType"
          :acceptDrop="false">
        </app-model-cards-container>

        <div v-if="!isSectionsOrder && thereAreMore" class="w3-row w3-center">
          <button @click="showMore()"="w3-button app-button-light" type="button" name="button">show more</button>
        </div>

        <div v-if="loading" class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../assets/loading.gif" alt="">
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import MessageThread from '@/components/notifications/MessageThread'
import ModelSection from '@/components/model/ModelSection'
import ModelCardsContainer from '@/components/model/cards/ModelCardsContainer'

const sectionToMarkdown = function (section, level) {
  var text = ''
  /* write section on text as reference */
  text += Array(level + 1).join('#') + ' '
  level = level < 5 ? level + 1 : level
  text += section.title + '\r\n'

  if (section.description !== '') {
    text += section.description + '\r\n'
  }

  for (let ix in section.cardsWrappers) {
    let cardWrapper = section.cardsWrappers[ix]
    if (cardWrapper.card.title !== '') {
      text += Array(level + 1).join('#') + ' '
      level = level < 5 ? level + 1 : level
      text += cardWrapper.card.title + '\r\n'
    }
    text += cardWrapper.card.text + '\r\n'
  }

  for (let ix in section.subsections) {
    text += sectionToMarkdown(section.subsections[ix], level)
  }

  return text
}

const sectionHasContent = function (section) {
  if (section.cardsWrappers.length > 0) {
    return true
  } else {
    if (section.subsections.length > 0) {
      return true
    } else {
      /* recursive call  */
      for (let ix in section.subsections) {
        if (sectionHasContent(section.subsections[ix])) {
          return true
        }
      }
    }
  }
  return false
}

export default {
  components: {
    'app-model-section': ModelSection,
    'app-model-cards-container': ModelCardsContainer,
    'app-message-thread': MessageThread
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
      isOnlyMessages: false,
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
    sectionHasContent () {
      if (this.section) {
        return sectionHasContent(this.section)
      }
      return false
    },
    isMessagesContent () {
      return this.$route.name === 'ModelSectionMessages'
    },
    isCardsContent () {
      return this.$route.name === 'ModelSectionCards' ||
        this.$route.name === 'ModelSectionCard'
    },
    levels () {
      return this.$route.query.levels ? parseInt(this.$route.query.levels) : 1
    },
    infiniteLevels () {
      return this.levels === 999
    },
    cardsType () {
      return this.$route.query.cardsType ? this.$route.query.cardsType : 'card'
    },
    isSummary () {
      return this.cardsType === 'summary' && this.isCardsContent
    },
    isCard () {
      return this.cardsType === 'card' && this.isCardsContent
    },
    isDoc () {
      return this.cardsType === 'doc' && this.isCardsContent
    },
    isSectionsOrder () {
      return this.orderType === 'sections'
    }
  },

  watch: {
    '$route.params.sectionId' () {
      this.update()
    },
    '$route.name' () {
      this.checkCardSubroute()
    },
    levels () {
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
    }
  },

  methods: {
    messagesContent () {
      if (this.$route.name !== 'ModelSectionMessages') {
        this.$router.push({name: 'ModelSectionMessages'})
      }
    },
    cardsContent () {
      if (this.$route.name !== 'ModelSectionCards') {
        this.$router.push({name: 'ModelSectionCards'})
      }
    },
    sectionOrder () {
      this.orderType = 'sections'
    },
    aggregatedOrder () {
      this.resetCards()
      this.orderType = 'aggregated'
    },
    levelUp () {
      if (!this.infiniteLevels) {
        this.$router.replace({name: this.$route.name, query: {levels: this.levels + 1}})
      }
    },
    levelDown () {
      if (!this.infiniteLevels) {
        if (this.levels > 1) {
          this.$router.replace({name: this.$route.name, query: {levels: this.levels - 1}})
        }
      }
    },
    levelsInfinite () {
      if (this.infiniteLevels) {
        this.$router.replace({name: this.$route.name, query: {levels: 1}})
      } else {
        this.$router.replace({name: this.$route.name, query: {levels: 999}})
      }
    },
    summaryView () {
      if (!this.sectionLoadedOnce || (this.sectionLoadedOnceLevels !== this.levels)) {
        this.updateSection()
      }
      if (this.$route.query.cardsType !== 'summary' || this.$route.name !== 'ModelSectionCards') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'summary'}})
      }
    },
    cardView () {
      if (!this.sectionLoadedOnce || (this.sectionLoadedOnceLevels !== this.levels)) {
        this.updateSection()
      }
      if (this.$route.query.cardsType !== 'card' || this.$route.name !== 'ModelSectionCards') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'card'}})
      }
    },
    docView () {
      if (!this.sectionLoadedOnce || (this.sectionLoadedOnceLevels !== this.levels)) {
        this.updateSection()
      }
      if (this.$route.query.cardsType !== 'doc' || this.$route.name !== 'ModelSectionCards') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'doc'}})
      }
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
        this.axios.get('/1/model/section/' + this.currentSectionId, {params: {levels: this.levels}}).then((response) => {
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
        }
      }
    },
    closeCardModal () {
      this.$router.replace({name: 'ModelSectionCards'})
    },
    downloadContent () {
      let fileContent = 'data:text/plain;charset=utf-8,'
      fileContent += sectionToMarkdown(this.section, 1)

      var encodedUri = encodeURI(fileContent)
      var link = document.createElement('a')
      link.setAttribute('href', encodedUri)
      link.setAttribute('download', this.section.title + '.md')
      document.body.appendChild(link)

      link.click()
    },
    atKeydown (e) {
      if (document.activeElement === this.$refs.inputQuery) {
        if (e.keyCode === 13) {
          e.preventDefault()
          this.updateQuery()
        }
      }
    }
  },

  created () {
    this.update()
    this.checkCardSubroute()
  },

  mounted () {
    window.addEventListener('keydown', this.atKeydown)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }
}
</script>

<style scoped>

.model-section-elements {
  height: 100%;
  display: flex;
  flex-grow: 1;
  flex-direction: column;
}

.model-section-elements-container {
  height: 100%;
  display: flex;
  flex-grow: 1;
  flex-direction: column;
}

.elements-container {
  padding: 12px 24px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.controls-row {
  margin: 12px 12px 6px 24px;
  min-height: 40px;
  flex-shrink: 0;
  white-space: nowrap;
  overflow-x: auto;
}

.control-group {
  margin-right: 20px;
  margin-bottom: 3px;
  display: inline-block;
  vertical-align: top;
}

.control-group div {
  display: inline-block;
}

.zoom-controls {
  min-height: 1px;
  width: 160px;
  text-align: center;
  color: #3e464e;
  background-color: #eff3f6;
  border-radius: 3px;
}

.zoom-controls .arrow-div {
  width: 40px;
  height: 38px;
}

.zoom-controls .arrow-div img {
  margin-top: 12px;
  width: 14px;
}

.zoom-controls .arrow-div:hover {
  background-color: #ced4d9;
}

.zoom-controls .number-div {
  width: 40px;
  height: 38px;
  font-size: 19px;
  padding: 5px 12px;
}

.zoom-controls-enabled {
  position: relative;
}

.zoom-controls .zoom-controls-cover {
  position: absolute;
  top:0;
  left:0;
  width:100%;
  height:100%;
  background-color: rgba(71, 71, 71, 0.8);
  border-radius: 3px;
}

.text-details {
  font-size: 12px;
}

.section-cards-container {
  margin-top: 15px;
}

</style>
