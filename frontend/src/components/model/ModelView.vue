<template lang="html">
  <div v-if="view" class="model-view-container">
    <div class="view-title"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <div class="slider-container">
        <transition name="slideDownUp">
          <app-model-new-section-modal
            v-if="showNewSectionModal"
            :viewId="view.id" :initiativeId="view.initiativeId"
            :key="view.id"
            @close="showNewSectionModal = false">
          </app-model-new-section-modal>
        </transition>
      </div>

      <transition name="fadeenter">
        <div v-if="showActionButton" class="w3-col w3-right buttons-container gray-1-color" style="width:100px">
          <div @click="showViewModal = true" class="w3-button model-action-button w3-right">
            <i class="fa fa-expand" aria-hidden="true"></i>
          </div>
          <div  @click="showNewSectionModal = true" class="w3-button model-action-button w3-right">
            <i class="fa fa-plus-circle" aria-hidden="true"></i>
          </div>
        </div>
      </transition>
      <div class="w3-rest">
        <h1 class="">{{ view.title }}</h1>
      </div>
    </div>
    <app-model-section
      v-for="section in view.sections"
      :key="section.id"
      :section="section"
      :initiativeId="view.initiativeId"
      :level="0"
      class="view-sections">
    </app-model-section>
  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection.vue'
import ModelNewSectionModal from '@/components/model/modals/ModelNewSectionModal.vue'

export default {
  name: 'app-model-view',

  components: {
    'app-model-section': ModelSection,
    'app-model-new-section-modal': ModelNewSectionModal
  },

  data () {
    return {
      showActionButton: false,
      showNewSectionModal: false,
      showViewModal: false
    }
  },

  props: {
    view: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>

.model-view-container, .model-view-container h1 {
  font-family: 'Open Sans', sans-serif;
}

.model-view-container h1 {
  font-size: 32px;
}

.model-view-container {
  padding-bottom: 50px;
}

.model-button {
  background-color: #eff3f6;
  padding-top: 2px !important;
  padding-bottom: 3px !important;
}

.view-sections {
  margin-bottom: 30px;
}

.view-title .buttons-container {
  padding-top: 15px;
}

</style>
