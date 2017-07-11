<template lang="html">
  <div v-if="initiative" class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-left">
        <h5 class="w3-text-indigo w3-left"><b>Transfer from </b></h5>
      </div>
      <div class="w3-left">
        <app-initiative-selector class="initiative-selector"
          anchor="id" label="name" :init="initiative"
          url="/1/secured/initiatives/suggestions"
          @select="initiativeSelected($event)">
        </app-initiative-selector>
      </div>
    </div>
    <div v-if="initiative.ownTokens" class="w3-row assigner-div">
      <app-asset-distribution-chart
        :assetId="initiative.ownTokens.assetId" :initiativeId="initiative.id"
        :type="type" @assigned="newAssignment($event)">
      </app-asset-distribution-chart>
    </div>
    <div v-if="initiative.otherAssets" class="w3-row assigner-div">
      <app-asset-distribution-chart v-for="asset in initiative.otherAssets"
        :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
        :type="type" @assigned="newAssignment($event)">
      </app-asset-distribution-chart>
    </div>

  </div>
</template>

<script>
import InitiativeSelector from '@/components/initiative/InitiativeSelector.vue'
import AssetDistributionChart from '@/components/transfers/AssetDistributionChart.vue'

export default {
  props: {
    initInitiativeId: {
      type: String
    },
    type: {
      type: String,
      default: 'initiative-assigner'
    }
  },

  data () {
    return {
      initiative: null,
      assetsTransfers: []
    }
  },

  components: {
    'app-initiative-selector': InitiativeSelector,
    'app-asset-distribution-chart': AssetDistributionChart
  },

  methods: {
    initiativeSelected (initiative) {
      this.updateInitiative(initiative.id)
    },
    updateInitiative (id) {
      this.axios.get('/1/secured/initiative/' + id, {
        params: {
          addAssets: true
        }
      }).then((response) => {
        this.initiative = response.data.data
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
  },

  mounted () {
    this.updateInitiative(this.initInitiativeId)
  }
}
</script>

<style scoped>

.sub-initiative-first-row {
  margin-bottom: 20px;
}

</style>
