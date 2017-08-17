<template lang="html">
  <div v-if="view" class="model-view-container">
    <h1>{{ view.title }}</h1>
    <app-model-section
      v-for="section in view.sections"
      :key="section.id"
      :section="section"
      :level="0">
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
  props: {
    view: {
      type: Object,
      default: null
    }
  }
}
</script>

<style lang="css">

.model-view-container {
  font-family: 'Open Sans', sans-serif;
}

.model-view-container h1 {
  font-family: 'Open Sans', sans-serif;
}

.model-button {
  background-color: #eff3f6;
  padding-top: 2px !important;
  padding-bottom: 3px !important;
}

</style>
