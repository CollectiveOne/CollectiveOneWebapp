<template lang="html">
  <div class="model-section-read">

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

    <div class="section-nav">

    </div>

    <div class="section-content">
      <div class="w3-row">
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

      <div class="w3-row">
        <app-model-section
          v-if="!loading"
          :section="section"
          :showThisTitle="true"
          :hideCardControls="true"
          :cardRouteName="'ModelSectionReadCard'"
          :cardsType="cardsType">
        </app-model-section>

        <div v-else class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../assets/loading.gif" alt="">
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection'

export default {

  components: {
    'app-model-section': ModelSection
  },

  data () {
    return {
      section: null,
      loading: false,
      showCardModal: false
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

.section-content {
  padding: 12px 12px;
}

</style>
