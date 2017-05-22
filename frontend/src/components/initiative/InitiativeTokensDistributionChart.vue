<template lang="html">
  <div class="this-container">
    <div class="w3-row w3-padding w3-theme-d1 w3-large w3-theme-d4"><b>{{ assetData.assetName }}</b></div>
    <div class="w3-row w3-padding w3-theme-d3">{{ ownedByThisInitiativeAndSubinitiativesStr }} held by "{{ assetData.holderName }}"{{ assetData.ownedBySubinitiatives.length > 0 ? ' and subinitiatives' : '' }}, of which:</div>
    <div class="first-level-distr w3-row w3-padding w3-theme-d2">{{ ownedByThisInitiative }} are still available {{ assetData.ownedBySubinitiatives.length > 0 ? 'and' : '' }}</div>
    <div class="first-level-distr w3-row w3-padding w3-theme-d2" v-if=" assetData.ownedBySubinitiatives.length > 0">{{ ownedBySubinitiativesStr }} were transferred to subinitiatives, of which:</div>
    <div class="second-level-distr w3-row w3-padding w3-theme-d1" v-for="subinitiativeAssets in assetData.ownedBySubinitiatives" >
      {{ subinitiativePortion(subinitiativeAssets) }} to "{{ subinitiativeAssets.holderName }}"
    </div>
  </div>
</template>

<script>
import { tokensString, amountAndPerc } from '@/lib/common'

const sumOfSubinitiatives = function (asset) {
  var tot = 0.0
  for (var ixAss in asset.ownedBySubinitiatives) {
    var subInitiativeAsset = asset.ownedBySubinitiatives[ixAss]
    /* recurively sum the assets owned by subinitiatives */
    tot += subInitiativeAsset.ownedByThisHolder + sumOfSubinitiatives(subInitiativeAsset)
  }
  return tot
}

export default {
  props: {
    assetId: {
      type: String
    },
    initiativeId: {
      type: String
    }
  },

  data () {
    return {
      assetData: {
        assetName: '',
        totalExistingTokens: 100,
        holderName: 'holder name',
        ownedByThisHolder: 10,
        ownedBySubinitiatives: []
      }
    }
  },

  computed: {
    name () {
      return this.assetData.assetName
    },
    totalExisting () {
      return tokensString(this.assetData.totalExistingTokens)
    },
    ownedByThisInitiativeAndSubinitiativesVal () {
      return this.assetData.ownedByThisHolder + this.ownedBySubinitiativesVal
    },
    ownedByThisInitiativeAndSubinitiativesStr () {
      return amountAndPerc(this.ownedByThisInitiativeAndSubinitiativesVal, this.assetData.totalExistingTokens)
    },
    ownedByThisInitiative () {
      return amountAndPerc(this.assetData.ownedByThisHolder, this.ownedByThisInitiativeAndSubinitiativesVal)
    },
    hasSubinitiatives () {
      return (this.assetData.ownedBySubinitiatives.length > 0)
    },
    ownedBySubinitiativesVal () {
      return sumOfSubinitiatives(this.assetData)
    },
    ownedBySubinitiativesStr () {
      return amountAndPerc(this.ownedBySubinitiativesVal, this.ownedByThisInitiativeAndSubinitiativesVal)
    }
  },

  methods: {
    subinitiativePortion (subinitiativeAssets) {
      return amountAndPerc(subinitiativeAssets.ownedByThisHolder + sumOfSubinitiatives(subinitiativeAssets), this.ownedByThisInitiativeAndSubinitiativesVal)
    },
    updateTokenData () {
      this.axios.get('/1/secured/token/' + this.assetId, {
        params: {
          initiativeId: this.initiativeId
        }
      }).then((response) => {
        this.assetData = response.data.data
      })
    }
  },

  watch: {
    assetId () {
      this.updateTokenData()
    },
    initiativeId () {
      this.updateTokenData()
    }
  },

  mounted () {
    this.updateTokenData()
  }
}
</script>

<style scoped>

.asset-avatar {
  text-align: center;
}

.first-level-distr {
  padding-left: 35px !important;
}

.second-level-distr {
  padding-left: 70px !important;
}


</style>
