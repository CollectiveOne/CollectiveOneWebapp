<template lang="html">
  <div class="col">
    <div class="row">
      <app-filter
        :orderByOptions="orderByOptions"
        :projectNameOptions="allProjects()"
        :stateOptions="stateOptions"
        :statesSelected="statesSelected"
        :orderBySelected="orderBySelected"
        :resSet="resSet"
        @updateData="updateProjects">
      </app-filter>
    </div>

    <transition name="slide">
      <div class="row data">
        <div class="col-12 col-md-6 col-lg-4 el-placeholder" v-for="project in projects" >
          <app-project-box :key="project.id" :project="project"></app-project-box>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Filter from '@/components/Filter.vue'
import ProjectBox from '@/components/ProjectBox.vue'
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      projects: [],
      stateOptions: ['PROPOSED', 'OPEN', 'ASSIGNED', 'ACCEPTED', 'NOTOPENED', 'DELETED'],
      statesSelected: ['PROPOSED', 'OPEN', 'ASSIGNED'],
      orderByOptions: [
        { text: 'New first', value: 'CREATIONDATEDESC' },
        { text: 'Old first', value: 'CREATIONDATEASC' }
      ],
      orderBySelected: 'CREATIONDATEASC',
      resSet: []
    }
  },

  methods: {
    ...mapGetters(['allProjects']),

    updateProjects (filters) {
      this.axios.put('/1/projects', filters).then((response) => {
        this.projects = response.data.projectDtos
        this.resSet = response.data.resSet
      })
    }
  },

  components: {
    AppFilter: Filter,
    AppProjectBox: ProjectBox
  }

}
</script>

<style scoped>

.data {
  margin-top: 20px;
}

.el-placeholder {
  margin-bottom: 20px;
}

</style>
