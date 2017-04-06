<template lang="html">
  <div>
    <b-alert state="info" dismissible :show="true">
		  <strong>Tip</strong><br />
		  Here you can browse all active projects.
		</b-alert>
		<app-filter></app-filter>
    <app-project-box v-for="project in projects" :key="project.id" :project-data="project"></app-project-box>
  </div>
</template>

<script>
import Filter from '@/components/Filter.vue'
import ProjectBox from '@/components/ProjectBox.vue'

export default {
  data () {
    return {
      projects: []
    }
  },

  components: {
    AppFilter: Filter,
    AppProjectBox: ProjectBox
  }
}
</script>

<style lang="css">
</style>
