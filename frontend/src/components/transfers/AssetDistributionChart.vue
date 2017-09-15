<template lang="html">

  <div v-if="assetData" class="">
    <div class="this-container">
      <div class="w3-row">
        <div class="w3-col distribution-container" :class="{'l8': showAssigner, 'l12': !showAssigner}">
          <div class="w3-row-padding">
            <div class="w3-col l4 w3-center">
              <div class="w3-display-container" :class="{tall: isOverview, short: !isOverview}">
                <div class="w3-display-middle" style="width: 100%">
                  <div class="w3-row tokens-row">
                    {{ underThisInitiativeStr }} {{ assetData.assetName }}
                  </div>
                  <div class="w3-row percentage-row">
                    {{ underThisInitiativePercent }}% of existing
                  </div>
                </div>
                <div v-if="canEdit && canMint " class="w3-button w3-display-bottommiddle" @click="mintClicked()">
                  <i class="fa fa-plus-circle gray-1-color" aria-hidden="true"></i>
                </div>
              </div>
            </div>
            <div class="w3-col l8">
              <div class="w3-row w3-margin-bottom">
                <label class="">
                  <b>Still Available</b>
                </label>
                <div class="light-grey w3-round-xlarge w3-large">
                  <div class="app-blue w3-container w3-center w3-round-xlarge" :style="{'width': availableToThisInitiativePercent +'%'}">
                    <div class="bar-txt w3-center noselect">{{ availableToThisInitiative }}</div>
                  </div>
                </div>
              </div>

              <div v-if="hasPending" class="w3-row w3-margin-bottom">
                <label class="">
                  <b>Under pending peer-review transfers</b>
                </label>
                <div class="light-grey w3-round-xlarge w3-large">
                  <div class="app-blue w3-container w3-center w3-round-xlarge" :style="{'width': pendingTransfersPercent +'%'}">
                    <div class="bar-txt w3-center noselect">{{ pendingTransfers }}</div>
                  </div>
                </div>
              </div>

              <div v-if="isInitiativeAssigner || isOverview" class="w3-row w3-margin-bottom">
                <div class="w3-col" :class="{'s10' : canEdit, 's12' : !canEdit}">
                  <div class="w3-row cursor-pointer" @click="showSubinitiatives = !showSubinitiatives">
                    <label class="cursor-pointer noselect">
                      <b>Transferred to sub-initiatives</b>
                    </label>
                    <div class="light-grey w3-round-xlarge w3-large">
                      <div class="app-blue w3-container w3-center w3-round-xlarge" :style="{'width': transferredToSubinitiativesPercent +'%'}">
                        <div class="bar-txt w3-center noselect">{{ transferredToSubinitiativesStr }}</div>
                      </div>
                    </div>
                  </div>

                  <div class="w3-row" v-if="isOverview || isInitiativeAssigner">
                    <div class="slider-container" v-if="hasSubinitiatives">
                      <transition name="slideDownUp">
                        <div class="sub-elements" v-if="showSubinitiatives">
                          <router-link tag="div"
                            :to="{name: 'Initiative', params: {'initiativeId': subinitiativeAssets.receiverId}}"
                            class="w3-row cursor-pointer"
                            v-for="subinitiativeAssets in assetData.transferredToSubinitiatives"
                            :key="subinitiativeAssets.assetId">

                            <label class="">
                              <b class="noselect"><i class="fa fa-circle" aria-hidden="true" :style="{'color': $store.getters.colorOfInitiative(subinitiativeAssets.receiverId)}"></i>  {{ subinitiativeAssets.receiverName }}</b>
                            </label>
                            <div class="light-grey w3-round-xlarge w3-large">
                              <div class="app-blue w3-container w3-center w3-round-xlarge" :style="{'width': subinitiativePercent(subinitiativeAssets) +'%'}">
                                <div class="bar-txt w3-center noselect">{{ subinitiativePortion(subinitiativeAssets) }}</div>
                              </div>
                            </div>
                          </router-link>
                        </div>
                      </transition>
                    </div>
                  </div>
                </div>

                <div v-if="isOverview && canEdit" class="w3-col s2 w3-center icon-div">
                  <button type="button" class="w3-button"
                    @click="$store.commit('showNewInitiativeTransferModal', true)">
                    <i class="fa fa-plus" aria-hidden="true"></i>
                  </button>
                </div>
              </div>

              <div v-if="isOverview || isMemberAssigner" class="w3-row">
                <div class="w3-col" :class="{'s10' : canEdit, 's12' : !canEdit}">

                  <div class="w3-row cursor-pointer" @click="showMembers = !showMembers">
                    <label class="cursor-pointer">
                      <b class="noselect">Transferred to members</b>
                    </label>
                    <div class="light-grey w3-round-xlarge w3-large">
                      <div class="app-blue w3-container w3-center w3-round-xlarge" :style="{'width': transferredToMembersPercent +'%'}">
                        <div class="bar-txt w3-center noselect">{{ transferredToMembersStr }}</div>
                      </div>
                    </div>
                  </div>

                  <div v-if="isOverview" class="w3-row">
                    <div v-if="hasMembers" class="slider-container">
                      <transition name="slideDownUp">
                        <div class="sub-elements" v-if="showMembers">
                          <div class="w3-row" v-for="memberAssets in assetData.transferredToUsers" v-if="memberAssets.value > 0">
                            <label class="">
                              <b class="noselect">{{ memberAssets.receiverName }}</b>
                            </label>
                            <div class="light-grey w3-round-xlarge w3-large">
                              <div class="app-blue w3-container w3-center w3-round-xlarge" :style="{'width': memberPercent(memberAssets) +'%'}">
                                <div class="bar-txt w3-center noselect">{{ memberPortion(memberAssets) }}</div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </transition>
                    </div>
                  </div>
                </div>

                <div v-if="isOverview && canEdit" class="w3-col s2 w3-center icon-div">
                  <button type="button" class="w3-button"
                    @click="$store.commit('showNewAssignationModal', true)">
                    <i class="fa fa-plus" aria-hidden="true"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>

        </div>
        <div v-if="showAssigner" class="assigner-container w3-col l4 w3-container">
          <div class="w3-row label-row">
            <label class=""><b>Amount to be transfered</b></label>
          </div>
          <div class="w3-row-padding w3-large">
            <div class="w3-col s6">
              <input @input="valueUpdated($event)" :value="value.toFixed(1)"  class="w3-input w3-border w3-hover-light-grey w3-round"
              :class="{ 'error-input' : errorFound }" type="number" min="0">
            </div>
            <div class="w3-col s6">
              <div class="w3-row">
                <div class="w3-col s10">
                  <input @input="percentageUpdated($event)" :value="percentage.toFixed(1)" class="w3-input w3-border w3-hover-light-grey w3-round"
                  :class="{ 'error-input' : errorFound }" type="number" min="0" step="5">
                </div>
                <div class="w3-col s2">
                  <i class="fa fa-percent" aria-hidden="true"></i>
                </div>
              </div>
            </div>
          </div>
          <div class="slider-container error-row w3-center">
            <transition name="slideDownUp">
              <div v-if="valueTooLarge" class="w3-row error-panel w3-tag w3-round">
                {{ this.assetData.ownedByThisHolder > 0 ? 'only ' : ''}} {{this.assetData.ownedByThisHolder.toFixed(1) }} {{ this.assetData.assetName }} available
              </div>
            </transition>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import { tokensString, amountAndPerc } from '@/lib/common'

