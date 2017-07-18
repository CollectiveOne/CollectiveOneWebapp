<template lang="html">
  <nav class="nav-container dark-gray">
    <div class="">

      <div class="w3-container">
        <div
          class="create-new w3-button light-grey w3-round-large w3-center"
          @click="$store.commit('showNewInitiativeModal', true)">
          <i class="fa fa-plus-circle"></i>  create new
        </div>
      </div>

      <div class="w3-row my-initiatives-row">
        <h6 class="w3-center white-text noselect"><i>my initiatives</i></h6>
      </div>
      <app-initiative-menu-item v-for="(initiative, ix) in userInitiatives"
        :initiative="initiative" :key="initiative.id"
        :coord="[ ix ]" class="top-menu-item"
        @initiative-selected="$emit('initiative-selected')">
      </app-initiative-menu-item>

    </div>
  </nav>
</template>

<script>
import InitiativeMenuItem from './InitiativeMenuItem.vue'

export default {

  components: {
    'app-initiative-menu-item': InitiativeMenuItem
  },

  data () {
    return {
    }
  },

  computed: {
    userInitiatives () {
      return this.$store.state.support.initiativesTree
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

.nav-container {
  min-height: calc(100vh - 51px);
}

.create-new {
  margin-top: 20px;
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

.my-initiatives-row {
  margin-top: 30px;
}

</style>
