<template lang="html">
  <div class="">
    <div v-show="!cardSelected" class="">
      <div class="w3-row-padding w3-margin-top">
        <div class="w3-col m6">
          <div class="input-div">
            <input v-model="query" class="w3-input" type="text" name="" value="" placeholder="search and select a card" ref="inputQuery">
          </div>
          <div @click="resetSearch()" class="search-btn-div control-btn control-btn-selected">
            <i class="fa fa-search" aria-hidden="true"></i>
          </div>
        </div>
        <div class="w3-col m6">
          <div @click="summaryView()" class="w3-left control-btn" :class="{'control-btn-selected': isSummary}">
            <img src="./../../../assets/rows-icon.svg" alt="">
          </div>
          <div @click="cardView()" class="w3-left control-btn" :class="{'control-btn-selected': isCard}">
            <img src="./../../../assets/cards-icon.svg" alt="">
          </div>
        </div>

      </div>
      <div class="card-results-container w3-row-padding w3-container w3-margin-top w3-border">
        <div class="card-container-in-selector cursor-pointer"
          v-for="cardWrapper in cardWrappers"
          :key="cardWrapper.id"
          @click="cardClicked(cardWrapper)">
          <div class="w3-card-4 w3-topbar w3-round-large card-subcontainer-in-selector">
            <app-model-card-as-card-content
             :card="cardWrapper.card"
             :showFull="false"
             @card-clicked="cardClicked()">
            </app-model-card-as-card-content>
          </div>
        </div>
        <div v-if="cardWrappers.length == 0" class="w3-center">
          <i>no cards found</i>
        </div>
      </div>
    </div>
    <div v-if="cardSelected !== null" class="w3-row w3-display-container w3-margin-top w3-container">
      <div class="w3-card-4 w3-topbar w3-round-large card-subcontainer-in-selector">
        <app-model-card-as-card-content
         :card="cardSelected.card"
         :showFull="false"
         @card-clicked="cardClicked()">
        </app-model-card-as-card-content>
      </div>
      <div class="delete-selected gray-1-color w3-display-topright w3-xlarge"
        @click="cardDeselected()">
        <i class="fa fa-times-circle cursor-pointer" aria-hidden="true"></i>
      </div>
    </div>
  </div>
</template>

<script>
import ModelCardAsCardContent from '@/components/model/cards/ModelCardAsCardContent.vue'

export default {

  name: 'model-card-selector',

  components: {
    'app-model-card-as-card-content': ModelCardAsCardContent
  },

  props: {
    inSectionId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      query: '',
      page: 0,
      cardWrappers: [],
      cardSelected: null,
      allShown: false,
      cardsType: 'card',
      cardSortBy: 'CREATION_DATE_DESC'
    }
  },

  watch: {
    cardSortBy () {
      this.queryUpdate()
    }
  },

  computed: {
    isSummary () {
      return this.cardsType === 'summary'
    },
    isCard () {
      return this.cardsType === 'card'
    }
  },

  methods: {
    summaryView () {
      this.cardsType = 'summary'
    },
    cardView () {
      this.cardsType = 'card'
    },
    cardClicked (card) {
      this.cardSelected = card
      this.$emit('select', card)
    },
    cardDeselected () {
      this.cardSelected = null
      this.$emit('select', null)
    },
    resetSearch () {
      this.allShow = false
      this.axios.get('/1/model/section/' + this.inSectionId + '/cardWrappers/search', {
        params: {
          query: this.query,
          page: this.page,
          pageSize: 10,
          sortBy: this.cardSortBy,
          inInitiativeEcosystem: true
        }
      }).then((response) => {
        if (response.data.data.content < 10) {
          this.allShown = true
        }
        this.cardWrappers = response.data.data.content
      })
    },
    atKeydown (e) {
      if (document.activeElement === this.$refs.inputQuery) {
        if (e.keyCode === 13) {
          e.preventDefault()
          this.resetSearch()
        }
      }
    }
  },

  created () {
    this.resetSearch()
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

.card-subcontainer-in-selector {
  width: 100%;
  max-height: 250px;
  overflow-y: auto;
}

.input-div {
  float: left;
  width: calc(100% - 50px);
}

.search-btn-div {
  float: left;
  width: 35px;
}

.card-results-container {
  max-height: 500px;
  overflow-y: auto;
  padding-top: 16px;
  padding-bottom: 16px;
}

.card-container-in-selector {
  margin-bottom: 16px;
  display: inline-block;
  width: 100%;
}

@media screen and (min-width: 992px) {
  .card-container-in-selector {
    width: calc(50% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

.delete-selected {
  position: absolute;
  margin-right: 25px;
  margin-top: 2px;
}

.delete-selected:hover {
  color: #15a5cc !important;
}

</style>
