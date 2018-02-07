<template lang="html">
  <div class="">
    <div v-show="!cardSelected" class="">
      <div class="w3-row w3-margin-top">
        <input @input="queryUpdate()" v-model="query" class="w3-input" type="text" name="" value="" placeholder="search and select a card">
      </div>
      <div class="card-results-container w3-row-padding w3-container w3-margin-top w3-border">
        <div class="card-container cursor-pointer"
          v-for="cardWrapper in cardWrappers"
          :key="cardWrapper.id"
          @click="cardClicked(cardWrapper)">
          <app-model-card
            :cardWrapper="cardWrapper"
            :enableExpand="false"
            :hoverHighlight="true">
          </app-model-card>
        </div>
        <div v-if="cardWrappers.length == 0" class="w3-center">
          <i>no cards found</i>
        </div>
      </div>
    </div>
    <div v-if="cardSelected !== null" class="w3-row w3-display-container w3-margin-top w3-container">
      <app-model-card
        :cardWrapper="cardSelected"
        :enableExpand="false">
      </app-model-card>
      <div class="delete-selected gray-1-color w3-display-topright w3-xlarge"
        @click="cardDeselected()">
        <i class="fa fa-times-circle cursor-pointer" aria-hidden="true"></i>
      </div>
    </div>
  </div>
</template>

<script>
import ModelCard from '@/components/model/ModelCard.vue'

export default {

  name: 'model-card-selector',

  components: {
    'app-model-card': ModelCard
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      query: '',
      page: 0,
      cardWrappers: [],
      cardSelected: null
    }
  },

  methods: {
    queryUpdate () {
      this.update()
    },
    cardClicked (card) {
      this.cardSelected = card
      this.$emit('select', card)
    },
    cardDeselected () {
      this.cardSelected = null
      this.$emit('select', null)
    },
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/cardWrapper/search', {
        params: {
          query: this.query,
          page: this.page,
          size: 10
        }
      }).then((response) => {
        this.cardWrappers = response.data.data.content
      })
    }
  },

  created () {
    this.update()
  }
}
</script>

<style scoped>

.card-results-container {
  max-height: 500px;
  overflow-y: auto;
  padding-top: 16px;
  padding-bottom: 16px;
}

.card-container {
  margin-bottom: 16px;
  display: inline-block;
  width: 100%;
}

@media screen and (min-width: 992px) {
  .card-container {
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
