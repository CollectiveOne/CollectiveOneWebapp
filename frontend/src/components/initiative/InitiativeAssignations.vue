<template lang="html">
  <div v-if="initiativeAssignations && initiativeTransfers" class="w3-container section-container">

    <div v-if="isLoggedAnAdmin" class="w3-row action-buttons">
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="$emit('new-assignation')">new transfer to user(s)</button>
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="$emit('new-transfer-to-initiative')">new transfer to initiative</button>
      <hr class="separator">
    </div>

    <div v-if="hasTransfers" class="w3-card section-card">
      <header class="w3-container w3-theme noselect">
        <h4>Transfers from {{ initiative.name }}</h4>
      </header>

      <div class="w3-container card-content">

        <div v-if="initiativeAssignations.assignations.length > 0">
          <div class="w3-row">
            <label class="w3-text-indigo"><b>To users</b></label>
          </div>
          <div class="w3-row-padding assignations-container">
            <div class="w3-col l6" v-for="assignation in initiativeAssignations.assignations">
              <app-initiative-assignation class="assignation-card"
                :assignation="assignation"
                :key="assignation.id" @please-update="update()">
              </app-initiative-assignation>
            </div>
          </div>
        </div>

        <div v-if="initiativeTransfers.transfers.length > 0">
          <div class="w3-row">
            <label class="w3-text-indigo"><b>To initiatives</b></label>
          </div>
          <div v-if="initiativeTransfers.transfers.length > 0" class="w3-row-padding assignations-container">
            <div class="w3-col l6" v-for="transfer in initiativeTransfers.transfers">
              <app-initiative-transfer class="assignation-card"
                :transfer="transfer" :key="transfer.id">
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
              <div class="w3-col l6" v-for="assignationData in getSubassignations">
                <app-initiative-assignation class="assignation-card"
                  :assignation="assignationData.assignation"
                  :key="assignationData.assignation.id" @please-update="update()">
                </app-initiative-assignation>
              </div>
            </div>
          </div>
        </div>

        <div v-if="getSubtransfers.length > 0" class="">
          <div class="w3-row">
            <label class="w3-text-indigo"><b>To initiatives</b></label>
          </div>
          <div class="w3-container">
            <div class="w3-row-padding assignations-container">
              <div class="w3-col l6" v-for="transferData in getSubtransfers">
                <app-initiative-transfer class="assignation-card"
                  :transfer="transferData.transfer" :key="transferData.transfer.id">
                </app-initiative-transfer>
              </div>
            </div>
          </div>
        </div>

      </div>

    </div>


</div>
</template>

<script>
import InitiativeAssignation from './InitiativeAssignation.vue'
import InitiativeTransfer from './InitiativeTransfer.vue'

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
    'app-initiative-assignation': InitiativeAssignation,
    'app-initiative-transfer': InitiativeTransfer
  },

  props: {
    initiative: null
  },

  data () {
    return {
      showNewAssignationModal: false,
      initiativeAssignations: null,
      initiativeTransfers: null
    }
  },

  computed: {
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
    }
  },

  methods: {
    update () {
      this.axios.get('/1/secured/initiative/' + this.initiative.id + '/assignations').then((response) => {
        this.initiativeAssignations = response.data.data
      })

      this.axios.get('/1/secured/initiative/' + this.initiative.id + '/transfersToInitiatives').then((response) => {
        this.initiativeTransfers = response.data.data
      })
    }
  },

  mounted () {
    this.update()
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
