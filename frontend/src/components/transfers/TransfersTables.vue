<template lang="html">
  <div class="">
    <div class="tabs-row w3-row w3-center">
      <div class="tabs-container">
        <div
          class="subtab-element w3-bottombar w3-hover-light-grey w3-padding cursor-pointer"
          :class="{'w3-border-blue': isTransfersToUsers}"
          @click="goToAssignationsTable()">
          to users
        </div>
        <div
          class="subtab-element w3-bottombar w3-hover-light-grey w3-padding cursor-pointer"
          :class="{'w3-border-blue': isTransfersToInitiatives}"
          @click="goToTransfersTable()">
          to initiatives
        </div>
      </div>
    </div>
    <div class="w3-row">
      <div class="slider-container">
        <!-- <transition :name="transfersTableTransition" mode="out-in"> -->
          <component
            :is="transfersTableComponent"
            :transfers="transfers"
            :assignations="assignations"
            :showFrom="showFrom"
            :key="transfersTableComponent.name">
          </component>
        <!-- </transition> -->
      </div>
    </div>
  </div>
</template>

<script>
import AssignationsTable from '@/components/transfers/AssignationsTable.vue'
import TransfersTable from '@/components/transfers/TransfersTable.vue'

export default {

  components: {
    'app-transfers-table': TransfersTable,
    'app-assignations-table': AssignationsTable
  },

  props: {
    assignations: Array,
    transfers: Array,
    showFrom: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      transfersTableComponent: AssignationsTable
    }
  },

  computed: {
    isTransfersToUsers () {
      return this.transfersTableComponent.name === 'assignationsTable'
    },
    isTransfersToInitiatives () {
      return this.transfersTableComponent.name === 'transfersTable'
    },
    transfersTableTransition () {
      if (this.isTransfersToUsers) {
        return 'slideToRight'
      }

      if (this.isTransfersToInitiatives) {
        return 'slideToLeft'
      }
    }
  },

  methods: {
    goToAssignationsTable () {
      this.transfersTableComponent = AssignationsTable
    },
    goToTransfersTable () {
      this.transfersTableComponent = TransfersTable
    }
  }
}
</script>

<style scoped>

.tabs-row {
  margin-bottom: 10px;
}

.subtab-element {
  display: inline-block;
  width: 120px;
  text-align: center;
}

</style>
