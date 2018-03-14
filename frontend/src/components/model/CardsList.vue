<template lang="html">
  <div class="">

    <div class="slider-container">
      <transition name="slideDownUp">
        <app-model-card-modal
          v-if="showNewCardModal"
          :isNew="true"
          :inSectionId="currentSection.id"
          :inSectionTitle="currentSection.title"
          @close="showNewCardModal = false">
        </app-model-card-modal>
      </transition>
    </div>

    <div class="cards-list">
      <app-model-section
        :section="section"
        :cardEffect="true">
      </app-model-section>
    </div>
  </div>
</template>

<script>

import ModelCardModal from '@/components/model/modals/ModelCardModal'
import ModelSection from '@/components/model/ModelSection'

export default {
  components: {
    'app-model-card-modal': ModelCardModal,
    'app-model-section': ModelSection
  },

  props: {
    level: {
      type: Number,
      default: 1
    }
  },

  data () {
    return {
      section: null,
      showNewCardModal: false
    }
  },

  computed: {
    currentSection () {
      return this.$store.state.model.currentSection
    }
  },

  watch: {
    level () {
      this.update()
    },
    '$store.state.model.currentSection' () {
      this.update()
    }
  },

  methods: {
    update () {
      if (this.currentSection) {
        this.axios.get('/1/model/section/' + this.currentSection.id, {params: {level: this.level}}).then((response) => {
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
