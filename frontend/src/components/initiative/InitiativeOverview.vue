<template lang="html">
  <div class="">
    <app-new-assignation-modal
      v-if="showNewAssignationModal"
      :initiativeId="initiative.id"
      @close-this="showNewAssignationModal = false"
      @assignation-done="$emit('please-update')">
    </app-new-assignation-modal>

    <div v-if="initiative" class="this-container w3-container w3-padding">
      <div class="w3-card">
        <header class="w3-container w3-theme-l2">
          <h4 class="w3-left">Driver</h4>
          <div v-if="isLoggedAnAdmin" class="edit-btn-div w3-button w3-right w3-large">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </div>
        </header>
        <div class="w3-container">
          <p>{{ initiative.driver }}</p>
        </div>
      </div>
      <br>
      <div class="w3-card">
        <header class="w3-container w3-theme-l2">
          <h4>Assets</h4>
        </header>
        <div class="tokens-div">
          <div v-if="hasOwnTokens">
            <app-tokens-distribution-chart
              :assetId="initiative.ownTokens.assetId" :initiativeId="initiative.id"
              :canMint="true" :canEdit="isLoggedAnAdmin"
              @new-initiative="$emit('new-initiative', $event)" @new-assignment="newAssignment($event)"
              @please-update="$emit('please-update')">
            </app-tokens-distribution-chart>
          </div>
          <div v-if="hasOtherAssets">
            <app-tokens-distribution-chart v-for="asset in initiative.otherAssets"
              :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
              :canMint="false" :canEdit="isLoggedAnAdmin"
              @new-initiative="$emit('new-initiative', $event)" @new-assignment="newAssignment($event)"
              @please-update="$emit('please-update')">
            </app-tokens-distribution-chart>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import InitiativeTokensDistributionChart from './InitiativeTokensDistributionChart.vue'
import NewAssignationModal from '../modal/NewAssignationModal.vue'

export default {
  components: {
    'app-tokens-distribution-chart': InitiativeTokensDistributionChart,
    'app-new-assignation-modal': NewAssignationModal
  },

  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      showNewAssignationModal: false,
      parentInitiativeIdForModal: null
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
    },
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
    newAssignment (data) {
      this.showNewAssignationModal = true
    }
  }
}
</script>

<style scoped>

.this-container {
  padding-top: 25px !important;
  padding-bottom: 25px !important;
}

.tokens-div {
  padding-top: 15px;
}

.edit-btn-div {
  padding-top: 15px;
}

</style>
