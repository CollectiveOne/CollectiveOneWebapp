<template lang="html">
  <div v-if="view" class="model-view-container">
    <div class="view-title"
      @mouseover="showActionButton = true"
      @mouseleave="showActionButton = false">

      <transition name="fadeenter">
        <div v-if="showActionButton" class="w3-col w3-right buttons-container gray-1-color" style="width:100px">
          <div @click="expandViewModal()" class="w3-button model-action-button w3-right">
            <i class="fa fa-expand" aria-hidden="true"></i>
          </div>
          <div
            @click="newSection()"
            class="w3-button model-action-button w3-right">
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
      class="view-sections"
      @show-section-modal="$emit('show-section-modal', $event)"
      @show-card-modal="$emit('show-card-modal', $event)">
    </app-model-section>
  </div>
</template>

<script>
import ModelSection from '@/components/model/ModelSection.vue'
export default {
  name: 'app-model-view',

  components: {
    'app-model-section': ModelSection
  },

  data () {
    return {
      showActionButton: false
    }
  },

  props: {
    view: {
      type: Object,
      default: null
    }
  },

  methods: {
    expandViewModal () {
      this.$emit('show-view-modal', {
        new: false,
        viewId: this.view.id,
        initiativeId: this.view.initiativeId
      })
    },
    newSection () {
      this.$emit('show-section-modal', {
        new: true,
        initiativeId: this.view.initiativeId,
        isSubsection: false,
        viewId: this.view.id,
        viewTitle: this.view.title
      })
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
