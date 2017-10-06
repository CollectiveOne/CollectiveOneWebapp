<template lang="html">
  <div class="">
    <div v-if="initiative" class="this-container w3-container w3-padding">
      <div class="w3-card">
        <header class="w3-bar gray-1 section-header-bar">
          <h4 class="w3-bar-item w3-left">Purpose</h4>
          <div v-if="isLoggedAnAdmin"
            class="edit-btn-div w3-bar-item w3-button w3-right w3-large"
            @click="$store.commit('showEditInitiativeModal', true)">
            <i class="fa fa-pencil" aria-hidden="true"></i>
          </div>
        </header>
        <div class="w3-container section-content">
          <div class="w3-row">
            <vue-markdown class="marked-text" :source="initiative.meta.driver"></vue-markdown>
          </div>
          <hr v-if="initiative.meta.tags.length > 0">
          <div class="w3-row w3-margin-top">
            <app-initiative-tag
              v-for="tag in initiative.meta.tags"
              :tag="tag"
              :key="tag.id"
              class="tags-containers">
            </app-initiative-tag>
          </div>
        </div>
      </div>

      <br>
      <div class="w3-card">
        <header class="section-header-bar w3-bar gray-1">
          <h4 class="w3-bar-item w3-left">Assets</h4>
        </header>
        <div class="activity-content">
          <app-asset-distribution-chart v-for="asset in initiative.assets"
            :key="asset.assetId" :assetId="asset.assetId" :initiativeId="initiative.id"
            :canMint="initiative.ownAssetsId === asset.assetId" :canEdit="isLoggedAnAdmin">
          </app-asset-distribution-chart>
        </div>
      </div>

      <br>
      <div class="w3-card">
        <header class="section-header-bar w3-bar gray-1">
          <h4 class="w3-bar-item w3-left">Recent Activity</h4>
        </header>
        <div class="activity-content">
          <app-activity-getter :url="'/1/activity/initiative/' + initiative.id">
          </app-activity-getter>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AssetDistributionChart from '@/components/transfers/AssetDistributionChart.vue'
import EditInitiativeModal from '@/components/modal/EditInitiativeModal.vue'
import InitiativeTag from '@/components/initiative/InitiativeTag.vue'
import ActivityGetter from '@/components/notifications/ActivityGetter.vue'

export default {
  components: {
    'app-asset-distribution-chart': AssetDistributionChart,
    'app-edit-initiative-modal': EditInitiativeModal,
    'app-initiative-tag': InitiativeTag,
    'app-activity-getter': ActivityGetter
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
      return this.$store.getters.isLoggedAnAdmin
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
  padding-top: 6px;
  padding-bottom: 0px;
}

.activity-content {
  padding-top: 16px;
  padding-bottom: 16px;
}

.edit-btn-div {
  padding-top: 20px;
  width: 60px;
  height: 100%
}

</style>
