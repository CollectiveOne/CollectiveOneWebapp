<template lang="html">
  <div v-if="initiative" class="this-container w3-container w3-padding">
    <div class="w3-card">
      <header class="w3-container w3-theme">
        <h4>Driver</h4>
      </header>
      <div class="w3-container">
        <p>{{ initiative.driver }}</p>
      </div>
    </div>
    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme">
        <h4>Tokens Distribution</h4>
      </header>
      <div class="">
        <app-initiative-ecosystem-tokens></app-initiative-ecosystem-tokens>
      </div>
    </div>
    <br>
  </div>
</template>

<script>
import InitiativeEcosystemTokens from './InitiativeEcosystemTokens.vue'

export default {
  components: {
    AppInitiativeEcosystemTokens: InitiativeEcosystemTokens
  },

  computed: {
    initiative () {
      return this.$store.state.activeInitiative.initiative
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
}

</style>
