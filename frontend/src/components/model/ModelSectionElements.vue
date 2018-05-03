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

    <div class="model-section-elements-container">
      <div class="w3-row controls-row">

        <div class="w3-left control-group">
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

        <div class="w3-left control-group noselect">
          <div class="w3-left zoom-controls">
            <div @click="levelDown()" class="w3-left cursor-pointer arrow-div">
              <img src="./../../assets/zoom-out-icon.svg" alt="">
            </div>
            <div class="w3-left number-div">
              {{ levels }}
            </div>
            <div @click="levelUp()" class="w3-left cursor-pointer arrow-div">
              <img src="./../../assets/zoom-in-icon.svg" alt="">
            </div>
          </div>
        </div>

        <div v-if="isCardsContent" class="w3-left control-group">
          <div class="">
            <div @click="sectionOrder()" class="w3-left control-btn" :class="{'control-btn-selected': isSectionsOrder}">
              <img src="./../../assets/network-icon.svg" alt="">
            </div>
            <div @click="aggregatedOrder()" class="w3-left control-btn" :class="{'control-btn-selected': !isSectionsOrder}">
              <img src="./../../assets/search-icon.svg" alt="">
            </div>
          </div>
        </div>

        <div v-if="!isCardsContent" class="w3-left control-group">
          <div @click="isOnlyMessages = true" class="w3-left control-btn" :class="{'control-btn-selected': isOnlyMessages}">
            <img src="./../../assets/chat-icon-2.svg" alt="">
          </div>
          <div @click="isOnlyMessages = false" class="w3-left control-btn" :class="{'control-btn-selected': !isOnlyMessages}">
            <img src="./../../assets/all-events-icon.svg" alt="">
          </div>
        </div>

        <div class="w3-left slider-container">
          <transition name="slideRightLeft">
            <div v-if="isCardsContent && !isSectionsOrder" class="">
              <div class="w3-left control-group">
                <div class="w3-left">
                  <input ref="inputQuery" v-model="newCardQuery" class="w3-input"
                    type="text" name="" value="" placeholder="search">
                </div>
                <div @click="updateQuery()" class="w3-left control-btn control-btn-selected">
                  <i class="fa fa-refresh" aria-hidden="true"></i>
                </div>
              </div>

              <div class="w3-left control-group">
                <select v-model="cardSortBy" class="w3-input">
                  <option value="CREATION_DATE_DESC">Last Created</option>
                  <option value="EDITION_DATE_DESC">Last Edited</option>
                  <option value="CREATOR">Author</option>
                </select>
              </div>
            </div>
          </transition>
        </div>

        <div v-if="false" class="w3-left control-group text-details">
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
          :contextElementId="section.id"
          :onlyMessages="isOnlyMessages"
          :levels="levels">
        </app-message-thread>

        <app-model-section
          v-if="isCardsContent && isSectionsOrder"
          :section="section"
          :cardsType="cardsType">
        </app-model-section>

        <app-model-cards-container
          v-if="isCardsContent && !isSectionsOrder"
          :cardWrappers="cardWrappers"
          :inSection="section"
          :cardsType="cardsType"
          :acceptDrop="false">
        </app-model-cards-container>

        <div v-if="!isSectionsOrder && thereAreMore" class="w3-row w3-center">
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
import MessageThread from '@/components/notifications/MessageThread'
import ModelSection from '@/components/model/ModelSection'
import ModelCardsContainer from '@/components/model/cards/ModelCardsContainer'

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
      loading: false,
      orderType: 'sections',
      isOnlyMessages: true,
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
      this.$router.replace({name: this.$route.name, query: {levels: this.levels + 1}})
    },
    levelDown () {
      if (this.levels > 1) {
        this.$router.replace({name: this.$route.name, query: {levels: this.levels - 1}})
      }
    },
    summaryView () {
      if (this.$route.query.cardsType !== 'summary' || this.$route.name !== 'ModelSectionCards') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'summary'}})
      }
    },
    cardView () {
      if (this.$route.query.cardsType !== 'card' || this.$route.name !== 'ModelSectionCards') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'card'}})
      }
    },
    docView () {
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
          if (response.data.result === 'success') {
            this.section = response.data.data
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
        if (this.$route.params.sectionId === this.section.id) {
          this.showCardModal = true
        }
      }
    },
    closeCardModal () {
      this.$router.replace({name: 'ModelSectionCards'})
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

.controls-row {
  margin: 12px 12px 6px 24px;
  min-height: 40px;
  flex-shrink: 0;
}

.elements-container {
  padding: 12px 24px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.control-group {
  margin-right: 20px;
  margin-bottom: 3px;
}

.zoom-controls {
  min-height: 1px;
  width: 120px;
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

.text-details {
  font-size: 12px;
}

.section-cards-container {
  margin-top: 15px;
}

</style>
