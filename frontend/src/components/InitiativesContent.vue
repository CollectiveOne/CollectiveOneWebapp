<template lang="html">
  <div v-if="initiative" class="w3-row">
    <div class="w3-white">
      <header class="w3-container w3-theme">
        <h3>{{ initiativePath }}</h3>
      </header>

      <div class=" w3-row">
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding w3-border-blue">Overview</div>
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Actions</div>
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Activity</div>
      </div>
      <div class="w3-row">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  computed: {
    initiative () {
      return this.$store.state.activeInitiative.initiative
    },

    initiativePath () {
      var subInitiativesTitle = ''
      for (var subInitiative in this.initiative.subInitiatives) {
        subInitiativesTitle = subInitiativesTitle + ' - ' + subInitiative.name
      }
      return this.initiative.name + subInitiativesTitle
    }
  },

  methods: {
    ...mapActions(['setActiveInitiative'])
  },

  mounted () {
    this.setActiveInitiative(this.$route.params.id)
  }
}
</script>

<style scoped>

.tablink {
  cursor: pointer;
}
</style>
