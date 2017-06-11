<template lang="html">
  <div class="w3-container">
    <app-initiative-assignation v-for="assignation in assignations" :assignationInit="assignation">
    </app-initiative-assignation>
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

<style lang="css">
</style>
