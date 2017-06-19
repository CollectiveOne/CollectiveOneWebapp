<template lang="html">
  <div class="this-container">
    <div class="w3-row">
      <div class="w3-col distribution-container" :class="{'m8': showAssigner, 'm12': !showAssigner}">
        <div class="w3-row-padding">
          <div class="w3-col m4 w3-center">
            <div class="w3-display-container" :class="{tall: isOverview, short: !isOverview}">
              <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
              <div class="w3-display-middle d2-color" style="width: 100%">
                <div class="w3-row">
                  <b class="w3-xlarge ">{{ underThisInitiativeStr }} {{ assetData.assetName }}</b>
                </div>
                <div class="w3-row">
                  <b class="w3-large">{{ underThisInitiativePercent }}% of existing</b>
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
                <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': availableToThisInitiativePercent +'%'}">
                  <div class="bar-txt w3-center d2-color">{{ availableToThisInitiative }}</div>
                </div>
              </div>
            </div>

            <div v-if="isInitiativeAssigner || isOverview" class="w3-row">
              <div class="w3-col" :class="{'s10' : canEdit, 's12' : !canEdit}">
                <label class="d2-color">
                  <b>Transferred to sub-initiatives</b>
                </label>
                <div class="w3-light-grey w3-round-xlarge w3-large">
                  <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': transferredToSubinitiativesPercent +'%'}">
                    <div class="bar-txt w3-center d2-color">{{ transferredToSubinitiativesStr }}</div>
                  </div>
                </div>
              </div>

              <div v-if="isOverview && canEdit" class="w3-col s2 w3-center icon-div">
                <button type="button" class="w3-button l2-color"
                  @click="newSubInitiativeClicked()">
                  <i class="fa fa-external-link" aria-hidden="true"></i>
                </button>
              </div>
            </div>

            <div v-if="isOverview || isMemberAssigner" class="w3-row">
              <div class="w3-col" :class="{'s10' : canEdit, 's12' : !canEdit}">
                <label class="d2-color">
                  <b>Transferred to members</b>
                </label>
                <div class="w3-light-grey w3-round-xlarge w3-large">
                  <div class="w3-container w3-center w3-round-xlarge w3-theme-l3" :style="{'width': transferredToMembersPercent +'%'}">
                    <div class="bar-txt w3-center d2-color">{{ transferredToMembersStr }}</div>
                  </div>
                </div>
              </div>

              <div v-if="isOverview && canEdit" class="w3-col s2 w3-center icon-div">
                <button type="button" class="w3-button l2-color"
                  @click="newAssignmentClicked()">
                  <i class="fa fa-external-link" aria-hidden="true"></i>
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="isOverview || isInitiativeAssigner" class="w3-row">
          <div v-if="hasSubinitiatives" class="sub-initiatives">
            <div class="" v-if="showSubinitiatives">
              <div class="w3-row" v-for="subinitiativeAssets in assetData.transferredToSubinitiatives" >
                <label class="d2-color">
                  <b>{{ subinitiativeAssets.receiverName }}</b>
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
      <div v-if="showAssigner" class="assigner-container w3-col m4 w3-container d2-color">
        <div class="w3-row label-row">
          <label class="w3-text-indigo"><b>Amount to be transfered</b></label>
        </div>
        <div class="w3-row-padding">
          <div class="w3-col s6">
            <input v-model.number="value" class="w3-input w3-border w3-hover-light-gray w3-round" type="number" min="0">
          </div>
          <div class="w3-col s6">
            <div class="w3-row">
              <div class="w3-col s10">
                <input v-model.number="percentage" class="w3-input w3-border w3-hover-light-gray w3-round" type="number" min="0" step="5">
              </div>
              <div class="w3-col s2">
                <i class="fa fa-percent" aria-hidden="true"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { tokensString, amountAndPerc } from '@/lib/common'

