<template lang="html">
  <div class="">

    <transition name="slideDownUp">
      <app-model-section-modal
        v-if="showSectionModal"
        :isNew="true"
        :initiativeId="initiative.id"
        :inInitiative="true"
        :inElementId="initiative.id"
        :inElementTitle="initiative.meta.name"
        @close="showSectionModal = false">
      </app-model-section-modal>
    </transition>

    <div class="model-nav-container">
      <div class="w3-center">
        <button @click="showSectionModal = true" class="w3-button app-button" type="button" name="button">new section</button>
      </div>
      <app-model-section-nav-item
        v-for="section in sections" :key="section.id"
        :section="section">
      </app-model-section-nav-item>
    </div>
  </div>
</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'

export default {
  components: {
    'app-model-section-modal': ModelSectionModal
  },
  data () {
    return {
      showSectionModal: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    sections () {
      return this.$store.state.initiative.initiativeModelSections
    }
  }
}
</script>

<style scoped>

.model-nav-container {
  padding-top: 16px;
  min-height: calc(100vh - 131px);
}

</style>
