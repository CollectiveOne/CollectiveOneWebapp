<template lang="html">
  <div v-if="initiativeAssignations" class="w3-container this-container">

    <div v-if="isLoggedAnAdmin" class="w3-row action-buttons">
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="$emit('new-assignation')">new transfer to user(s)</button>
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="$emit('new-transfer-to-initiative')">new transfer to initiative</button>
      <hr>
    </div>

    <div class="w3-card section-card">
      <header class="w3-container w3-theme noselect">
        <h4>Transfers from {{ initiative.name }}</h4>
      </header>

      <label class="w3-text-indigo"><b>To users</b></label>
      <div v-if="initiativeAssignations.assignations.length > 0" class="w3-row-padding assignations-container">
        <div class="w3-col l6" v-for="assignation in initiativeAssignations.assignations">
          <app-initiative-assignation class="assignation-card"
            :assignation="assignation" :initiative="initiative"
            :key="assignation.id" @please-update="update()">
          </app-initiative-assignation>
        </div>
      </div>
      <div v-else class="w3-padding">
        <i>(empty)</i>
      </div>

      <label class="w3-text-indigo"><b>To initiatives</b></label>
      <div v-if="initiativeTransfers.length > 0" class="w3-row-padding">
        <div class="w3-col l6" v-for="transfer in initiativeTransfers">
          <app-initiative-transfer class="assignation-card"
            :assignation="assignation" :initiative="initiative"
            :key="assignation.id" @please-update="update()">
          </app-initiative-transfer>
        </div>
      </div>
      <div v-else class="w3-padding">
        <i>(empty)</i>
      </div>

    </div>

    <div class="w3-card section-card">
      <header class="w3-container w3-theme noselect">
        <h4>Transfers in sub-initiatives</h4>
      </header>

      <div v-if="getSubassignations.length > 0" class="w3-container">
        <div class="w3-row-padding assignations-container">
          <div class="w3-col l6" v-for="assignationData in getSubassignations">
            <app-initiative-assignation class="assignation-card"
              :assignation="assignationData.assignation" :initiative="initiative"
              :key="assignationData.assignation.id" @please-update="update()">
            </app-initiative-assignation>
          </div>
        </div>
      </div>
      <div v-else class="w3-padding">
        <i>(empty)</i>
      </div>
    </div>


</div>
</template>

<script>
import InitiativeAssignation from './InitiativeAssignation.vue'
import NewAssignationModal from '../modal/NewAssignationModal.vue'

const getIndexOfAssignation = function (list, id) {
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
      var ix = getIndexOfAssignation(subassignations, assignation.id)
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

export default {
  components: {
    'app-initiative-assignation': InitiativeAssignation,
    'app-new-assignation-modal': NewAssignationModal
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

.section-card {
  margin-bottom: 25px;
}

.assignation-card {
  margin-bottom: 20px;
}

.this-container {
  padding-top: 25px;
  padding-bottom: 25px;
}

.assignations-container {
  margin-top: 20px;
}

.action-buttons {
  text-align: center;
}

.action-buttons button {
  width: 220px;
}

</style>
