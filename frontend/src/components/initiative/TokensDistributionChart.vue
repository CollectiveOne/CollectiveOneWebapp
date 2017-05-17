<template lang="html">
  <div class="">
    <div class="w3-bar w3-theme-d1 w3-left-align w3-large">
      <div class="w3-bar-item">{{ ownedByThisInitiative }} {{ name }} owned by this initiative</div>
      <div class="w3-bar-item w3-right w3-button" @click="mintMore()">add</div>
    </div>
    <div v-if="hasSubinitiatives" class="w3-bar w3-theme-d1 w3-left-align w3-large">
      <div class="w3-bar-item">{{ ownedBySubinitiatives }} {{ name }} owned by sub-initiatives</div>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
import { tokensString } from '@/lib/common'

const sumOfSubinitiatives = function (subInitiativesTokenData) {
  debugger
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
    ownedBySubinitiatives () {
      return sumOfSubinitiatives(this.tokenData.ownedBySubinitiatives)
    }
  },

  methods: {
    ...mapMutations(['showNewInitiativeModal', 'setNewInitiativeParent']),

    newSubInitiative () {
      this.setNewInitiativeParent(this.initiative)
      this.showNewInitiativeModal(true)
    }
  }
}
</script>

<style scoped>
</style>
