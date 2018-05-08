<template lang="html">
  <div class="this-container">
    <div class="w3-row sub-initiative-first-row">
      <div class="w3-left">
        <h5 class="w3-left"><b>Transfer from {{ initiativeName }}</b></h5>
      </div>
    </div>

    <div v-if="assets.length > 0" class="w3-row assigner-div">
      <div v-if="showSelector" class="">
        <div class="w3-row">
          <label for=""><b>Asset to transfer</b></label>
          <select v-model="assetIdSelected" class="w3-input" name="">
            <option v-for="asset in assets" :value="asset.assetId">{{ asset.assetName }}</option>
          </select>
        </div>
        <br>
        <div class="w3-row">
          <app-asset-distribution-chart :assetId="assetIdSelected" :initiativeId="initiativeId"
            :type="type" @assigned="newAssignment($event)" :triggerReset="triggerReset" :showError="showError">
          </app-asset-distribution-chart>
        </div>
      </div>
      <div v-else class="slider-container">
        <transition-group name="fadeenter" mode="out-in">
          <div class="" v-for="(asset, ix) in assets" :key="asset.assetId">
            <hr v-if="ix > 0">
            <app-asset-distribution-chart :assetId="asset.assetId" :initiativeId="initiativeId"
              :type="type" @assigned="newAssignment($event)" :triggerReset="triggerReset" :showError="showError">
            </app-asset-distribution-chart>
          </div>
        </transition-group>
      </div>
    </div>
    <div v-else class="w3-center w3-row">
      <i>the parent initaitive does not have any asset</i>
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
    initiativeName: {
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
      assets: [],
      assetsTransfers: [],
      assetIdSelected: '',
      triggerReset: false
    }
  },

  watch: {
    assetIdSelected () {
      if (this.showSelector) {
        this.assetsTransfers = []
        this.triggerReset = !this.triggerReset
        this.$emit('updated', this.assetsTransfers)
      }
    }
  },

  methods: {
    updateAssets () {
      this.axios.get('/1/initiative/' + this.initiativeId, {
        params: {
          addAssetsIds: true
        }
      }).then((response) => {
        this.assets = response.data.data.assets
        if (this.assetIdSelected === '') {
          if (this.assets.length > 0) {
            this.assetIdSelected = this.assets[0].assetId
          }
        }
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
    this.updateAssets()
  }
}
</script>

<style scoped>

.sub-initiative-first-row {
  margin-bottom: 20px;
}

</style>
