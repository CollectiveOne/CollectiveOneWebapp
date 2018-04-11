<template lang="html">
  <div class="model-section-cards">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :cardWrapperId="$route.params.cardId"
          @close="closeCardModal()"
          @updateCards="updateCards()">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="model-section-cards-container">
      <div class="w3-row controls-row">
        <div class="w3-left card-view-controls">
          <div @click="summaryView()" class="w3-left control-btn" :class="{'selected': isSummary}">
            <i class="fa fa-list" aria-hidden="true"></i>
          </div>
          <div @click="cardView()" class="w3-left control-btn" :class="{'selected': isCard}">
            <i class="fa fa-th-large" aria-hidden="true"></i>
          </div>
          <div @click="docView()" class="w3-left control-btn" :class="{'selected': isDoc}">
            <i class="fa fa-file-text" aria-hidden="true"></i>
          </div>
        </div>

        <div class="w3-left card-order-controls">
          <select v-model="cardSortBy" class="w3-input">
            <option value="BY_SECTIONS">Section Order</option>
            <option value="CREATION_DATE_DESC">Last Created</option>
            <option value="EDITION_DATE_DESC">Last Edited</option>
            <option value="CREATOR">Author</option>
          </select>
        </div>

        <div class="w3-left card-order-controls">
          <div class="w3-left">
            <input ref="inputQuery" v-model="newCardQuery" class="w3-input" type="text" name="" value="" placeholder="search">
          </div>
          <div @click="updateQuery()" class="w3-left control-btn selected">
            <i class="fa fa-search" aria-hidden="true"></i>
          </div>
        </div>

        <div v-if="cardQuery !== '' && !isSectionsOrder" class="w3-left card-order-controls text-details">
          searching by
          <span class="cursor-pointer" @click="resetQuery()">
            <i><b>"{{ cardQuery}}"</b></i> <i class="fa fa-times-circle" aria-hidden="true"></i>
          </span>
        </div>

      </div>

      <div class="cards-list">
        <app-model-section
          v-if="!loading && isSectionsOrder"
          :section="section"
          :cardsType="cardsType">
        </app-model-section>

        <app-model-cards-container
          v-if="!loading && !isSectionsOrder"
          :cardWrappers="cardWrappers"
          :inSection="section"
          :cardsType="cardsType">
        </app-model-cards-container>

        <div v-if="loading" class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../assets/loading.gif" alt="">
        </div>

      </div>
    </div>
  </div>
</template>

<script>

import ModelSection from '@/components/model/ModelSection'
import ModelCardsContainer from '@/components/model/cards/ModelCardsContainer'

export default {
  components: {
    'app-model-section': ModelSection,
    'app-model-cards-container': ModelCardsContainer
  },

  data () {
    return {
      section: null,
      showCardModal: false,
      loading: false,
      newCardQuery: '',
      cardQuery: '',
      cardSortBy: 'BY_SECTIONS',
      page: 0,
      pageSize: 20,
      cardWrappers: []
    }
  },

  computed: {
    currentSectionId () {
      return this.$route.params.sectionId
    },
    levels () {
      return this.$route.query.levels ? parseInt(this.$route.query.levels) : 1
    },
    cardsType () {
      return this.$route.query.cardsType ? this.$route.query.cardsType : 'card'
    },
    isSummary () {
      return this.cardsType === 'summary'
    },
    isCard () {
      return this.cardsType === 'card'
    },
    isDoc () {
      return this.cardsType === 'doc'
    },
    isSectionsOrder () {
      return this.cardQuery === '' && this.cardSortBy === 'BY_SECTIONS'
    }
  },

  watch: {
    '$route.params.sectionId' () {
      this.update()
    },
    '$route' () {
      this.checkCardSubroute()
    },
    levels () {
      this.update()
    },
    cardSortBy () {
      if (!this.isSectionsOrder) {
        this.updateCards()
      } else {
        this.updateSection()
      }
    }
  },

  methods: {
    summaryView () {
      if (this.$route.query.cardsType !== 'summary') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'summary'}})
      }
    },
    cardView () {
      if (this.$route.query.cardsType !== 'card') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'card'}})
      }
    },
    docView () {
      if (this.$route.query.cardsType !== 'doc') {
        this.$router.push({name: 'ModelSectionCards', query: {cardsType: 'doc'}})
      }
    },
    updateCards () {
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
            this.cardWrappers = response.data.data.content
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
      if (this.isSectionsOrder) {
        this.updateSection()
      } else {
        this.updateCards()
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

.controls-row {
  margin: 6px 6px;
}

.card-order-controls {
  margin-left: 20px;
}

.control-btn {
  cursor: pointer;
  margin-right: 4px;
  padding: 8px 12px;
  background-color: #b6b6b6;
  border-radius: 3px;
}

.control-btn:hover {
  background-color: #595959;
}

.selected {
  background-color: #15a5cc;
  color: white;
}

.control-btn-selected {
  background-color: #15a5cc;
}

.text-details {
  padding-top: 8px;
}

.cards-list {
  padding: 12px 12px;
}

</style>
