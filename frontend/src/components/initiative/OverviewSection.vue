<template lang="html">
  <div class="">
    <div v-if="initiative" class="this-container w3-container w3-padding">
      <div class="w3-card">
        <header class="w3-container w3-theme">
          <h4 class="w3-left">Driver</h4>
          <div v-if="isLoggedAnAdmin" class="edit-btn-div w3-button w3-right w3-large" @click="$store.commit('showEditInitiativeModal', true)">
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
          <app-asset-distribution-chart v-for="asset in initiative.assets"
            :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
            :canMint="initiative.ownAssetsId === asset.assetId" :canEdit="isLoggedAnAdmin">
          </app-asset-distribution-chart>
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
  },

  data () {
    return {
      showEditInitiativeModal: false,
      parentInitiativeIdForModal: null
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
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
