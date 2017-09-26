<template lang="html">
  <div class="">
    <div class="w3-row-padding">
      <div class="w3-col m4">
        <input @input="queryUpdate()" v-model='query' class="w3-input" type="text" name="" placeholder="search">
      </div>
      <div class="w3-col m8 search-config-row">
        <div class="w3-margin-right" style="display: inline-block"><b>type:</b></div>
        <button
          @click="searchSectionsClicked()"
          class="w3-button" type="button" name="button"
          :class="{'app-button-light': !searchSections, 'app-button': searchSections}">
          sections
        </button>
        <button
          @click="searchCardsClicked()"
          class="w3-button app-button" type="button" name="button"
          :class="{'app-button-light': !searchCards, 'app-button': searchCards}">
          cards
        </button>
      </div>
    </div>

    <div v-if="sections.length > 0 || cardWrappers.length > 0"
      class="w3-row-padding search-results-row">

      <div v-if="searchSections" class="w3-col" :class="{'l6': searchBoth, 's12': !searchBoth}">
        <b>Sections:</b>
        <hr style="margin-top: 3px">
        <div v-if="sections.length > 0" class="">
          <div
            v-for="section in sections"
            :key="section.id"
            class="section-container">

            <app-model-section
              :initiativeId="initiative.id"
              :sectionId="section.id"
              :preloaded="false"
              :floating="true">
            </app-model-section>
          </div>
        </div>
        <div v-else class="w3-center">
          <i>no results found</i>
        </div>
      </div>
      <div v-if="searchCards" class="w3-col"  :class="{'l6': searchBoth, 's12': !searchBoth}">
        <b>Cards:</b>
        <hr style="margin-top: 3px">
        <div
          v-for="cardWrapper in cardWrappers"
          :key="cardWrapper.id"
          class="card-container">

          <app-model-card
            :cardWrapper="cardWrapper"
            :floating="true">
          </app-model-card>
        </div>
      </div>
    </div>
    <div v-else class="w3-row w3-center">
      <div v-if="loading" class="loader-gif-container">
        <img class="loader-gif" src="../../assets/loading.gif" alt="">
      </div>
      <div v-else class="loader-gif-container">
        <i>no results found</i>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection.vue'
import ModelCard from '@/components/model/ModelCard.vue'

export default {
  components: {
    'app-model-section': ModelSection,
    'app-model-card': ModelCard
  },

  props: {
    initType: {
      type: String,
      default: 'CARDS'
    },
    exclusive: {
      type: Boolean,
      default: false
    },
    queryInit: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      query: '',
      page: 0,
      searchSections: true,
      searchCards: true,
      sections: [],
      cardWrappers: [],
      loading: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    searchBoth () {
      return this.searchCards && this.searchSections
    }
  },

  methods: {
    searchCardsClicked () {
      this.searchCards = !this.searchCards
      if (this.exclusive) {
        this.searchSections = !this.searchSections
      }
      this.queryUpdate()
    },
    searchSectionsClicked () {
      this.searchSections = !this.searchSections
      if (this.exclusive) {
        this.searchCards = !this.searchCards
      }
      this.queryUpdate()
    },
    queryUpdate () {
      this.$emit('query-updated', this.query)

      if (this.searchSections) {
        this.loading = true
        this.axios.get('/1/initiative/' + this.initiative.id + '/model/section/search', {
          params: {
            query: this.query,
            page: this.page,
            size: 10
          }
        }).then((response) => {
          this.sections = response.data.data.content
          this.loading = false
        })
      } else {
        this.sections = []
      }

      if (this.searchCards) {
        this.loading = true
        this.axios.get('/1/initiative/' + this.initiative.id + '/model/cardWrapper/search', {
          params: {
            query: this.query,
            page: this.page,
            size: 10
          }
        }).then((response) => {
          this.cardWrappers = response.data.data.content
          this.loading = false
        })
      } else {
        this.cardWrappers = []
      }
    }
  },

  created () {
    if (this.exclusive) {
      switch (this.initType) {
        case 'CARDS':
        default:
          this.searchCards = true
          this.searchSections = false
          break

        case 'SECTIONS':
          this.searchCards = false
          this.searchSections = true
          break
      }
    }
    this.query = this.queryInit
    this.queryUpdate()
  }
}
</script>

<style scoped>

.search-config-row {
  padding-top: 12px;
}

.search-config-row button {
  padding-top: 0px;
  padding-bottom: 0px;
  width: 120px;
}

.w3-col {
  min-height: 16px;
}

.search-results-row {
  padding-top: 20px;
}

.search-results-row .section-container {
  margin-bottom: 22px;
}

.search-results-row .card-container {
  margin-bottom: 22px;
  display: inline-block;
}

@media screen and (min-width: 1200px) {
  .search-results-row .card-container {
    width: calc(50% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

@media screen and (min-width: 992px) and (max-width: 1199px) {
  .search-results-row .card-container {
    width: calc(100% - 16px);
    margin-left: 8px;
    margin-right: 8px;
    vertical-align: top;
  }
}

</style>
