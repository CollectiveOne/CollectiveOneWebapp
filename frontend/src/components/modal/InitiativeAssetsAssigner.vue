<template lang="html">
  <div class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-col m4">
        <h5 class="w3-text-indigo w3-hide-medium w3-hide-large"><b>Subinitiative of</b></h5>
        <h5 class="w3-text-indigo w3-hide-small w3-right"><b>Subinitiative of</b></h5>
      </div>
      <div class="w3-col m8">
        <app-initiative-selector class="initiative-selector"
          anchor="id" label="name" :init="initiative"
          url="/1/secured/initiatives/suggestions"
          @select="initiativeSelected($event)">
        </app-initiative-selector>
      </div>
    </div>
    <div v-if="initiative.ownTokens" class="w3-row assigner-div">
      <app-tokens-distribution-chart
        :assetId="initiative.ownTokens.assetId" :initiativeId="initiative.id"
        :type="type" @assigned="newAssignment($event)">
      </app-tokens-distribution-chart>
    </div>
    <div v-if="initiative.otherAssets" class="w3-row assigner-div">
      <app-tokens-distribution-chart v-for="asset in initiative.otherAssets"
        :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
        :type="type" @assigned="newAssignment($event)">
      </app-tokens-distribution-chart>
    </div>

  </div>
</template>

<script>
import InitiativeSelector from '../initiative/InitiativeSelector.vue'
import TokensDistributionChart from '../initiative/InitiativeTokensDistributionChart.vue'

export default {
  props: {
    initiative: {
      type: Object
    },
    type: {
      type: String,
      default: 'assigner'
    }
  },

  data () {
    return {
      assetsTransfers: []
    }
  },

  components: {
    'app-initiative-selector': InitiativeSelector,
    'app-tokens-distribution-chart': TokensDistributionChart
  },

  methods: {
    initiativeSelected (initiative) {
      this.axios.get('/1/secured/initiative/' + initiative.id, {
        params: {
          addAssets: true
        }
      }).then((response) => {
        this.$emit('parent-initiative-updated', response.data.data)
      })
    },
    indexOfAsset (assetId) {
      for (var ix in this.assetsTransfers) {
        if (this.assetsTransfers[ix].assetId === assetId) {
          return ix
        }
      }

      return -1
    },
    newAssignment (transferData) {
      var transferDataClean = JSON.parse(JSON.stringify(transferData))

      var ix = this.indexOfAsset(transferData.assetId)
      if (ix === -1) {
        this.assetsTransfers.push(transferDataClean)
      } else {
        this.assetsTransfers[ix] = transferDataClean
      }
      this.$emit('updated', this.assetsTransfers)
    }
  }

}
</script>

<style scoped>

.sub-initiative-first-row {
  margin-bottom: 20px;
}

</style>
