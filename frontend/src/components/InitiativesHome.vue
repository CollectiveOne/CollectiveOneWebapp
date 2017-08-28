<template lang="html">
  <div class="">
    <router-view></router-view>
    <div v-if="userInitiatives.length > 0" class="w3-row-padding">
      <div v-for="initiative in userInitiatives"
        :key="initiative.id"
        class="w3-col l4 m6">
        <app-initiative-card :initiative="initiative">
        </app-initiative-card>
      </div>
    </div>
    <div v-if="showEmptyMessage" class="w3-row">
      <div class="w3-panel w3-padding light-grey">
        <h4>
          <p>
            This is empty for the moment. In order to be added to an existing initiative,
            please contact its administrator.
          </p>
          <p>
            If this is not the case, and you want to create your own initiative, you can
            simply do this from the panel in the left.
          </p>
          <p>
            We are working on an initiative's explorer feature (among several other things),
            so expect to be surprised very soon!
          </p>
        </h4>
      </div>
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
    },
    showEmptyMessage () {
      return this.userInitiatives.length === 0
    },
    showVerifyEmail () {
      return this.$store.state.support.userEmailNotVerified
    }
  }
}
</script>

<style lang="css">
</style>
