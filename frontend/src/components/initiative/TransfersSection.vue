<template lang="html">

<div>

  <transition name="slideDownUp">
    <app-assignation-modal v-if="showAssignationModal">
    </app-assignation-modal>
  </transition>

  <div v-if="initiativeAssignations && initiativeTransfers" class="w3-container section-container">

    <div v-if="isLoggedAnAdmin" class="w3-row action-buttons">
      <button class="w3-button w3-theme w3-round" type="button" name="button"
        @click="$store.commit('showNewAssignationModal', true)">
        transfer to member(s)
      </button>
      <button class="w3-button w3-theme w3-round" type="button" name="button"
        @click="$store.commit('showNewInitiativeTransferModal', true)">
        transfer to subinitiative
      </button>
      <hr class="separator">
    </div>

    <div v-if="hasTransfers" class="w3-card d2-color section-card">
      <header class="w3-container noselect">
        <h4><b>Transfers from {{ initiative.name }}</b></h4>
        <hr>
      </header>

      <div class="w3-container card-content">

        <div v-if="initiativeAssignations.assignations.length > 0">
          <div class="w3-row">
            <label class="d2-color"><b>To users</b></label>
          </div>
          <div class="w3-row-padding assignations-container">
            <div class="w3-col l6" v-for="assignation in initiativeAssignations.assignations" :key="assignation.id">
              <app-initiative-assignation class="assignation-card"
                :assignation="assignation">
              </app-initiative-assignation>
            </div>
          </div>
        </div>

        <div v-if="initiativeTransfers.transfers.length > 0">
          <div class="w3-row">
            <label class="d2-color"><b>To initiatives</b></label>
          </div>
          <div v-if="initiativeTransfers.transfers.length > 0" class="w3-row-padding assignations-container">
            <div class="w3-col l6" v-for="transfer in initiativeTransfers.transfers" :key="transfer.id">
              <app-initiative-transfer class="assignation-card"
                :transfer="transfer">
              </app-initiative-transfer>
            </div>
          </div>
        </div>

      </div>

    </div>

    <div v-if="hasSubinitiativesTransfers" class="w3-card section-card">

      <header class="w3-container w3-theme noselect">
        <h4>Transfers from sub-initiatives of {{ initiative.name }}</h4>
      </header>


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
import AssignationBox from '@/components/transfers/AssignationBox.vue'
import InitiativeTransfer from '@/components/transfers/InitiativeTransfer.vue'
import AssignationModal from '@/components/transfers/AssignationModal.vue'

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
    'app-initiative-assignation': AssignationBox,
    'app-initiative-transfer': InitiativeTransfer,
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

  mounted () {
    this.$store.dispatch('refreshTransfers')
  }
}
</script>

<style scoped>

.section-container {
  padding-top: 25px;
  padding-bottom: 15px;
}

.action-buttons {
  text-align: center;
}

.action-buttons button {
  width: 220px;
  margin-bottom: 10px;
}

.separator {
  margin-top: 10px;
}

.section-card {
  margin-bottom: 25px;
}

.card-content {
  padding-top: 20px;
}

.assignations-container {
  padding-top: 25px;
  padding-bottom: 10px;
}

.assignation-card {
  margin-bottom: 20px;
}

</style>
