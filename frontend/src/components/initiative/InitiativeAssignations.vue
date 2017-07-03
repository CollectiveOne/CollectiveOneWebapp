<template lang="html">
  <div v-if="assignations" class="w3-container this-container">

    <div v-if="isLoggedAnAdmin" class="w3-row action-buttons">
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="$emit('new-assignation')">transfer to user(s)</button>
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="$emit('new-transfer-to-initiative')">transfer to initiative</button>
    </div>

    <label class="w3-text-indigo"><b>Transfers in {{ initiative.name }}</b></label>
    <div v-if="assignations.assignations.length > 0" class="">
      <div class="w3-row-padding assignations-container">
        <div class="w3-col l6" v-for="assignation in assignations.assignations">
          <app-initiative-assignation class="assignation-card"
            :assignation="assignation" :initiative="initiative"
            :key="assignation.id" @please-update="update()">
          </app-initiative-assignation>
        </div>
      </div>
    </div>
    <div v-else class="">
      <div class="w3-panel w3-padding warning-panel">
        no transfers have been made in this initiative
      </div>
    </div>

    <hr>
    <label class="w3-text-indigo"><b>Transfers in sub-initiatives</b></label>
    <div v-if="getSubassignations.length > 0" class="">
      <div class="w3-row-padding assignations-container">
        <div class="w3-col l6" v-for="assignationData in getSubassignations">
          <app-initiative-assignation class="assignation-card"
            :assignation="assignationData.assignation" :initiative="initiative"
            :key="assignationData.assignation.id" @please-update="update()">
          </app-initiative-assignation>
        </div>
      </div>
    </div>
    <div v-else class="">
      <div class="w3-panel w3-padding warning-panel">
        no transfers have been made in any sub-initiative
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
      assignations: null
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
    },
    getSubassignations () {
      return getAllSubassignations(this.assignations.subinitiativesAssignations)
    }
  },

  methods: {
    update () {
      this.axios.get('/1/secured/initiative/' + this.initiative.id + '/assignations').then((response) => {
        this.assignations = response.data.data
      })
    }
  },

  mounted () {
    this.update()
  }
}
</script>

<style scoped>

.assignation-card {
  margin-bottom: 20px;
}

.this-container {
  padding-top: 25px;
  padding-bottom: 25px;
}

.assignations-container {
  margin-top: 10px;
}

.action-buttons {
  text-align: center;
}

.action-buttons button {
  width: 220px;
}

</style>
