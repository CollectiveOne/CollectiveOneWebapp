<template lang="html">
  <div class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-col m4">
        <h5 class="w3-text-indigo w3-hide-medium w3-hide-large">Subinitiative of</h5>
        <h5 class="w3-text-indigo w3-hide-small w3-right">Subinitiative of</h5>
      </div>
      <div class="w3-col m8">
        <app-initiative-selector class="initiative-selector"
          anchor="id" label="name" :init="parentInitiative"
          url="/1/secured/initiatives/suggestions"
          @select="parentInitiativeSelected($event)">
        </app-initiative-selector>
      </div>
    </div>
    <div v-if="parentInitiative.ownTokens" class="w3-row assigner-div">
      <app-tokens-distribution-chart
        :assetId="parentInitiative.ownTokens.assetId" :initiativeId="parentInitiative.id"
        :type="'assigner'">
      </app-tokens-distribution-chart>
    </div>
    <div v-if="parentInitiative.otherAssets" class="w3-row assigner-div">
      <app-tokens-distribution-chart v-for="asset in parentInitiative.otherAssets"
        :key="asset.assetId" :assetId="asset.assetId" :initiativeId="parentInitiative.id"
        :type="'assigner'">
      </app-tokens-distribution-chart>
    </div>

  </div>
</template>

<script>
import InitiativeSelector from '../initiative/InitiativeSelector.vue'
import TokensDistributionChart from '../initiative/InitiativeTokensDistributionChart.vue'

export default {
  props: {
    parentInitiative: {
      type: Object
    }
  },

  components: {
    'app-initiative-selector': InitiativeSelector,
    'app-tokens-distribution-chart': TokensDistributionChart
  },

  methods: {
    parentInitiativeSelected (initiative) {
      this.axios.get('/1/secured/initiative/' + initiative.id, {
        params: {
          addAssets: true
        }
      }).then((response) => {
        this.$emit('parent-initiative-updated', response.data.data)
      })
    },

    parentOwnTokensSelected (data) {
      this.$emit('selected', [data])
    },

    parentOtherAssetSelected (data) {
      this.$emit('selected', [data])
    }
  }

}
</script>

<style scoped>

</style>
