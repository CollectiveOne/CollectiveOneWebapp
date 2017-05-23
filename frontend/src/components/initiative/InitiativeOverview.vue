<template lang="html">
  <div v-if="initiative" class="this-container w3-container w3-padding">
    <div class="w3-card">
      <header class="w3-container w3-theme">
        <h4>Overview</h4>
      </header>
      <div class="w3-container">
        <p>{{ initiative.driver }}</p>
      </div>
    </div>
    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme">
        <h4>Tokens</h4>
      </header>
      <div v-if="hasOwnTokens">
        <app-tokens-distribution-chart :assetId="this.initiative.ownTokens.assetId" :initiativeId="this.initiative.id"></app-tokens-distribution-chart>
      </div>
      <div v-if="hasOtherAssets">
        <app-tokens-distribution-chart :assetId="this.initiative.otherAssets[0].assetId" :initiativeId="this.initiative.id"></app-tokens-distribution-chart>
      </div>
    </div>
    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme">
        <h4>Members</h4>
      </header>
      <div>
        <app-user-avatar v-for="user in this.initiative.contributors" :key="user.c1Id"></app-user-avatar>
      </div>
    </div>
    <br>
  </div>
</template>

<script>
import InitiativeTokensDistributionChart from './InitiativeTokensDistributionChart.vue'
import UserAvatar from '../user/UserAvatar.vue'

export default {
  components: {
    AppTokensDistributionChart: InitiativeTokensDistributionChart,
    AppUserAvatar: UserAvatar
  },

  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      showNewInitiativeModal: false,
      parentInitiativeIdForModal: null
    }
  },

  computed: {
    hasOwnTokens () {
      if (this.initiative.ownTokens) {
        return true
      } else {
        return false
      }
    },
    hasOtherAssets () {
      if (this.initiative.otherAssets) {
        if (this.initiative.otherAssets.length > 0) {
          return true
        }
      }
      return false
    }
  },

  methods: {
    newSubInitiativeClicked (initiativeId) {
      this.parentInitiativeIdForModal = initiativeId
      this.showNewInitiativeModal = true
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
}

</style>
