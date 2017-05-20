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
        <h4>Assets</h4>
      </header>
      <div v-if="hasOwnTokens" class="w3-container">
        <h5><b>{{ this.initiative.ownTokens.assetName }}</b></h5>
        <app-tokens-distribution-chart :assetId="this.initiative.ownTokens.assetId" :initiativeId="this.initiative.id"></app-tokens-distribution-chart>
      </div>
      <div v-if="hasOtherAssets" class="w3-container">
        <h5><b>{{ this.initiative.otherAssets[0].assetName }}</b></h5>
        <app-tokens-distribution-chart :assetId="this.initiative.otherAssets[0].assetId" :initiativeId="this.initiative.id"></app-tokens-distribution-chart>
      </div>
    </div>
    <br>
  </div>
</template>

<script>
import InitiativeTokensDistributionChart from './InitiativeTokensDistributionChart.vue'

export default {
  components: {
    AppTokensDistributionChart: InitiativeTokensDistributionChart
  },

  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      showNewInitiativeModal: false,
      parentInitiativeIdForModal: null
    }
  },

  computed: {
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
  },

  methods: {
    newSubInitiativeClicked (initiativeId) {
      this.parentInitiativeIdForModal = initiativeId
      this.showNewInitiativeModal = true
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
}

</style>
