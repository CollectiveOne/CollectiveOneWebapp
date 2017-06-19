<template lang="html">
  <div class="w3-container this-container">
    <app-new-assignation-modal
      v-if="showNewAssignationModal"
      :initiativeId="initiative.id"
      @close-this="showNewAssignationModal = false"
      @assignation-done="$emit('please-update')">
    </app-new-assignation-modal>

    <div v-if="isLoggedAnAdmin" class="w3-row">
      <button class="w3-button w3-theme w3-round" type="button" name="button" @click="showNewAssignationModal = true">new assignation</button>
    </div>

    <div class="w3-row-padding assignations-container">
      <div class="w3-col l6" v-for="assignation in assignations">
        <app-initiative-assignation class="assignation-card"
          :assignation="assignation" :initiative="initiative"
          :key="assignation.id" @please-update="update()">
        </app-initiative-assignation>
      </div>
    </div>
</div>
</template>

<script>
import InitiativeAssignation from './InitiativeAssignation.vue'
import NewAssignationModal from '../modal/NewAssignationModal.vue'

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
      assignations: []
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.initiative.loggedMember.role === 'ADMIN'
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
  margin-top: 25px;
}

</style>
