<template lang="html">
  <div class="w3-col m12">
    <div class="w3-row w3-theme-d3 w3-large">
      <div class="w3-col s9 w3-padding">{{ ownedByThisInitiative }} {{ name }} available</div>
      <div class="w3-col s3 w3-button" @click="mintMore()">add</div>
    </div>
    <div class="w3-row w3-theme-d2 w3-large" v-if="hasSubinitiatives">
      <div class="w3-col s12">
        <div class="w3-row">
            <div class="w3-col s9 w3-padding">{{ ownedBySubinitiativesStr }} {{ name }} transferred to sub-initiatives</div>
            <div class="w3-col s3 w3-button" @click="newSubInitiative()">new</div>
        </div>

        <div class="w3-col s12 w3-padding w3-theme-d1" v-for="subinitiative in subinitiatives" >
          {{ subinitiativePortion(subinitiative) }} in {{ subinitiative.initiativeName }}
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { mapMutations, mapActions } from 'vuex'
import { tokensString, amountAndPerc } from '@/lib/common'

const sumOfSubinitiatives = function (subInitiativesTokenData) {
  var tot = 0.0
  for (var ix in subInitiativesTokenData) {
    tot += subInitiativesTokenData[ix].ownedByThisInitiative + sumOfSubinitiatives(subInitiativesTokenData[ix].ownedBySubinitiatives)
  }
  return tot
}

export default {
  props: {
    tokenData: {
      type: Object,
      default: () => {
        return {
          assetId: '',
          assetName: 'token',
          totalExistingTokens: 1.0,
          initiativeId: '',
          initiativeName: 'Initiative Name',
          ownedByThisInitiative: 0.0,
          ownedBySubinitiatives: []
        }
      }
    }
  },

  computed: {
    name () {
      return this.tokenData.name
    },
    ownedByThisInitiative () {
      return tokensString(this.tokenData.ownedByThisInitiative)
    },
    hasSubinitiatives () {
      return (this.tokenData.ownedBySubinitiatives.length > 0)
    },
    ownedBySubinitiativesVal () {
      return sumOfSubinitiatives(this.tokenData.ownedBySubinitiatives)
    },
    ownedBySubinitiativesStr () {
      return tokensString(this.ownedBySubinitiativesVal)
    },
    subinitiatives () {
      return this.tokenData.ownedBySubinitiatives
    }
  },

  methods: {
    ...mapMutations(['showNewInitiativeModal']),
    ...mapActions(['getNewInitiativeParent']),

    newSubInitiative () {
      this.getNewInitiativeParent(this.tokenData.initiativeId)
      this.showNewInitiativeModal(true)
    },

    subinitiativePortion (thisSubinitiative) {
      return amountAndPerc(thisSubinitiative.ownedByThisInitiative, this.ownedBySubinitiativesVal)
    }
  }
}
</script>

<style scoped>
</style>
