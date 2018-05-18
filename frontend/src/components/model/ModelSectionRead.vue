<template lang="html">
  <div class="model-section-read-component">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal v-if="showCardModal"
          :isNew="false"
          :cardWrapperId="$route.params.cardId"
          :inSectionId="section.id"
          :inSectionTitle="section.title"
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
            v-if="!loading"
            :section="section"
            :showThisTitle="true"
            :hideCardControls="true"
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
      return this.$route.query.cardsType ? this.$route.query.cardsType : 'doc'
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
      if (this.$route.query.cardsType !== 'summary') {
        this.$router.push({name: 'ModelSectionRead', query: {cardsType: 'summary'}})
      }
    },
    cardView () {
      if (this.$route.query.cardsType !== 'card') {
        this.$router.push({name: 'ModelSectionRead', query: {cardsType: 'card'}})
      }
    },
    docView () {
      if (this.$route.query.cardsType !== 'doc') {
        this.$router.push({name: 'ModelSectionRead', query: {cardsType: 'doc'}})
      }
    }
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
  padding: 12px 12px;
  flex-grow: 1;
  height: 100%;
  display: flex;
  flex-direction: column;
  min-width: 300px;
}

.control-btns-row {
  padding-left: 28px;
  flex-shrink: 0;
}

.section-row {
  flex-grow: 1;
  overflow: auto;
  padding: 12px 0px;
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