export default {
  props: {
    assetId: {
      type: String
    },
    initiativeId: {
      type: String
    },
    type: {
      type: String,
      default: 'overview'
    },
    canEdit: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      assetData: {
        assetName: '',
        totalExistingTokens: 100,
        holderName: 'holder name',
        ownedByThisHolder: 10,
        transferredToSubinitiatives: []
      },
      showSubinitiatives: false,
      value: 0,
      percentage: 0
    }
  },

  computed: {
    showAssigner () {
      return this.isInitiativeAssigner || this.isMemberAssigner
    },
    isInitiativeAssigner () {
      return this.type === 'initiative-assigner'
    },
    isMemberAssigner () {
      return this.type === 'member-assigner'
    },
    isOverview () {
      return this.type === 'overview'
    },
    name () {
      return this.assetData.assetName
    },
    totalExisting () {
      return tokensString(this.assetData.totalExistingTokens)
    },
    underThisInitiativeVal () {
      return this.assetData.totalUnderThisHolder
    },
    underThisInitiativeStr () {
      return tokensString(this.underThisInitiativeVal)
    },
    underThisInitiativePercent () {
      return this.underThisInitiativeVal / this.assetData.totalExistingTokens * 100
    },
    availableToThisInitiative () {
      return amountAndPerc(this.assetData.ownedByThisHolder, this.underThisInitiativeVal)
    },
    availableToThisInitiativePercent () {
      return this.assetData.ownedByThisHolder / this.underThisInitiativeVal * 100
    },
    hasSubinitiatives () {
      return (this.assetData.transferredToSubinitiatives.length > 0)
    },
    transferredToSubinitiativesVal () {
      return this.assetData.totalTransferredToSubinitiatives
    },
    transferredToSubinitiativesPercent () {
      return this.transferredToSubinitiativesVal / this.underThisInitiativeVal * 100
    },
    transferredToSubinitiativesStr () {
      return amountAndPerc(this.transferredToSubinitiativesVal, this.underThisInitiativeVal)
    },
    transferredToMembersVal () {
      return this.assetData.totalTransferredToUsers
    },
    transferredToMembersPercent () {
      return this.transferredToMembersVal / this.underThisInitiativeVal * 100
    },
    transferredToMembersStr () {
      return amountAndPerc(this.transferredToMembersVal, this.underThisInitiativeVal)
    }

  },

  methods: {
    subinitiativePortion (subinitiativeAssets) {
      return amountAndPerc(subinitiativeAssets.value, this.underThisInitiativeVal)
    },
    subinitiativePercent (subinitiativeAssets) {
      return (subinitiativeAssets.value) / this.underThisInitiativeVal * 100
    },
    newSubInitiativeClicked () {
      this.$emit('new-initiative', this.assetData.holderId)
    },
    newAssignmentClicked () {
      this.$emit('new-assignment', this.assetData.holderId)
    },
    updateTokenData () {
      this.axios.get('/1/secured/token/' + this.assetId, {
        params: {
          initiativeId: this.initiativeId
        }
      }).then((response) => {
        this.assetData = response.data.data
      })
    },
    assign () {
      var transferData = {
        assetId: this.assetData.assetId,
        assetName: this.assetData.assetName,
        senderId: this.assetData.holderId,
        senderName: this.assetData.holderName,
        value: this.value
      }

      this.$emit('assigned', transferData)
    }
  },

  watch: {
    assetId () {
      this.updateTokenData()
    },
    initiativeId () {
      this.updateTokenData()
    },
    value () {
      let perc = this.value / this.underThisInitiativeVal * 100
      this.percentage = Math.round(perc * 1000) / 1000
      this.assign()
    },
    percentage () {
      let toks = this.percentage / 100 * this.underThisInitiativeVal
      this.value = Math.round(toks * 1000) / 1000
    }

  },

  mounted () {
    this.updateTokenData()
  }
}
</script>

<style scoped>

.this-container {
  padding-bottom: 10px;
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

.distribution-container {
  margin-bottom: 10px;
}

.assigner-container {
}

.label-row {
  margin-bottom: 10px;
}

.icon-div {
  padding-top: 12px !important;
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
