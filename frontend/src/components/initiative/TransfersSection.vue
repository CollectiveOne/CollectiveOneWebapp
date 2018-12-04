<template lang="html">

<div class="transfer-section">

  <transition name="slideDownUp">
    <app-new-token-modal
      v-if="showNewTokenModal"
      :initiativeId="initiative.id"
      @close="tokenModalClosed()">
    </app-new-token-modal>
  </transition>

  <transition name="slideDownUp">
    <app-assignation-modal v-if="showAssignationModal">
    </app-assignation-modal>
  </transition>

  <transition name="slideDownUp">
    <app-new-assignation-modal v-if="showNewAssignationModal"
      :initiative="initiative"
      @close="showNewAssignationModal = false"
      @created="triggerUpdateCall()">
    </app-new-assignation-modal>
  </transition>

  <transition name="slideDownUp">
    <app-new-initiative-transfer-modal
      v-if="showNewInitiativeTransferModal"
      :initiative="initiative"
      @close="showNewInitiativeTransferModal = false"
      @created="triggerUpdateCall()">
    </app-new-initiative-transfer-modal>
  </transition>

  <div class="section-container w3-display-container">

    <div class="assets-div">
      <div class="w3-card">

        <header class="section-header-bar w3-bar gray-1">
          <h4 class="w3-bar-item w3-left">{{ $t('tokens.TOKENS')}} </h4>

          <popper :append-to-body="true" trigger="click" :options="popperOptions" class="" :toggleShow="toggleDropdown">
            <div class="">
              <app-drop-down-menu
                class="drop-menu"
                @create-new="createNewToken()"
                @toggle-deleted="toggleDeleted()"
                :items="assetsMenuItems">
              </app-drop-down-menu>
            </div>

            <div slot="reference" class="expand-btn w3-xlarge fa-button w3-right">
              <i class="fa fa-bars" aria-hidden="true"></i>
            </div>
          </popper>

        </header>

        <div class="assets-content-div">
          <div v-if="assets.length === 0 && !loading && isLoggedAnAdmin" class="w3-center">
            {{ $t('tokens.NO_ASSETS_HELD') }}<br>
            <button class="w3-button app-button w3-margin-top" name="button"
              @click="showNewTokenModal = true">
              {{ $t('general.CREATE_NEW') }}
            </button>
          </div>
          <div v-if="!loading" class="">
            <div class="w3-row" v-for="(asset, ix) in assets" :key="asset.assetId" >
              <hr v-if="ix > 0">
              <app-asset-distribution-chart
                :assetId="asset.assetId" :initiativeId="initiative.id"
                :canMint="initiative.ownAssetsIds.includes(asset.assetId)" :canEdit="isLoggedAnAdmin"
                @new-assignation="newAssignation($event)"
                @new-transfer="newTransfer($event)"
                @please-update="updateAssets()">
              </app-asset-distribution-chart>
            </div>
          </div>
          <div v-else class="w3-row w3-center loader-gif-container">
            <img class="loader-gif" src="../../assets/loading.gif" alt="">
          </div>
        </div>
      </div>
    </div>

    <div class="own-transfers-div">
      <h3 class="section-header">{{ $t('tokens.TRANSFERS_HISTORY_FROM', { initName: initiative.meta.name }) }}:</h3>
      <app-transfers-tables
        :initiativeId="initiative.id"
        :ofSubinitiatives="false"
        :triggerUpdate="triggerUpdate">
      </app-transfers-tables>
    </div>

    <div v-if="hasSubinitiatives" class="sub-transfers-div">
      <hr>
      <h3 class="section-header">{{ $t('tokens.TRANSFERS_HISTORY_FROM_SUBINITIATIVES_OF', { initName: initiative.meta.name }) }}:</h3>
      <app-transfers-tables
        :initiativeId="initiative.id"
        :ofSubinitiatives="true"
        :triggerUpdate="triggerUpdate">
      </app-transfers-tables>
    </div>

  </div>
</div>
</template>

