<template lang="html">
  <div class="">
    <div class="w3-row controls-row">
      <div class="w3-left card-view-controls">
        <div @click="cardsType = 'summary'" class="w3-left control-btn">
          <i class="fa fa-list" aria-hidden="true"></i>
        </div>
        <div @click="cardsType = 'card'" class="w3-left control-btn">
          <i class="fa fa-th-large" aria-hidden="true"></i>
        </div>
        <div @click="cardsType = 'doc'" class="w3-left control-btn">
          <i class="fa fa-file-text" aria-hidden="true"></i>
        </div>
      </div>
    </div>

    <div class="cards-list">
      <app-model-section
        v-if="!loading"
        :section="section"
        :cardsType="cardsType">
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

  data () {
    return {
      section: null,
      showNewCardModal: false,
      loading: false,
      cardsType: 'card'
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

<style scoped>

.controls-row {
  margin: 6px 6px;
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

.control-btn-selected {
  background-color: #15a5cc;
}

</style>
