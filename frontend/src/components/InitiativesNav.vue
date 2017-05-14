<template lang="html">
  <div class="w3-card-2 w3-round w3-white">
    <div class="w3-container w3-padding">
      <h4 class="w3-opacity">My Initiatives</h4>
      <app-initiative-menu-item v-for="initiative in initiatives" :initiative="initiative" :key="initiative.id"></app-initiative-menu-item>
      <button type="button" class="w3-button w3-teal w3-round" @click="newInitiative()"><i class="fa fa-plus-circle"></i>  create new</button>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'

import InitiativeMenuItem from '@/components/InitiativeMenuItem.vue'

export default {

  components: {
    AppInitiativeMenuItem: InitiativeMenuItem
  },

  data () {
    return {
    }
  },

  computed: {
    initiatives () {
      if (this.$store.state.userElements.initiatives) {
        return this.$store.state.userElements.initiatives
      } else {
        return []
      }
    }
  },

  methods: {
    ...mapMutations(['showNewInitiativeModal']),

    newInitiative () {
      this.showNewInitiativeModal(true)
    }
  }

}
</script>

<style scoped>

</style>
