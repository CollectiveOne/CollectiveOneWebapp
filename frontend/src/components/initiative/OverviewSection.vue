<template lang="html">
  <div class="">
    <div v-if="initiative" class="this-container w3-container w3-padding">
      <div class="w3-card">
        <header class="w3-container w3-theme">
          <h4 class="w3-left">Driver</h4>
          <div v-if="isLoggedAnAdmin" class="edit-btn-div w3-button w3-right w3-large" @click="showEditInitiativeModal = true">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </div>
        </header>
        <div class="w3-container">
          <p>{{ initiative.driver }}</p>
        </div>
      </div>
      <br>
      <div class="w3-card">
        <header class="w3-container w3-theme">
          <h4>Assets</h4>
        </header>
        <div class="tokens-div">
          <div v-if="hasOwnTokens">
            <app-asset-distribution-chart
              :assetId="initiative.ownTokens.assetId" :initiativeId="initiative.id"
              :canMint="true" :canEdit="isLoggedAnAdmin"
              @please-update="$emit('please-update')"
              @new-token-mint="$emit('new-token-mint')"
              @new-transfer-to-initiative="$emit('new-transfer-to-initiative')"
              @new-assignation="$emit('new-assignation')" >
            </app-asset-distribution-chart>
          </div>
          <div v-if="hasOtherAssets">
            <app-asset-distribution-chart v-for="asset in initiative.otherAssets"
              :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
              :canMint="false" :canEdit="isLoggedAnAdmin"
              @please-update="$emit('please-update')"
              @new-token-mint="$emit('new-token-mint')"
              @new-transfer-to-initiative="$emit('new-transfer-to-initiative')"
              @new-assignation="$emit('new-assignation')" >
            </app-asset-distribution-chart>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AssetDistributionChart from '@/components/transfers/AssetDistributionChart.vue'
import EditInitiativeModal from '@/components/modal/EditInitiativeModal.vue'

export default {
  components: {
    'app-asset-distribution-chart': AssetDistributionChart,
    'app-edit-initiative-modal': EditInitiativeModal
  },

  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      showEditInitiativeModal: false,
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

.fade-enter-active, .fade-leave-active {
  transition: opacity .5s
}
.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
  opacity: 0
}

</style>
