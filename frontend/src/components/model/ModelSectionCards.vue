<template lang="html">
  <div class="">
    <div class="cards-list">
      <app-model-section
        v-if="!loading"
        :section="section"
        :cardsAsCards="cardsAsCards">
      </app-model-section>
      <div v-else class="w3-row w3-center loader-gif-container">
        <img class="loader-gif" src="../../assets/loading.gif" alt="">
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

  props: {
    cardsAsCards: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      section: null,
      showNewCardModal: false,
      loading: false
    }
  },

  computed: {
    currentSectionId () {
      return this.$route.params.sectionId
    },
    levels () {
      return parseInt(this.$route.query.levels)
    }
  },

  watch: {
    '$route.params.sectionId' () {
      this.update()
    },
    levels () {
      this.update()
    }
  },

  methods: {
    update () {
      this.loading = true
      if (this.currentSectionId) {
        this.axios.get('/1/model/section/' + this.currentSectionId, {params: {levels: this.levels}}).then((response) => {
          this.loading = false
          if (response.data.result === 'success') {
            this.section = response.data.data
          }
        })
      }
    }
  },

  created () {
    this.update()
  }
}
</script>

<style section>
</style>
