<template lang="html">
  <div class="row">
    <app-filter class="col"
      :stateOptions="['PROPOSED', 'OPEN', 'ASSIGNED', 'ACCEPTED']"
      :projectNameOptions="allProjects()"
      ></app-filter>
    <app-project-box v-for="project in projects" :key="project.id" :project-data="project"></app-project-box>
  </div>
</template>

<script>
import Filter from '@/components/Filter.vue'
import CbtionBox from '@/components/CbtionBox.vue'
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      projects: []
    }
  },

  methods: {
    ...mapGetters(['allProjects'])
  },

  components: {
    AppFilter: Filter,
    AppCbtionBox: CbtionBox
  }
}
</script>

<style lang="css">
</style>
