<template lang="html">
  <select class="form-control" v-model="projectName" @change="$emit('input',projectName)">
    <option v-for="project in allProjects()">{{ project }}</option>
  </select>
</template>

<script>
import { mapGetters } from 'vuex'

export default {

  props: {
    value: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      projectName: '',
      projectNameOptions: []
    }
  },

  methods: {
    ...mapGetters(['allProjects', 'activeProject'])
  }
}
</script>

<style lang="css">
</style>
