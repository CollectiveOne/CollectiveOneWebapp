<template lang="html">

<div>

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
      @close="showNewInitiativeTransferModal = false"
      @created="triggerUpdateCall()">
    </app-new-initiative-transfer-modal>
  </transition>

  <div class="section-container w3-display-container">

    <div class="own-transfers-div">
      <h3 class="section-header">From {{ initiative.meta.name }}:</h3>
      <app-transfers-tables
        :initiativeId="initiative.id"
        :ofSubinitiatives="false"
        :triggerUpdate="triggerUpdate">
      </app-transfers-tables>
    </div>

    <div v-if="hasSubinitiatives" class="sub-transfers-div">
      <hr>
      <h3 class="section-header">From subinitiatives of {{ initiative.meta.name }}:</h3>
      <app-transfers-tables
        :initiativeId="initiative.id"
        :ofSubinitiatives="true"
        :triggerUpdate="triggerUpdate">
      </app-transfers-tables>
    </div>

    <div v-if="isLoggedAnAdmin && initiative.ownAssetsIds.length > 0"
      class="w3-display-topright w3-xxlarge w3-button plus-button gray-1-color"
      @click="showActionMenu = !showActionMenu"
      v-click-outside="clickOutsideShowMenu">

      <i class="fa fa-plus-circle" aria-hidden="true"></i>
    </div>
    <div v-if="showActionMenu" class="action-menu w3-display-topright">
      <div class="w3-card w3-white w3-large">
        <div class="w3-button" @click="newTransferToUser()">
          <span class="w3-left">to user</span><i class="fa fa-sign-in w3-right" aria-hidden="true"></i>
        </div>
        <div v-if="hasSubinitiatives" class="w3-button" @click="newTransferToInitiative()">
          <span class="w3-left">to initiative</span><i class="fa fa-sign-in w3-right" aria-hidden="true"></i>
        </div>
      </div>
    </div>

  </div>
</div>
</template>

<script>
import AssignationModal from '@/components/transfers/AssignationModal.vue'
import TransfersTables from '@/components/transfers/TransfersTables.vue'
import NewInitiativeTransferModal from '@/components/modal/NewInitiativeTransferModal.vue'
import NewAssignationModal from '@/components/modal/NewAssignationModal.vue'

export default {
  components: {
    'app-transfers-tables': TransfersTables,
    'app-assignation-modal': AssignationModal,
    'app-new-initiative-transfer-modal': NewInitiativeTransferModal,
    'app-new-assignation-modal': NewAssignationModal
  },

  data () {
    return {
      showActionMenu: false,
      showNewAssignationModal: false,
      showNewInitiativeTransferModal: false,
      triggerUpdate: false
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
    }
  },

  methods: {
    triggerUpdateCall () {
      this.triggerUpdate = !this.triggerUpdate
    },
    newTransferToUser () {
      this.showNewAssignationModal = true
    },
    newTransferToInitiative () {
      this.showNewInitiativeTransferModal = true
    },
    clickOutsideShowMenu () {
      this.showActionMenu = false
    }
  }
}
</script>

<style scoped>

.section-container {
  padding-top: 0px;
  padding-bottom: 25px;
  min-height: 200px;
}

.empty-div {
  text-align: center;
}

.own-transfers-div {
  z-index: -1
}

</style>