<script>
import NewTokenModal from '@/components/modal/NewTokenModal.vue'
import AssetDistributionChart from '@/components/transfers/AssetDistributionChart.vue'
import AssignationModal from '@/components/transfers/AssignationModal.vue'
import TransfersTables from '@/components/transfers/TransfersTables.vue'
import NewInitiativeTransferModal from '@/components/modal/NewInitiativeTransferModal.vue'
import NewAssignationModal from '@/components/modal/NewAssignationModal.vue'

export default {
  components: {
    'app-new-token-modal': NewTokenModal,
    'app-asset-distribution-chart': AssetDistributionChart,
    'app-transfers-tables': TransfersTables,
    'app-assignation-modal': AssignationModal,
    'app-new-initiative-transfer-modal': NewInitiativeTransferModal,
    'app-new-assignation-modal': NewAssignationModal
  },

  data () {
    return {
      showNewTokenModal: false,
      showActionMenu: false,
      assetIdToTransfer: '',
      showNewAssignationModal: false,
      showNewInitiativeTransferModal: false,
      triggerUpdate: false,
      assets: [],
      showDeleted: false,
      loading: false,
      toggleDropdown: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    hasSubinitiatives () {
      return this.initiative.subInitiatives.length > 0
    },
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    showAssignationModal () {
      /* based on the route */
      for (var ix in this.$route.matched) {
        let e = this.$route.matched[ix]
        if (e.name === 'InitiativeAssignation') {
          return true
        }
      }
      return false
    },
    assetsMenuItems () {
      let items = []

      items.push({
        text: this.$t('general.CREATE_NEW'),
        value: 'create-new',
        faIcon: 'fa-plus'
      })

      items.push({
        text: !this.showDeleted ? this.$t('tokens.SHOW_DELETED') : this.$t('tokens.HIDE_DELETED'),
        value: 'toggle-deleted',
        faIcon: !this.showDeleted ? 'fa-eye' : 'fa-ban'
      })

      return items
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: false
          },
          flip: {
            enabled: false
          }
        }
      }
    }
  },

  methods: {
    toggleDeleted () {
      this.showDeleted = !this.showDeleted
      this.updateAssets()
      this.toggleDropdown = !this.toggleDropdown
    },
    updateAssets () {
      this.loading = true
      this.axios.get('/1/initiative/' + this.initiative.id + '/assets', {
        params: {
          addDeleted: this.showDeleted
        }
      }).then((response) => {
        this.loading = false
        this.assets = response.data.data
      })
    },
    triggerUpdateCall () {
      this.triggerUpdate = !this.triggerUpdate
    },
    tokenModalClosed () {
      this.showNewTokenModal = false
      this.updateAssets()
    },
    newAssignation (assetData) {
      this.assetIdToTransfer = assetData.assetId
      this.showNewAssignationModal = true
    },
    newTransfer (assetData) {
      this.assetIdToTransfer = assetData.assetId
      this.showNewInitiativeTransferModal = true
    },
    clickOutsideShowMenu () {
      this.showActionMenu = false
    },
    createNewToken () {
      this.showNewTokenModal = true
      this.toggleDropdown = !this.toggleDropdown
    }
  },

  created () {
    this.updateAssets()
  }
}
</script>

<style scoped>

.assets-div {
  padding: 20px 20px;
}

.expand-btn {
  width: 60px;
  padding-top: 15px;
  height: 66px;
  text-align: center;
  color: white;
}

.drop-menu {
  width: 200px;
}

.transfer-section {
  overflow: auto;
}

.section-container {
  padding-top: 0px;
  padding-bottom: 25px;
  min-height: 200px;
}

.assets-content-div {
  padding: 25px 0px;
}

.empty-div {
  text-align: center;
}

.own-transfers-div {
  z-index: -1
}

.action-menu {
  padding: 6px 0px 0px 0px  !important;
  margin-left: -120%;
  margin-top: 100px;
}

.action-menu .fa {
  margin-left: 10px;
  padding-top: 3px;
}

.action-menu .w3-card {
  width: 250px;
}

.action-menu .w3-button {
  width: 100%;
}

</style>
