<template lang="html">
  <div class="w3-col m12">
    <div class="w3-row w3-theme-d3 w3-large">
      <div class="w3-col s12 w3-padding">{{ totalExisting }} {{ name }} existing</div>
    </div>
    <div class="w3-row w3-theme-d3 w3-large">
      <div class="w3-col s12 w3-padding">{{ ownedByThisInitiative }} {{ name }} held by this initiative</div>
    </div>
    <div class="w3-row w3-theme-d2 w3-large" v-if="hasSubinitiatives">
      <div class="w3-col s12">
        <div class="w3-row">
            <div class="w3-col s12 w3-padding">{{ ownedBySubinitiativesStr }} {{ name }} transferred to sub-initiatives</div>
        </div>
        <div class="w3-col s12 w3-padding w3-theme-d1" v-for="subinitiativeAssets in assetData.ownedBySubinitiatives" >
          {{ subinitiativePortion(subinitiativeAssets) }} in {{ subinitiativeAssets.holderName }}
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { tokensString, amountAndPerc } from '@/lib/common'

const sumOfSubinitiatives = function (subInitiativesAssets) {
  var tot = 0.0
  for (var ixAss in subInitiativesAssets) {
    tot += subInitiativesAssets[ixAss].ownedByThisHolder
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
    ownedByThisInitiative () {
      return tokensString(this.assetData.ownedByThisHolder)
    },
    hasSubinitiatives () {
      return (this.assetData.ownedBySubinitiatives.length > 0)
    },
    ownedBySubinitiativesVal () {
      return sumOfSubinitiatives(this.assetData.ownedBySubinitiatives)
    },
    ownedBySubinitiativesStr () {
      return tokensString(this.ownedBySubinitiativesVal)
    }
  },

  methods: {
    subinitiativePortion (subinitiativeAssets) {
      return amountAndPerc(subinitiativeAssets.ownedByThisHolder, this.ownedBySubinitiativesVal)
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
</style>