export default {
  components: {
  },

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
    },
    canMint: {
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
      assetData: null,
      showSubinitiatives: false,
      showMembers: false,
      value: 0,
      percentage: 0,
      valueTooLarge: false
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
      return this.assetData.totalExistingTokens > 0 ? tokensString(this.underThisInitiativeVal / this.assetData.totalExistingTokens * 100) : 0
    },
    hasPending () {
      return this.assetData.totalPending > 0
    },
    availableToThisInitiativeVal () {
      return this.assetData.ownedByThisHolder - this.assetData.totalPending
    },
    availableToThisInitiative () {
      return amountAndPerc(this.availableToThisInitiativeVal, this.underThisInitiativeVal)
    },
    availableToThisInitiativePercent () {
      return this.availableToThisInitiativeVal / this.underThisInitiativeVal * 100
    },
    pendingTransfers () {
      return amountAndPerc(this.assetData.totalPending, this.underThisInitiativeVal)
    },
    pendingTransfersPercent () {
      return this.assetData.totalPending / this.underThisInitiativeVal * 100
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
    hasMembers () {
      return (this.assetData.transferredToUsers.length > 0)
    },
    transferredToMembersVal () {
      return this.assetData.totalTransferredToUsers
    },
    transferredToMembersPercent () {
      return this.transferredToMembersVal / this.underThisInitiativeVal * 100
    },
    transferredToMembersStr () {
      return amountAndPerc(this.transferredToMembersVal, this.underThisInitiativeVal)
    },
    errorFound () {
      return this.showError || this.valueTooLarge
    }
  },

  methods: {
    subinitiativePortion (subinitiativeAssets) {
      return amountAndPerc(subinitiativeAssets.value, this.underThisInitiativeVal)
    },
    subinitiativePercent (subinitiativeAssets) {
      return (subinitiativeAssets.value) / this.underThisInitiativeVal * 100
    },
    memberPortion (memberAssets) {
      return amountAndPerc(memberAssets.value, this.underThisInitiativeVal)
    },
    memberPercent (memberAssets) {
      return this.underThisInitiativeVal > 0 ? (memberAssets.value) / this.underThisInitiativeVal * 100 : 0
    },
    updateTokenData () {
      this.axios.get('/1/token/' + this.assetId, {
        params: {
          includeSubinitiatives: true,
          initiativeIdStr: this.initiativeId
        }
      }).then((response) => {
        this.assetData = response.data.data
        this.checkValue()
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
    },
    mintClicked () {
      this.$store.commit('showNewTokenMintModal', {
        show: true,
        assetId: this.assetData.assetId
      })
    },
    valueUpdated (event) {
      this.value = Number(event.target.value)
      this.percentage = this.value / this.underThisInitiativeVal * 100
      this.checkValue()
    },
    percentageUpdated (event) {
      this.percentage = Number(event.target.value)
      this.value = this.percentage / 100 * this.underThisInitiativeVal
      this.checkValue()
    },
    checkValue () {
      if (this.value <= this.assetData.ownedByThisHolder) {
        this.valueTooLarge = false
        this.assign()
      } else {
        this.valueTooLarge = true
      }
    }
  },

  watch: {
    assetId () {
      this.updateTokenData()
    },
    initiativeId () {
      this.updateTokenData()
    },
    '$store.state.support.triggerUpdateAssets' () {
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
  padding-bottom: 10px;
}

.fa-plus-circle {
  font-size: 30px;
}

.tall {
  height: 150px;
}

.short {
  height: 100px;
}

.tokens-row {
  font-size: 26px;
  font-style: normal;
  font-weight: 900;
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
  margin-top: 11px;
  margin-left: 5px;
}

.error-row {
  margin-top: 10px;
  width: 100%;
}

.bar-txt {
  white-space: nowrap;
}

.sub-elements {
  padding-left: 30px;
  margin-top: 4px;
  margin-bottom: 10px;
}

</style>
