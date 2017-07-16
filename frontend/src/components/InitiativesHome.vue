<template lang="html">
  <div class="w3-row-padding">
    <div v-for="initiative in userInitiatives"
      :key="initiative.id"
      class="w3-col l4 m6">
      <app-initiative-card :initiative="initiative">
      </app-initiative-card>
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
    userInitiatives () {
      return this.$store.state.support.initiativesTree
    }
  }
}
</script>

<style lang="css">
</style>
