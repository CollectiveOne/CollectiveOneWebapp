<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <app-initiative-assignation :assignation="assignation"></app-initiative-assignation>

      <div class="w3-display-topright">
        <router-link tag="div" :to="{ name: 'InitiativeAssignations' }"
          class="w3-button d2-color">
          <i class="fa fa-times" aria-hidden="true"></i>
        </router-link>
      </div>

    </div>
  </div>
</template>

<script>
import InitiativeAssignation from './InitiativeAssignation.vue'

export default {

  components: {
    'app-initiative-assignation': InitiativeAssignation
  },

  data () {
    return {
      assignation: null
    }
  },

  computed: {
    assignationId () {
      return this.$route.params.assignationId
    }
  },

  methods: {
    updateAssignationData () {
      this.axios.get('/1/secured/assignation/' + this.assignationId).then((response) => {
        this.assignation = response.data.data
      })
    }
  },

  mounted () {
    this.updateAssignationData()
  }
}
</script>

<style scoped>

.w3-modal {
  display: block;
}

.close-div {
}

.fa-times {
}

form {
  padding-top: 0px;
  padding-bottom: 35px;
}

.bottom-btns-row button {
  width: 100%;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.amount-container {
  height: 150px;
}

.fa-certificate {
  font-size: 100px;
}


</style>
