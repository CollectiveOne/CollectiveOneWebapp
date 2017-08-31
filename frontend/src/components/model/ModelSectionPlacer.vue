<template lang="html">
  <app-model-section
    :initiativeId="$route.params.initiativeId"
    :sectionId="$route.params.sectionId"
    :key="$route.params.sectionId"
    :preloaded="false"
    @show-view-modal="$emit('show-view-modal', $event)"
    @show-section-modal="$emit('show-section-modal', $event)"
    @show-card-modal="$emit('show-card-modal', $event)">
  </app-model-section>
</template>

<script>
import ModelSection from '@/components/model/ModelSection.vue'

export default {
  components: {
    'app-model-section': ModelSection
  }
}
</script>

<style scoped>
</style>
