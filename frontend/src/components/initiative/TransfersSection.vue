<template lang="html">

<div>

  <transition name="slideDownUp">
    <app-assignation-modal v-if="showAssignationModal">
    </app-assignation-modal>
  </transition>

  <div class="section-container w3-display-container"
    v-if="initiativeAssignations && initiativeTransfers">

    <div class="own-transfers-div">
      <h3 class="section-header">from {{ initiative.meta.name }}</h3>
      <app-transfers-tables v-if="hasTransfers"
        :assignations="initiativeAssignations.assignations"
        :transfers="initiativeTransfers.transfers">
      </app-transfers-tables>
      <div v-if="!hasTransfers" class="empty-div">
        no transfers have been made from {{ initiative.meta.name }}
      </div>
    </div>

    <div class="sub-transfers-div">
      <h3 class="section-header">from subinitiatives of {{ initiative.meta.name }}</h3>
      <app-transfers-tables v-if="hasSubinitiativesTransfers"
        :assignations="getSubassignations"
        :transfers="getSubtransfers"
        :showFrom="true">
      </app-transfers-tables>
      <div v-if="!hasSubinitiativesTransfers" class="empty-div">
        no transfers have been made from subinitiatives of {{ initiative.meta.name }}
      </div>
    </div>

    <div v-if="isLoggedAnAdmin"
      class="action-buttons w3-display-topright w3-xlarge dark-gray w3-circle w3-button"
      @click="showActionMenu = !showActionMenu">
      <i class="fa fa-plus" aria-hidden="true"></i>
    </div>
    <div v-if="showActionMenu" class="action-menu w3-display-topright">
      <div class="w3-card w3-white w3-large">
        <div class="w3-button" @click="newTransferToUser()">
          <span class="w3-left">to user</span><i class="fa fa-sign-in w3-right" aria-hidden="true"></i>
        </div>
        <div class="w3-button" @click="newTransferToInitiative()">
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

const getAllSubassignations = function (subinitiativesAssignations) {
  var subassignations = []
  subinitiativesAssignations.forEach((subinitiativeAssignations) => {
    subinitiativeAssignations.assignations.forEach((assignation) => {
      subassignations.push(assignation)
    })
  })
  return subassignations
}

const getAllSubtransfers = function (subinitiativesTransfers) {
  var subtransfers = []
  subinitiativesTransfers.forEach((subinitiativeTransfers) => {
    subinitiativeTransfers.transfers.forEach((transfer) => {
      subtransfers.push(transfer)
    })
  })
  return subtransfers
}

export default {
  components: {
    'app-transfers-tables': TransfersTables,
    'app-assignation-modal': AssignationModal
  },

  data () {
    return {
      showActionMenu: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    initiativeAssignations () {
      return this.$store.state.initiative.initiativeAssignations
    },
    initiativeTransfers () {
      return this.$store.state.initiative.initiativeTransfers
    },
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
    },
    getSubassignations () {
      return getAllSubassignations(this.initiativeAssignations.subinitiativesAssignations)
    },
    getSubtransfers () {
      return getAllSubtransfers(this.initiativeTransfers.subinitiativesTransfers)
    },
    hasTransfers () {
      return (this.initiativeAssignations.assignations.length > 0) || (this.initiativeTransfers.transfers.length > 0)
    },
    hasSubinitiativesTransfers () {
      return (this.getSubassignations.length > 0) || (this.getSubtransfers.length > 0)
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
    newTransferToUser () {
      this.showActionMenu = false
      this.$store.commit('showNewAssignationModal', true)
    },
    newTransferToInitiative () {
      this.showActionMenu = false
      this.$store.commit('showNewInitiativeTransferModal', true)
    }
  },

  mounted () {
    this.$store.dispatch('refreshTransfers')
  }
}
</script>

<style scoped>

.section-container {
  padding-top: 0px;
  padding-bottom: 25px;
}

.action-buttons {
  padding: 1px 0px 0px 0px  !important;
  height: 35px;
  width: 35px;
  margin-right: 20px;
  margin-top: -5px;
  z-index: 1;
}

.action-menu {
  padding: 6px 0px 0px 0px  !important;
  height: 45px;
  width: 45px;
  margin-right: 125px;
  margin-top: 35px;
  z-index: 1;
}

.action-menu .fa {
  margin-left: 10px;
  padding-top: 3px;
}

.action-menu .w3-card {
  width: 150px;
}

.action-menu .w3-button {
  width: 100%;
}

.empty-div {
  text-align: center;
}

.own-transfers-div {
  z-index: -1
}

</style>
