<template lang="html">
  <div class="">
    <app-initiative-tag-selector
      @input="tagSelected($event)">
    </app-initiative-tag-selector>
  </div>
</template>

<script>
import InitiativeTagSelectorManager from '@/components/initiative/InitiativeTagSelectorManager.vue'

export default {
  components: {
    'app-initiative-tag-selector': InitiativeTagSelectorManager
  },
  methods: {
    tagSelected (tagText) {
      /* check if tag exist */
      this.axios.get('/1/secured/initiative')
      this.
    }
  }
}
</script>

<style scoped>
</style>
