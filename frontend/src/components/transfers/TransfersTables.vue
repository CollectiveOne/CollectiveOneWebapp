<template lang="html">
  <div class="">
    <div class="tabs-row w3-row w3-center">
      <div class="tabs-container">
        <div
          class="subtab-element w3-button"
          :class="{'app-button': isTransfersToUsers, 'app-button-light': !isTransfersToUsers}"
          @click="goToAssignationsTable()">
          to users
        </div>
        <div
          class="subtab-element w3-button"
          :class="{'app-button': isTransfersToInitiatives, 'app-button-light': !isTransfersToInitiatives}"
          @click="goToTransfersTable()">
          to initiatives
        </div>
      </div>
    </div>
    <div class="w3-row">
      <div class="slider-container">
        <transition :name="transfersTableTransition" mode="out-in">
          <component
            :is="transfersTableComponent"
            :url="url"
            :showFrom="ofSubinitiatives"
            :triggerUpdate="triggerUpdate"
            :key="transfersTableComponent.name">
          </component>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import AssignationsTable from '@/components/transfers/AssignationsTable.vue'
import TransfersTable from '@/components/transfers/TransfersTable.vue'

export default {

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    ofSubinitiatives: {
      type: Boolean,
      default: false
    },
    triggerUpdate: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      transfersTableComponent: AssignationsTable,
      showFrom: true
    }
  },

  computed: {
    url () {
      if (!this.ofSubinitiatives) {
        if (this.isTransfersToUsers) {
          return '/1/initiative/' + this.initiativeId + '/assignations'
        } else {
          return '/1/initiative/' + this.initiativeId + '/transfersToInitiatives'
        }
      } else {
        if (this.isTransfersToUsers) {
          return '/1/initiative/' + this.initiativeId + '/assignationsOfSubinitiatives'
        } else {
          return '/1/initiative/' + this.initiativeId + '/transfersFromSubinitiatives'
        }
      }
    },
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
  width: 160px;
  text-align: center;
}

</style>
