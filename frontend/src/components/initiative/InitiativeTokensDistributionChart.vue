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
import { tokensString, amountAndPerc } from '@/lib/common'

const sumOfSubinitiatives = function (subInitiatives, tokenId) {
  var tot = 0.0

  for (var ixSub in subInitiatives) {
    /* Look on the assets held by the subinitiatives and sum those that
    corresponds to the this initiative tokens */
    var thisSub = subInitiatives[ixSub]
    var heldByThis = 0.0
    for (var ixAsset in this.otherAssets) {
      var thisAsset = this.otherAssets[ixAsset]
      if (thisAsset.id === tokenId) {
        heldByThis = thisAsset.tokens
      }
    }

    tot += heldByThis + sumOfSubinitiatives(thisSub.subInitiatives, tokenId)
  }
  return tot
}

export default {
  props: {
    initiativeData: {
      type: Object,
      default: () => {
        return {
          id: '',
          name: 'Initiative Name',
          ownTokens: {
            assetId: '',
            assetName: 'tokens',
            ownedByThisHolder: 0.0
          },
          subInitiatives: []
        }
      }
    }
  },

  computed: {
    name () {
      return this.tokenData.name
    },
    ownedByThisInitiative () {
      return tokensString(this.initiativeData.ownTokens.ownedByThisHolder)
    },
    hasSubinitiatives () {
      return (this.initiativeData.subInitiatives.length > 0)
    },
    ownedBySubinitiativesVal () {
      return sumOfSubinitiatives(this.initiativeData.subInitiatives)
    },
    ownedBySubinitiativesStr () {
      return tokensString(this.ownedBySubinitiativesVal)
    },
    subinitiatives () {
      return this.initiativeData.subinitiatives
    }
  },

  methods: {
    newSubInitiative () {
      this.$emit('new-subinitiative', this.initiativeData.id)
    },

    subinitiativePortion (thisSubinitiative) {
      return amountAndPerc(thisSubinitiative.ownedByThisInitiative, this.ownedBySubinitiativesVal)
    }
  }
}
</script>

<style scoped>
</style>
