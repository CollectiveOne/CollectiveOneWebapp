<template lang="html">
  <div v-if="initiative" class="this-container w3-container w3-padding">
    <app-new-initiative-modal v-if="showNewInitiativeModal" :parent=""></app-new-initiative-modal>
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
        <h4>Assets</h4>
      </header>
      <div v-if="hasOwnTokens" class="w3-container">
        <h5><b>{{ this.initiative.ownTokens.assetName }}</b></h5>
        <app-tokens-distribution-chart :tokenData="this.initiative.ownTokens"></app-tokens-distribution-chart>
      </div>
      <div v-if="hasOtherAssets" class="w3-container">
        <h5><b>{{ this.initiative.otherAssets[0].assetName }}</b></h5>
        <app-tokens-distribution-chart :tokenData="this.initiative.otherAssets[0]"></app-tokens-distribution-chart>
      </div>
    </div>
    <br>
  </div>
</template>

<script>
import NewInitiativeModal from '../modal/NewInitiativeModal.vue'
import TokensDistributionChart from './TokensDistributionChart.vue'

export default {
  components: {
    AppNewInitiativeModal: NewInitiativeModal,
    AppTokensDistributionChart: TokensDistributionChart
  },

  data () {
    return {
      showNewInitiativeModal: false,
      parentInitiativeForModal: null
    }
  },

  computed: {
    initiative () {
      return this.$store.state.activeInitiative.initiative
    },
    hasOwnTokens () {
      if (this.initiative.ownTokens) {
        return true
      } else {
        return false
      }
    },
    hasOtherAssets () {
      if (this.initiative.otherAssets) {
        if (this.initiative.otherAssets.length > 0) {
          return true
        }
      }
      return false
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
}

</style>
