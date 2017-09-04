<template lang="html">
  <div class="">
    <router-view></router-view>
    <div v-if="$store.state.support.myInitiativesFirstLoaded" class="">
      <div v-if="userInitiatives.length > 0" class="w3-row-padding">
        <div v-for="initiative in userInitiatives"
          :key="initiative.id"
          class="w3-col l4 m6">
          <app-initiative-card :initiative="initiative">
          </app-initiative-card>
        </div>
      </div>
      <div v-else class="w3-row">
        <div class="w3-panel w3-padding light-grey">
          <h4>
            <p>
              You are not a member of any initiative for the moment.
              You can start by creating an initiative on the left, or
              ask the owner of an existing one to add you!
            </p>
          </h4>
        </div>
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
    userInitiatives () {
      return this.$store.state.support.initiativesTree
    }
  }
}
</script>

<style lang="css">
</style>
