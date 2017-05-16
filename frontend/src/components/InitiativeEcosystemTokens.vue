<template lang="html">
  <div class="">
    <div class="w3-bar w3-theme-d1 w3-left-align w3-large">
      <div class="w3-bar-item">{{ totalTokens }} {{ initiative.tokenName }}</div>
      <div class="w3-bar-item w3-right w3-button" @click="mintMore()">mint more</div>
    </div>
    <div class="w3-row w3-theme-d2 w3-left-align w3-large">
      <div class="w3-col m6 w3-padding w3-display-container">
        <div class="">{{ assignedToSubinitiatives }} by subinitiatives</div>
        <div class="w3-display-right w3-button" @click="newSubInitiative()">new</div>
      </div>
      <div class="w3-col m6 w3-padding w3-theme-d3">
        <div class="">{{ assignedToUsers }} by users</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
import { amountAndPerc, tokensString } from '@/lib/common'

export default {
  computed: {
    initiative () {
      return this.$store.state.activeInitiative.initiative
    },

    totalTokens () {
      return tokensString(this.initiative.totalExistingTokens)
    },

    assignedToSubinitiatives () {
      return amountAndPerc(this.initiative.totalAssignedToOtherInitiatives, this.initiative.totalExistingTokens)
    },

    assignedToUsers () {
      return amountAndPerc(this.initiative.totalAssignedToUsers, this.initiative.totalExistingTokens)
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
