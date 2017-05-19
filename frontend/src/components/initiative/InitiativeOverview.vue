<template lang="html">
  <div v-if="initiative" class="this-container w3-container w3-padding">
    <keep-alive>
      <app-new-initiative-modal v-if="showNewInitiativeModal" :parentInitId="parentInitiativeIdForModal" @close-this="showNewInitiativeModal = false"></app-new-initiative-modal>
    </keep-alive>
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
        <!-- <app-tokens-distribution-chart :initiativeData="this.initiative" @new-subinitiative="newSubInitiativeClicked($event)"></app-tokens-distribution-chart> -->
      </div>
      <div v-if="hasOtherAssets" class="w3-container">
        <h5><b>{{ this.initiative.otherAssets[0].assetName }}</b></h5>
        <!-- <app-tokens-distribution-chart :initiativeData="this.initiative.otherAssets[0]"></app-tokens-distribution-chart> -->
      </div>
    </div>
    <br>
  </div>
</template>

<script>
import NewInitiativeModal from '../modal/NewInitiativeModal.vue'
import InitiativeTokensDistributionChart from './InitiativeTokensDistributionChart.vue'

export default {
  components: {
    AppNewInitiativeModal: NewInitiativeModal,
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
