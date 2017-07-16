<template lang="html">

<div>

  <transition name="slideDownUp">
    <app-assignation-modal v-if="showAssignationModal">
    </app-assignation-modal>
  </transition>

  <div class="section-container w3-display-container"
    v-if="initiativeAssignations && initiativeTransfers">

    <div v-if="isLoggedAnAdmin" class="action-buttons w3-display-topright w3-xlarge w3-theme w3-circle w3-button">
      <i class="fa fa-plus" aria-hidden="true"></i>
      <div class="action-menu w3-dropdown-content w3-bar-block w3-card d2-color">
        <div class="w3-bar-item w3-button">
          <i class="fa fa-pencil" aria-hidden="true"></i>name and driver
        </div>
        <div class="w3-bar-item w3-button">
          <i class="fa fa-cog" aria-hidden="true"></i>notifications
        </div>
      </div>
    </div>

    <h3 class="section-header">from {{ initiative.name }}</h3>
    <app-transfers-tables v-if="hasTransfers"
      :assignations="initiativeAssignations.assignations"
      :transfers="initiativeTransfers.transfers">
    </app-transfers-tables>
    <div v-if="!hasTransfers" class="empty-div">
      no transfers have been made from {{ initiative.name }}
    </div>

    <h3 class="section-header">from subinitiatives of {{ initiative.name }}</h3>
    <app-transfers-tables v-if="hasSubinitiativesTransfers"
      :assignations="getSubassignations"
      :transfers="getSubtransfers"
      :showFrom="true">
    </app-transfers-tables>
    <div v-if="!hasSubinitiativesTransfers" class="empty-div">
      no transfers have been made from subinitiatives of {{ initiative.name }}
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

  props: {
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
  padding: 6px 0px 0px 0px  !important;
  height: 45px;
  width: 45px;
  margin-right: 30px;
  margin-top: 30px;
}

.action-menu {
  position: absolute;
}

.empty-div {
  text-align: center;
}

</style>
