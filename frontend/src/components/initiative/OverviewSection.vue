<template lang="html">
  <div class="">
    <div v-if="initiative" class="this-container w3-container w3-padding">
      <div class="w3-card">
        <header class="w3-bar gray-1 section-header-bar">
          <h4 class="w3-bar-item w3-left">Driver</h4>
          <div v-if="isLoggedAnAdmin"
            class="edit-btn-div w3-bar-item w3-button w3-right w3-large"
            @click="$store.commit('showEditInitiativeModal', true)">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </div>
        </header>
        <div class="w3-container section-content">
          {{ initiative.meta.driver }}
        </div>
      </div>
      <br>
      <div class="w3-card">
        <header class="section-header-bar w3-bar gray-1">
          <h4 class="w3-bar-item w3-left">Assets</h4>
        </header>
        <div class="section-content">
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

.section-header-bar {
  height: 65px;
}

.section-content {
  padding-top: 25px;
  padding-bottom: 15px;
}

.edit-btn-div {
  padding-top: 20px;
  width: 60px;
  height: 100%
}


</style>
