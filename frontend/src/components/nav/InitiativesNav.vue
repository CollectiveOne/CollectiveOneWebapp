<template lang="html">
  <nav class="nav-container w3-white w3-border-right">
    <div class="w3-container w3-padding">
      <div class="create-new w3-button w3-theme-l1" @click="$store.commit('showNewInitiativeModal', true)"><i class="fa fa-plus-circle"></i>  create new</div>

      <div class="w3-row">
        <h6 class="l2-color w3-center noselect"><i>my initiatives</i></h6>
      </div>
      <app-initiative-menu-item v-for="(initiative, ix) in userInitiatives"
        :initiative="initiative" :key="initiative.id"
        :coord="[ ix ]" class="top-menu-item">
      </app-initiative-menu-item>

      <div class="w3-row">
        <h6 class=" l2-color w3-center noselect"><i>my favorites</i></h6>
      </div>

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

</style>
