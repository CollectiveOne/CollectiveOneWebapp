<template lang="html">
  <div class="w3-white">
    <div class="w3-display-topright w3-xlarge" @click="$emit('close-navbar')"><i class="fa fa-times" aria-hidden="true"></i></div>
    <div class="w3-container w3-padding">
      <h4 class="w3-opacity">My Initiatives</h4>
      <app-initiative-menu-item v-for="initiative in initiatives" :initiative="initiative" :key="initiative.id"></app-initiative-menu-item>
      <button type="button" class="new-initiative-btn w3-button w3-teal w3-round" @click="newInitiative()"><i class="fa fa-plus-circle"></i>  create new</button>
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
    ...mapMutations(['showNewInitiativeModal', 'setNewInitiativeParent']),

    newInitiative () {
      this.setNewInitiativeParent(null)
      this.showNewInitiativeModal(true)
    }
  }

}
</script>

<style scoped>

.new-initiative-btn {
  margin-top: 20px;
}

.w3-display-topright {
  width: 80px;
  height: 40px;
  cursor: pointer;
  z-index: 1300;
  text-align: right;
}

.fa-times {
  color: #607d8b;
  margin-right: 10px;
}

</style>
