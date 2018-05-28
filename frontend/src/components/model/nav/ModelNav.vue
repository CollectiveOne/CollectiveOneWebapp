<template lang="html">
  <div class="model-nav-container-large">

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
      <div class="row">
        <app-model-nav-control-buttons>
        </app-model-nav-control-buttons>
      </div>
      <div class="w3-row">
        <app-model-section-nav-item
          :section="section"
          :coordinate="[0]"
          @section-selected="$emit('section-selected', $event)">
        </app-model-section-nav-item>
      </div>
    </div>

  </div>
</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelSectionNavItem from '@/components/model/nav/ModelSectionNavItem.vue'
import ModelNavControlButtons from '@/components/model/nav/ModelNavControlButtons.vue'

export default {
  components: {
    'app-model-section-modal': ModelSectionModal,
    'app-model-section-nav-item': ModelSectionNavItem,
    'app-model-nav-control-buttons': ModelNavControlButtons
  },

  data () {
    return {
      showSectionModal: false
    }
  },

  computed: {
    sectionData () {
      return this.$store.getters.getSectionDataAtCoord([0])
    },
    section () {
      if (this.sectionData) {
        return this.sectionData.section
      }
      return null
    },
    initiative () {
      return this.$store.state.initiative.initiative
    },
    levels () {
      return this.$store.state.viewParameters.levels
    }
  },

  methods: {
  },

  created () {
  }
}
</script>

<style scoped>

.model-nav-container-large {
  height: 100%;
}

.model-nav-container {
}

</style>
