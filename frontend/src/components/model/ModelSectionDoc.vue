<template lang="html">
  <div class="">
    <div class="cards-list">
      <app-model-section
        :section="section"
        :cardsAsCards="false">
      </app-model-section>
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
  },

  data () {
    return {
      section: null,
      showNewCardModal: false
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
    '$route' () {
      this.update()
    }
  },

  methods: {
    update () {
      if (this.currentSectionId) {
        this.axios.get('/1/model/section/' + this.currentSectionId, {params: {levels: this.levels}}).then((response) => {
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
