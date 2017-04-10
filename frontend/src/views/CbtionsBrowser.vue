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
        @updateData="updateCbtions"
        :loading="loading">
      </app-filter>
    </div>

    <transition name="slide">
      <div class="row data">
        <div class="col-12 col-md-6 col-lg-4 el-placeholder" v-for="cbtion in cbtions" >
          <app-cbtion-box :key="cbtion.id" :cbtion="cbtion"></app-cbtion-box>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Filter from '@/components/Filter.vue'
import CbtionBox from '@/components/CbtionBox.vue'
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      cbtions: [],
      stateOptions: ['PROPOSED', 'OPEN', 'ASSIGNED', 'ACCEPTED', 'NOTOPENED', 'DELETED'],
      statesSelected: ['PROPOSED', 'OPEN', 'ASSIGNED'],
      orderByOptions: [
        { text: 'New first', value: 'CREATIONDATEDESC' },
        { text: 'Old first', value: 'CREATIONDATEASC' },
        { text: 'More relevant', value: 'RELEVANCEDESC' },
        { text: 'Less relevant', value: 'RELEVANCEASC' }
      ],
      orderBySelected: 'CREATIONDATEDESC',
      resSet: [],
      loading: false
    }
  },

  methods: {
    ...mapGetters(['allProjects']),

    updateCbtions (filters) {
      this.loading = true
      this.axios.put('/1/cbtions', filters).then((response) => {
        this.loading = false
        this.cbtions = response.data.cbtionDtos
        this.resSet = response.data.resSet
      })
    }
  },

  components: {
    AppFilter: Filter,
    AppCbtionBox: CbtionBox
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
