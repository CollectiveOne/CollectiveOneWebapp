<template lang="html">
  <div v-if="initiative" class="this-container w3-container w3-padding">
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <h4>Overview</h4>
      </header>
      <div class="w3-container">
        <p>{{ initiative.driver }}</p>
      </div>
    </div>
    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <h4>Tokens</h4>
      </header>
      <div v-if="hasOwnTokens">
        <app-tokens-distribution-chart
          :assetId="this.initiative.ownTokens.assetId" :initiativeId="this.initiative.id"
          @new-initiative="$emit('new-initiative', $event)">
        </app-tokens-distribution-chart>
      </div>
      <div v-if="hasOtherAssets">
        <app-tokens-distribution-chart
          :assetId="this.initiative.otherAssets[0].assetId" :initiativeId="this.initiative.id"
          @new-initiative="$emit('new-initiative', $event)">
        </app-tokens-distribution-chart>
      </div>
    </div>
    <br>
    <div class="w3-card">
      <header class="w3-container w3-theme-l2">
        <div class="w3-row">
          <div class="w3-col s10">
            <h4>Members</h4>
          </div>
          <div class="w3-col s2" style="padding-top: 6px;">
            <button type="button" class="w3-button w3-right w3-theme-l1 w3-round"
              @click="$emit('new-member', initiative.id)">add / edit</button>
          </div>
        </div>


      </header>
      <div class="w3-container">
        <div v-for="contributor in this.initiative.contributors" :key="contributor.user.c1Id">
          <app-user-avatar :userData="contributor.user"></app-user-avatar>
        </div>
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
    'app-tokens-distribution-chart': InitiativeTokensDistributionChart,
    'app-user-avatar': UserAvatar
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
