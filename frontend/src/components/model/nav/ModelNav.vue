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

    <div class="model-nav-container w3-container">
      <div class="w3-row">
        <app-model-section-nav-item
          v-for="section in sections" :key="section.id"
          :section="section">
        </app-model-section-nav-item>
      </div>
      <div class="w3-center w3-row w3-margin-top">
        <button @click="showSectionModal = true" class="w3-button app-button" type="button" name="button">new section</button>
      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionModal from '@/components/model/modals/ModelSectionModal.vue'
import ModelSectionNavItem from '@/components/model/nav/ModelSectionNavItem.vue'

export default {
  components: {
    'app-model-section-modal': ModelSectionModal,
    'app-model-section-nav-item': ModelSectionNavItem
  },
  data () {
    return {
      showSectionModal: false,
      sections: []
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    }
  },

  methods: {
    updateSections () {
      this.axios.get('/1/initiative/' + this.initiative.id + '/model').then((response) => {
        if (response.data.result === 'success') {
          this.sections = response.data.data
        }
      })
    }
  },

  created () {
    this.updateSections()
  }
}
</script>

<style scoped>

.model-nav-container {
  padding-top: 16px;
  min-height: calc(100vh - 131px);
}

</style>
