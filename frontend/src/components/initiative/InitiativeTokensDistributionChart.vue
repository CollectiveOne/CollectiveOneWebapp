<template lang="html">
  <div class="this-container">
    <div class="w3-row">
      <div class="w3-col" :class="{'s8': isAssigner, 's12': !isAssigner}">
        <div class="w3-row-padding">
          <div class="w3-col m4 w3-center">
            <div class="w3-display-container" :class="{tall: !isAssigner, short: isAssigner}">
              <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
              <div class="w3-display-middle d2-color" style="width: 100%">
                <div class="w3-row">
                  <b class="w3-xlarge ">{{ ownedByThisInitiativeAndSubinitiativesStr }} {{ assetData.assetName }}</b>
                </div>
                <div class="w3-row">
                  <b class="w3-large">{{ ownedByThisInitiativeAndSubinitiativesPercent }}% of existing</b>
                </div>
              </div>
            </div>
          </div>
          <div class="w3-col m8">
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

            <div class="w3-row">
              <div class="w3-col m10">
                <label class="d2-color">
                  <b>Transferred to sub-initiatives</b>
                </label>
                <div class="w3-light-grey w3-round-xlarge w3-large">
                  <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': ownedBySubinitiativesPercent +'%'}">
                    <div class="bar-txt w3-center d2-color">{{ ownedBySubinitiativesStr }}</div>
                  </div>
                </div>
              </div>

              <div v-if="!isAssigner" class="w3-col m2 w3-center" style="padding-top: 15px !important;">
                <button type="button" class="w3-button w3-theme-l1 w3-round w3-small"
                  @click="newSubInitiativeClicked">
                  new
                </button>
              </div>
            </div>

            <div v-if="!isAssigner" class="w3-row">
              <div class="w3-col m10">
                <label class="d2-color">
                  <b>Transferred to members</b>
                </label>
                <div class="w3-light-grey w3-round-xlarge w3-large">
                  <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': 0 +'%'}">
                    <div class="bar-txt w3-center d2-color">0 / 0%</div>
                  </div>
                </div>
              </div>

              <div class="w3-col m2 w3-center" style="padding-top: 15px !important;">
                <button type="button" class="w3-button w3-theme-l1 w3-round w3-small"
                  @click="newSubInitiativeClicked">
                  new
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="w3-row">
          <div v-if="hasSubinitiatives" class="sub-initiatives">
            <div class="" v-if="showSubinitiatives">
              <div class="w3-row" v-for="subinitiativeAssets in assetData.ownedBySubinitiatives" >
                <label class="d2-color">
                  <b>{{ subinitiativeAssets.holderName }}</b>
                </label>
                <div class="w3-light-grey w3-round-xlarge w3-large">
                  <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': subinitiativePercent(subinitiativeAssets) +'%'}">
                    <div class="bar-txt w3-center d2-color">{{ subinitiativePortion(subinitiativeAssets) }}</div>
                  </div>
                </div>
              </div>
              <br>
              <div class="w3-row">
                <button type="button" class="w3-button w3-theme-l4 w3-round w3-small" style="width: 100%"
                  @click="showSubinitiatives = false">hide subinitiatives
                </button>
              </div>
            </div>
            <div v-else>
              <button type="button" class="w3-button w3-theme-l4 w3-round w3-small" style="width: 100%"
                @click="showSubinitiatives = true">show subinitiatives
              </button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="isAssigner" class="assigner-container w3-col s4 d2-color">
        <div class="w3-row label-row">
          <label class="w3-text-indigo"><b>Select amount</b></label>
        </div>
        <div class="w3-row-padding">
          <div class="w3-col s6">
            <input v-model.number="tokens" class="w3-input w3-border w3-hover-light-gray w3-round" type="number">
          </div>
          <div class="w3-col s6">
            <div class="w3-row">
              <div class="w3-col s10">
                <input v-model.number="percentage" class="w3-input w3-border w3-hover-light-gray w3-round" type="text">
              </div>
              <div class="w3-col s2">
                <i class="fa fa-percent" aria-hidden="true"></i>
              </div>
            </div>
          </div>
        </div>
        <br>
        <div class="w3-row w3-center">
          <button class="w3-button w3-theme w3-round">assign</button>
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
    },
    type: {
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
      },
      showSubinitiatives: false,
      tokens: 0,
      percentage: 0
    }
  },

  computed: {
    isAssigner () {
      return this.type === 'assigner'
    },
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
      return tokensString(this.ownedByThisInitiativeAndSubinitiativesVal)
    },
    ownedByThisInitiativeAndSubinitiativesPercent () {
      return this.ownedByThisInitiativeAndSubinitiativesVal / this.assetData.totalExistingTokens * 100
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
    newSubInitiativeClicked () {
      this.$emit('new-initiative', this.assetData.holderId)
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
    },
    tokens () {
      let perc = this.tokens / this.ownedByThisInitiativeAndSubinitiativesVal * 100
      this.percentage = Math.round(perc * 1000) / 1000
    },
    percentage () {
      let toks = this.percentage / 100 * this.ownedByThisInitiativeAndSubinitiativesVal
      this.tokens = Math.round(toks * 1000) / 1000
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

.fa-certificate {
  font-size: 100px;
}

.tall {
  height: 150px;
}

.short {
  height: 100px;
}

.assigner-container {
  padding-top: 0px;
}

.label-row {
  margin-bottom: 10px;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.bar-txt {
  white-space: nowrap;
}

.sub-initiatives {
  padding-top: 15px;
  padding-left: 30px;
  padding-right: 30px;
}




</style>
