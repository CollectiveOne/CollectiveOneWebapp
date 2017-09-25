<template lang="html">
  <div class="">

    <div class="">
      <h1 class="">Section Page</h1>
    </div>

    <app-model-section-with-modal
      :initiativeId="$route.params.initiativeId"
      :sectionId="$route.params.sectionId"
      :key="$route.params.sectionId"
      :preloaded="false"
      :level="0"
      :floating="true">
    </app-model-section-with-modal>
  </div>
</template>

<script>
import ModelSectionWithModal from '@/components/model/ModelSectionWithModal.vue'

export default {
  components: {
    'app-model-section-with-modal': ModelSectionWithModal
  }
}
</script>

<style scoped>
</style>
