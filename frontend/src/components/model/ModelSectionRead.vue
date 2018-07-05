<template lang="html">
  <div class="model-section-read-component">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :cardWrapperId="$route.params.cardId"
          @close="closeCardModal()">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="model-section-read-container">

      <transition name="slideRightLeft">
        <div v-show="expandModelNav" class="section-nav dark-gray">
          <app-model-section-nav-simple :section="section">
          </app-model-section-nav-simple>
        </div>
      </transition>

      <div class="hide-nav-div-container">
        <div @click="expandModelNavClicked()" class="hide-nav-div drop-shadow-br">
          <i v-if="expandModelNav" class="fa fa-chevron-left" aria-hidden="true"></i>
          <i v-else class="fa fa-chevron-right" aria-hidden="true"></i>
        </div>
      </div>

      <div class="section-content">
        <div class="w3-row control-btns-row">
          <div class="">
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
        </div>

        <div class="w3-row section-row">
          <app-model-section
            :class="{'section-component-doc': cardsType === 'doc'}"
            v-if="!loading"
            :section="section"
            :readOnly="true"
            :showThisTitle="true"
            :showPrivate="false"
            :showShared="false"
            :showCommon="true"
            :cardRouteName="'ModelSectionReadCard'"
            :cardsType="cardsType">
          </app-model-section>

          <div v-else class="w3-row w3-center loader-gif-container">
            <img class="loader-gif" src="../../assets/loading.gif" alt="">
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection'
import ModelSectionNavSimple from '@/components/model/nav/ModelNavSimple'

export default {

  components: {
    'app-model-section': ModelSection,
    'app-model-section-nav-simple': ModelSectionNavSimple
  },

  data () {
    return {
      section: null,
      loading: false,
      showCardModal: false,
      expandModelNav: true
    }
  },

  computed: {
    sectionId () {
      return this.$route.params.sectionId
    },
    cardsType () {
      return this.$store.state.viewParameters.cardsType
    },
    isSummary () {
      return this.cardsType === 'summary'
    },
    isCard () {
      return this.cardsType === 'card'
    },
    isDoc () {
      return this.cardsType === 'doc'
    }
  },

  watch: {
    '$route.params.sectionId' () {
      this.updateSection()
    },
    '$route.name' () {
      this.checkCardSubroute()
    }
  },

  methods: {
    expandModelNavClicked () {
      this.expandModelNav = !this.expandModelNav
    },
    updateSection () {
      this.loading = true
      this.axios.get('/1/model/section/' + this.sectionId, {params: {levels: 999}})
      .then((response) => {
        this.loading = false
        if (response.data.result === 'success') {
          this.section = response.data.data
          this.checkCardSubroute()
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    checkCardSubroute () {
      this.showCardModal = false
      if (this.$route.name === 'ModelSectionReadCard') {
        if (this.section) {
          this.showCardModal = true
        }
      }
    },
    closeCardModal () {
      this.$router.replace({name: 'ModelSectionRead'})
    },
    summaryView () {
      this.$store.commit('setCardsType', 'summary')
    },
    cardView () {
      this.$store.commit('setCardsType', 'card')
    },
    docView () {
      this.$store.commit('setCardsType', 'doc')
    }
  },

  beforeCreate () {
    this.$store.commit('setCardsType', 'doc')
  },

  created () {
    this.updateSection()
  }
}
</script>

<style scoped>

.model-section-read-component {
  height: 100%;
}

.section-nav {
  min-width: 350px;
  height: 100%;
  overflow: auto;
}

.model-section-read-container {
  height: 100%;
  display: flex;
  flex-direction: row;
  position: relative;
}

.section-content {
  padding: 12px 0px 12px 12px;
  flex-grow: 1;
  height: 100%;
  display: flex;
  flex-direction: column;
  min-width: 300px;
}

.control-btns-row {
  padding-left: 28px;
  padding-bottom: 12px;
  flex-shrink: 0;
}

.control-btn {
  margin-right: 6px;
}

.section-component-doc {
  max-width: 1000px;
}

.section-row {
  flex-grow: 1;
  overflow: auto;
  padding: 12px 0px;
  padding-left: 3vw;
  padding-right: 3vw;
}

.hide-nav-div-container {
  width: 0px;
  z-index: 3;
}

.hide-nav-div {
  width: 25px;
  height: 35px;
  position: absolute;
  top: 12px;
  background-color: #313942;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  color: white;
  padding-top: 7px;
  padding-left: 6px;
  cursor: pointer;
}

.hide-nav-div:hover {
  background-color: #3e464e;
}

</style>
