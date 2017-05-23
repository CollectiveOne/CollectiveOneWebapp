<template lang="html">
  <div class="w3-container this-container">
    <div class="total w3-row w3-padding w3-center">
      <i class="fa fa-certificate l2-color" aria-hidden="true"></i>
      <div class="total-owned-txt w3-xlarge d2-color">
        <b>{{ ownedByThisInitiativeAndSubinitiativesStr }} {{ assetData.assetName }}</b>
      </div>
    </div>

    <hr>
    <div class="w3-row">
      <label class="d2-color">
        <b>Still Available</b>
      </label>
      <div class="w3-light-grey w3-round-xlarge w3-large">
        <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': ownedByThisInitiativePercent +'%'}">
          <div class="bar-txt w3-center d2-color">{{ ownedByThisInitiative }}</div>
        </div>
      </div>
    </div>

    <hr>
    <div class="w3-row">
      <label class="d2-color">
        <b>Transferred to sub-initiatives</b>
      </label>
      <div class="w3-light-grey w3-round-xlarge w3-large">
        <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': ownedBySubinitiativesPercent +'%'}">
          <div class="bar-txt w3-center d2-color">{{ ownedBySubinitiativesStr }}</div>
        </div>
      </div>
      <div class="sub-initiatives">
        <div class="second-level-distr w3-col s12 w3-padding" v-for="subinitiativeAssets in assetData.ownedBySubinitiatives" >
          <label class="d2-color">
            <b>{{ subinitiativeAssets.holderName }}</b>
          </label>
          <div class="w3-light-grey w3-round-xlarge w3-large">
            <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': subinitiativePercent(subinitiativeAssets) +'%'}">
              <div class="bar-txt w3-center d2-color">{{ subinitiativePortion(subinitiativeAssets) }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <hr>
    <div class="w3-row">
      <label class="d2-color">
        <b>Transferred to members</b>
      </label>
      <div class="w3-light-grey w3-round-xlarge w3-large">
        <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': 0 +'%'}">
          <div class="bar-txt w3-center d2-color">0 / 0%</div>
        </div>
      </div>
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
    ownedByThisInitiativePercent () {
      return this.assetData.ownedByThisHolder / this.ownedByThisInitiativeAndSubinitiativesVal * 100
    },
    hasSubinitiatives () {
      return (this.assetData.ownedBySubinitiatives.length > 0)
    },
    ownedBySubinitiativesVal () {
      return sumOfSubinitiatives(this.assetData)
    },
    ownedBySubinitiativesPercent () {
      return this.ownedBySubinitiativesVal / this.ownedByThisInitiativeAndSubinitiativesVal * 100
    },
    ownedBySubinitiativesStr () {
      return amountAndPerc(this.ownedBySubinitiativesVal, this.ownedByThisInitiativeAndSubinitiativesVal)
    }
  },

  methods: {
    subinitiativePortion (subinitiativeAssets) {
      return amountAndPerc(subinitiativeAssets.ownedByThisHolder + sumOfSubinitiatives(subinitiativeAssets), this.ownedByThisInitiativeAndSubinitiativesVal)
    },
    subinitiativePercent (subinitiativeAssets) {
      return (subinitiativeAssets.ownedByThisHolder + sumOfSubinitiatives(subinitiativeAssets)) / this.ownedByThisInitiativeAndSubinitiativesVal * 100
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

.this-container {
  padding-bottom: 30px;
}

.total {
  padding-top: 30px !important;
}

.fa-certificate {
  font-size: 72px;
}

.bar-txt {
  white-space: nowrap;
}

.sub-initiatives {
  padding-top: 15px;
  padding-left: 30px;
}




</style>
