<template lang="html">
  <nav class="w3-sidebar w3-white" style="width:300px;">
    <div class="w3-display-topright d2-color w3-xlarge" @click="$emit('close-navbar')"><i class="fa fa-times" aria-hidden="true"></i></div>
    <div class="w3-container w3-padding">
      <h4 class="d2-color"><b>Initiatives</b></h4>
      <div class="create-new w3-button w3-theme-l1" @click="newInitiative()"><i class="fa fa-plus-circle"></i>  create new</div>

      <div class="w3-row section-header">
        <h6 class="section-header l2-color w3-center"><i>top-level</i></h6>
      </div>
      <app-initiative-menu-item v-for="initiative in userInitiatives"
        :initiative="initiative" :key="initiative.id"
        :level="0" class="top-menu-item"
        @new-subinitiative="$emit('new-initiative', $event)">
      </app-initiative-menu-item>

      <div class="w3-row section-header">
        <h6 class=" l2-color w3-center"><i>my favorites</i></h6>
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

  props: {
    userInitiatives: {
      type: Array
    }
  },

  methods: {
    newInitiative () {
      this.$emit('new-initiative', '')
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
  margin-bottom: 10px;
}

</style>
