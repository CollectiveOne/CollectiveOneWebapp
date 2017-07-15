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

    <div class="w3-row">
      <h6>to users: </h6>
      <app-assignations-table :assignations="initiativeAssignations.assignations"></app-assignations-table>

      <h6>to initiatives: </h6>
      <app-transfers-table :assignations="initiativeTransfers.assignations"></app-transfers-table>
    </div>

    <div v-if="hasSubinitiativesTransfers" class="w3-card section-card">

      <h6>to users: </h6>
      <app-assignations-table :assignations="getSubassignations"></app-assignations-table>

      <h6>to initiatives: </h6>
      <app-transfers-table :assignations="initiativeTransfers.assignations"></app-transfers-table>


      <div class="w3-container card-content">

        <div v-if="getSubassignations.length > 0" class="">
          <div class="w3-row">
            <label class="d2-color"><b>To users</b></label>
          </div>
          <div class="w3-container">
            <div class="w3-row assignations-container">
              <div class="w3-col l6" v-for="assignationData in getSubassignations" :key="assignationData.assignation.id">
                <app-initiative-assignation class="assignation-card"
                  :assignation="assignationData.assignation">
                </app-initiative-assignation>
              </div>
            </div>
          </div>
        </div>

        <div v-if="getSubtransfers.length > 0" class="">
          <div class="w3-row">
            <label class="d2-color"><b>To initiatives</b></label>
          </div>
          <div class="w3-container">
            <div class="w3-row-padding assignations-container">
              <div class="w3-col l6" v-for="transferData in getSubtransfers" :key="transferData.transfer.id">
                <app-initiative-transfer class="assignation-card"
                  :transfer="transferData.transfer">
                </app-initiative-transfer>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import AssignationModal from '@/components/transfers/AssignationModal.vue'
import AssignationsTable from '@/components/transfers/AssignationsTable.vue'

const getIndexOfElementWithId = function (list, id) {
  for (var ix in list) {
    if (list[ix].id === id) {
      return ix
    }
  }
  return -1
}

const getAllSubassignations = function (subinitiativesAssignations) {
  var subassignations = []
  subinitiativesAssignations.forEach((subinitiativeAssignations) => {
    subinitiativeAssignations.assignations.forEach((assignation) => {
      var ix = getIndexOfElementWithId(subassignations, assignation.id)
      if (ix === -1) {
        subassignations.push({
          assignation: assignation,
          subinitiative: subinitiativeAssignations.initiativeName
        })
      } else {
        subassignations[ix].subinitiative = subinitiativeAssignations.initiativeName
      }
    })
  })
  return subassignations
}

const getAllSubtransfers = function (subinitiativesTransfers) {
  var subtransfers = []
  subinitiativesTransfers.forEach((subinitiativeTransfers) => {
    subinitiativeTransfers.transfers.forEach((transfer) => {
      var ix = getIndexOfElementWithId(subtransfers, transfer.id)
      if (ix === -1) {
        subtransfers.push({
          transfer: transfer,
          subinitiative: subinitiativeTransfers.initiativeName
        })
      } else {
        subtransfers[ix].subinitiative = subinitiativeTransfers.initiativeName
      }
    })
  })
  return subtransfers
}

export default {
  components: {
    'app-assignations-table': AssignationsTable,
    'app-assignation-modal': AssignationModal
  },

  props: {
  },

  data () {
    return {
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
  },

  mounted () {
    this.$store.dispatch('refreshTransfers')
  }
}
</script>

<style scoped>

.section-container {
  padding-top: 10px;
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

.table-container {
}

</style>
