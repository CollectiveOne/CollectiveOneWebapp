<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="$router.push({ name: 'InitiativeAssignations'})">
          <i class="fa fa-times d2-color" aria-hidden="true"></i>
        </div>

        <app-initiative-assignation :assignation="assignation"></app-initiative-assignation>

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
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-times {
  margin-right: 20px;
  margin-top: 20px;
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
