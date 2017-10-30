<template lang="html">
  <div v-if="initiative" class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-left">
        <h5 class="w3-left"><b>Transfer from {{ initiative.meta.name }}</b></h5>
      </div>
    </div>

    <div v-if="initiative.assets" class="w3-row assigner-div">
      <div class="slider-container">
        <transition-group name="fadeenter" mode="out-in">
          <div class="" v-for="(assetId, ix) in initiative.assetsIds" :key="assetId">
            <hr v-if="ix > 0">
            <app-asset-distribution-chart :assetId="assetId" :initiativeId="initiative.id"
              :type="type" @assigned="newAssignment($event)" :showError="showError">
            </app-asset-distribution-chart>
          </div>
        </transition-group>
      </div>
    </div>

  </div>
</template>

<script>
import InitiativeSelector from '@/components/initiative/InitiativeSelector.vue'
import AssetDistributionChart from '@/components/transfers/AssetDistributionChart.vue'

export default {
  props: {
    initiativeId: {
      type: String
    },
    type: {
      type: String,
      default: 'initiative-assigner'
    },
    showError: {
      type: Boolean,
      default: false
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
    updateInitiative () {
      this.axios.get('/1/initiative/' + this.initiativeId, {
        params: {
          addAssetsIds: true
        }
      }).then((response) => {
        this.initiative = response.data.data
        this.$emit('initiative-updated', this.initiative)
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
    this.updateInitiative()
  }
}
</script>

<style scoped>

.sub-initiative-first-row {
  margin-bottom: 20px;
}

</style>
