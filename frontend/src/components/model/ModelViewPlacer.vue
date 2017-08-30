<template lang="html">
  <app-model-view
    :initiativeId="$route.params.initiativeId"
    :viewId="$route.params.viewId"
    @show-view-modal="$emit('show-view-modal', $event)"
    @show-section-modal="$emit('show-section-modal', $event)"
    @show-card-modal="$emit('show-card-modal', $event)">
  </app-model-view>
</template>

<script>
import ModelView from '@/components/model/ModelView.vue'

export default {
  components: {
    'app-model-view': ModelView
  }
}
</script>

<style lang="css">
</style>
