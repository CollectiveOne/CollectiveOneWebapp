<template lang="html">
  <div class="">
    <div class="w3-bar w3-theme-d1 w3-left-align w3-large">
      <div class="w3-bar-item">50,000.00 ouis</div>
      <div class="w3-bar-item w3-right w3-button" @click="mintMore()">mint more</div>
    </div>
    <div class="w3-row w3-theme-d2 w3-left-align w3-large">
      <div class="w3-col m6 w3-padding w3-display-container">
        <div class="">15,000.00 / 33% - Sub-Initiatives</div>
        <div class="w3-display-right w3-button" @click="newSubInitiative()">new</div>
      </div>
      <div class="w3-col m6 w3-padding w3-theme-d3">
        1,257.34 / 14.24% - Individuals
      </div>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'

export default {
  methods: {
    ...mapMutations(['showNewInitiativeModal']),

    newSubInitiative () {
      this.showNewInitiativeModal(true)
    }
  }
}
</script>

<style scoped>
</style>
