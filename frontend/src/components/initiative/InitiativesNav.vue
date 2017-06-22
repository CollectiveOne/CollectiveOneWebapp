<template lang="html">
  <div class="">

    <transition name="slideDownUp">
      <app-new-initiative-modal
        v-if="showNewInitiativeModal"
        @close-this="showNewInitiativeModal = false"
        @initiative-created="$emit('initiative-created', $event)">
      </app-new-initiative-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-subinitiative-modal
        v-if="showNewSubInitiativeModal" :parentInitId="parentInitiativeIdForModal"
        @close-this="showNewSubInitiativeModal = false"
        @initiative-created="$emit('initiative-created', $event)">
      </app-new-subinitiative-modal>
    </transition>

    <nav class="w3-white w3-border-right">
      <div class="w3-container w3-padding">
        <h4 class="d2-color noselect"><b>Initiatives</b></h4>
        <div class="create-new w3-button w3-theme-l1" @click="showNewInitiativeModal = true"><i class="fa fa-plus-circle"></i>  create new</div>

        <div class="w3-row section-header">
          <h6 class="section-header l2-color w3-center noselect"><i>top-level</i></h6>
        </div>
        <app-initiative-menu-item v-for="initiative in userInitiatives"
          :initiative="initiative" :key="initiative.id"
          :level="0" class="top-menu-item"
          @initiative-clicked="$emit('selected', $event)"
          @new-subinitiative="newSubInitiative($event)">
        </app-initiative-menu-item>

        <div class="w3-row section-header">
          <h6 class=" l2-color w3-center noselect"><i>my favorites</i></h6>
        </div>

      </div>
    </nav>
  </div>
</template>

<script>
import NewInitiativeModal from '../modal/NewInitiativeModal.vue'
import NewSubInitiativeModal from '../modal/NewSubInitiativeModal.vue'

import InitiativeMenuItem from './InitiativeMenuItem.vue'

export default {

  components: {
    'app-new-initiative-modal': NewInitiativeModal,
    'app-new-subinitiative-modal': NewSubInitiativeModal,
    'app-initiative-menu-item': InitiativeMenuItem
  },

  props: {
    userInitiatives: {
      type: Array
    }
  },

  data () {
    return {
      showNewInitiativeModal: false,
      showNewSubInitiativeModal: false,
      parentInitiativeIdForModal: ''
    }
  },

  methods: {
    newSubInitiative (data) {
      this.parentInitiativeIdForModal = data
      this.showNewSubInitiativeModal = true
    }
  }
}
</script>

<style scoped>

.create-new {
  margin-top: 0px;
  margin-bottom: 0px;
  width: 100%;
  text-align: left;
}

.section-header {
  margin-top: 10px;
}

.w3-display-topright {
  width: 80px;
  height: 40px;
  cursor: pointer;
  z-index: 1300;
  text-align: right;
}

.fa-times {
  margin-right: 10px;
}

.top-menu-item {
  margin-bottom: 15px;
}

</style>
