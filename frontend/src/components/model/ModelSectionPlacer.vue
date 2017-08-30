<template lang="html">
  <app-model-section
    :initiativeId="$route.params.initiativeId"
    :viewId="$route.params.viewId">
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

<style lang="css">
</style>
