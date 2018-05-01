<template lang="html">
  <div class="">
    <div v-if="!loading" class="w3-row-padding">
      <div v-for="initiative in myInitiatives" class="initiative-card-col">
        <app-initiative-card
          :initiative="initiative"
          :key="initiative.id"
          @clicked="updateCurrentInitiative($event)">
        </app-initiative-card>
      </div>
    </div>
    <div v-else class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../assets/loading.gif" alt="">
    </div>
  </div>
</template>

<script>
import InitiativeCard from '@/components/initiative/InitiativeCard.vue'

export default {
  components: {
    'app-initiative-card': InitiativeCard
  },

  computed: {
    loading () {
      return this.$store.state.initiativesTree.loadingMyInitiatives
    },
    myInitiatives () {
      return this.$store.getters.initiativesTreeTop()
    }
  },

  methods: {
    updateCurrentInitiative (id) {
      this.$store.dispatch('updateCurrentInitiativeTree', id)
    }
  }
}
</script>

<style scoped>

.initiative-card-col {
  margin-bottom: 32px;
  display: inline-block;
}

@media screen and (min-width: 1200px) {
  .initiative-card-col {
    width: calc(50% - 26px);
    margin-left: 13px;
    margin-right: 13px;
    vertical-align: top;
  }
}

@media screen and (max-width: 1199px) {
  .initiative-card-col {
    width: calc(100% - 26px);
    margin-left: 13px;
    margin-right: 13px;
    vertical-align: top;
  }
}

</style>
