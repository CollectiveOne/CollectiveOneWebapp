<template lang="html">
  <div class="">

    <transition name="slideDownUp">
      <app-new-token-modal
        v-if="showNewTokenModal"
        :initiativeId="initiative.id"
        @close="showNewTokenModal = false">
      </app-new-token-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-tokenexchange-modal
        v-if="showTokensExchangeModal"
        @close="showTokensExchangeModal = false">
      </app-new-tokenexchange-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-assignation-modal v-if="showNewAssignationModal"
        :initiative="initiative"
        :assetId="assetIdToTransfer"
        :showSelector="true"
        @close="showNewAssignationModal = false">
      </app-new-assignation-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-initiative-transfer-modal
        v-if="showNewInitiativeTransferModal"
        :initiative="initiative"
        :assetId="assetIdToTransfer"
        :showSelector="true"
        @close="showNewInitiativeTransferModal = false">
      </app-new-initiative-transfer-modal>
    </transition>

    <div v-if="initiative.meta.imageFile" class="image-container w3-center">
      <img :src="initiative.meta.imageFile.url + '?lastUpdated=' + initiative.meta.imageFile.lastUpdated" alt="">
    </div>

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
      <div class="w3-card w3-display-container">
        <header class="section-header-bar w3-bar gray-1">
          <h4 class="w3-bar-item w3-left">Assets</h4>
          <div v-if="isLoggedAnAdmin" class="edit-btn-div w3-bar-item w3-button w3-right w3-large"
            @click="showAssetsMenu = !showAssetsMenu"
            v-click-outside="clickOutsideShowAssetsMenu">
            <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
          </div>
        </header>
        <div v-if="showAssetsMenu" class="assets-menu w3-display-topright w3-card w3-white">
          <div v-if="isLoggedAnAdmin"
            @click="showNewTokenModal = true"
            class="w3-button">
            <i class="fa fa-plus" aria-hidden="true"></i>create new
          </div>
          <!-- <div v-if="isLoggedAnAdmin"
            @click="showTokensExchangeModal = true"
            class="w3-button">
            <i class="fa fa-exchange" aria-hidden="true"></i>exchange/convert
          </div> -->
        </div>
        <div class="assets-content">
          <div v-if="initiative.assets.length === 0" class="w3-center">
            <i>No assets are currently held by this initiative</i>
          </div>
          <div class="w3-row" v-for="(asset, ix) in initiative.assets" :key="asset.assetId" >
            <hr v-if="ix > 0">
            <app-asset-distribution-chart
              :assetId="asset.assetId" :initiativeId="initiative.id"
              :canMint="initiative.ownAssetsIds.includes(asset.assetId)" :canEdit="isLoggedAnAdmin"
              @new-assignation="newAssignation($event)"
              @new-transfer="newTransfer($event)">
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
import InitiativeTag from '@/components/initiative/InitiativeTag.vue'
import NewTokenModal from '@/components/modal/NewTokenModal.vue'
import NewTokenExchangeModal from '@/components/modal/NewTokenExchangeModal.vue'
import NewAssignationModal from '@/components/modal/NewAssignationModal.vue'
import NewInitiativeTransferModal from '@/components/modal/NewInitiativeTransferModal.vue'

export default {
  components: {
    'app-asset-distribution-chart': AssetDistributionChart,
    'app-edit-initiative-modal': EditInitiativeModal,
    'app-initiative-tag': InitiativeTag,
    'app-new-token-modal': NewTokenModal,
    'app-new-tokenexchange-modal': NewTokenExchangeModal,
    'app-new-assignation-modal': NewAssignationModal,
    'app-new-initiative-transfer-modal': NewInitiativeTransferModal
  },

  props: {
  },

  data () {
    return {
      showEditInitiativeModal: false,
      showAssetsMenu: false,
      parentInitiativeIdForModal: null,
      showNewTokenModal: false,
      showTokensExchangeModal: false,
      showNewAssignationModal: false,
      showNewInitiativeTransferModal: false,
      assetIdToTransfer: ''
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
    },
    newAssignation (assetData) {
      this.assetIdToTransfer = assetData.assetId
      this.showNewAssignationModal = true
    },
    newTransfer (assetData) {
      this.assetIdToTransfer = assetData.assetId
      this.showNewInitiativeTransferModal = true
    },
    clickOutsideShowAssetsMenu () {
      this.showAssetsMenu = false
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

.image-container {
  margin-top: 16px;
  min-height: 80px;
  max-height: 250px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.assets-menu {
  margin-top: 65px;
  margin-right: 2px;
  width: 220px;
  display: block;
  z-index: 10;
}

.assets-menu div {
  text-align: left;
  width: 100%;
}

.assets-menu .fa {
  margin-right: 15px;
}

.assets-content {
  padding-top: 16px;
  padding-bottom: 16px;
  overflow-y: auto;
}

.activity-content {
  padding-top: 16px;
  padding-bottom: 16px;
  max-height: 35vh;
  overflow-y: auto;
}

.edit-btn-div {
  padding-top: 20px;
  width: 60px;
  height: 100%
}

</style>
