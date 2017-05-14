<template lang="html">
  <div class="w3-row">
    <div class="w3-container w3-card w3-white w3-round">
      <h4>{{ initiativePath }}</h4>
      <div class=" w3-row">
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding w3-border-blue">Overview</div>
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Actions</div>
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Activity</div>
      </div>
      <div class=" w3-row">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      initiative: {
        name: 'InitiativeName',
        subInitiatives: []
      }
    }
  },

  computed: {
    initiativePath () {
      var subInitiativesTitle = ''
      for (var subInitiative in this.initiative.subInitiatives) {
        subInitiativesTitle = subInitiativesTitle + ' - ' + subInitiative.name
      }
      return this.initiative.name + subInitiativesTitle
    }
  },

  mounted () {
    this.axios.get('/1/secured/initiative/' + this.$route.params.id).then((response) => {
      this.initiative = response.data.data
    })
  }
}
</script>

<style scoped>

.tablink {
  cursor: pointer;
}
</style>
