<template lang="html">
  <nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;">
    <div class="w3-display-topright d2-color w3-xlarge" @click="$emit('close-navbar')"><i class="fa fa-times" aria-hidden="true"></i></div>
    <div class="w3-container w3-padding">
      <h4 class="d2-color"><b>My Initiatives</b></h4>
      <h5 class="create-new l2-color w3-button" @click="newInitiative()"><i class="fa fa-plus-circle"></i>  create new</h5>
      <div class="w3-card-2">
        <app-initiative-menu-item v-for="initiative in userInitiatives"
          :initiative="initiative" :key="initiative.id"
          @new-subinitiative="$emit('new-initiative', $event)">
        </app-initiative-menu-item>
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
  margin: 0px;
  width: 100%;
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

</style>
