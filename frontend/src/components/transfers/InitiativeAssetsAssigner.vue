<template lang="html">
  <div v-if="initiative" class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-left">
        <h5 class="w3-left"><b>Transfer from {{ initiative.meta.name }}</b></h5>
      </div>
    </div>

    <div v-if="initiative.assets.length > 0" class="w3-row assigner-div">
      <div v-if="showSelector" class="">
        <div class="w3-row">
          <label for=""><b>Asset to transfer</b></label>
          <select v-model="assetIdSelected" class="w3-input" name="">
            <option v-for="asset in initiative.assets" :value="asset.assetId">{{ asset.assetName }}</option>
          </select>
        </div>
        <br>
        <div class="w3-row">
          <app-asset-distribution-chart :assetId="assetIdSelected" :initiativeId="initiative.id"
            :type="type" @assigned="newAssignment($event)" :showError="showError">
          </app-asset-distribution-chart>
        </div>
      </div>
      <div v-else class="slider-container">
        <transition-group name="fadeenter" mode="out-in">
          <div class="" v-for="(asset, ix) in initiative.assets" :key="asset.assetId">
            <hr v-if="ix > 0">
            <app-asset-distribution-chart :assetId="asset.assetId" :initiativeId="initiative.id"
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

  components: {
    'app-initiative-selector': InitiativeSelector,
    'app-asset-distribution-chart': AssetDistributionChart
  },

  props: {
    initiativeId: {
      type: String
    },
    assetId: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'initiative-assigner'
    },
    showSelector: {
      type: Boolean,
      default: false
    },
    showError: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      initiative: null,
      assetsTransfers: [],
      assetIdSelected: ''
    }
  },

  watch: {
    assetIdSelected () {
      if (this.showSelector) {
        this.assetsTransfers = []
        this.$emit('updated', this.assetsTransfers)
      }
    }
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
        if (this.assetIdSelected === '') {
          this.assetIdSelected = this.initiative.assets[0].assetId
        }
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
    if (this.assetId !== '') {
      this.assetIdSelected = this.assetId
    }
    this.updateInitiative()
  }
}
</script>

<style scoped>

.sub-initiative-first-row {
  margin-bottom: 20px;
}

</style>
