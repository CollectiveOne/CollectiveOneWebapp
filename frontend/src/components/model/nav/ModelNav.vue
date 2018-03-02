<template lang="html">
  <div class="">
    
    <app-model-section-modal></app-model-section-modal>
    <div class="model-nav-container">
      <div class="w3-center">
        <button class="w3-button app-button" type="button" name="button">new section</button>
      </div>
      <app-model-section-nav-item
        v-for="section in sections" :key="section.id"
        :section="section">
      </app-model-section-nav-item>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    sections () {
      return this.$store.state.initiative.initiativeModelSections
    }
  }
}
</script>

<style scoped>

.model-nav-container {
  padding-top: 16px;
  min-height: calc(100vh - 131px);
}

</style>
