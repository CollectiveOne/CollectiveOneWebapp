<template lang="html">
  <select class="form-control" :value="value" @input="selected">
    <option v-for="project in activeProjects()">{{ project }}</option>
  </select>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'

export default {

  props: {
    value: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
    }
  },

  methods: {
    ...mapGetters(['activeProjects']),
    ...mapMutations(['setActiveProject']),

    selected (e) {
      var projectName = e.target.value
      this.$emit('input', projectName)
      this.setActiveProject(projectName)
    }
  }

}
</script>

<style lang="css">
</style>
