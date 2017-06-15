<template lang="html">
  <div class="w3-row-padding this-container">
    <div class="w3-col l6" v-for="assignation in assignations">
      <app-initiative-assignation :assignation="assignation" :initiative="initiative"
        :key="assignation.id" @please-update="update()">
      </app-initiative-assignation>
    </div>
  </div>
</template>

<script>
import InitiativeAssignation from './InitiativeAssignation.vue'

export default {
  components: {
    'app-initiative-assignation': InitiativeAssignation
  },

  props: {
    initiative: null
  },

  data () {
    return {
      assignations: []
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

.this-container {
  padding-top: 25px;
}

</style>
