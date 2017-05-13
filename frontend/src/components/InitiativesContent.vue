<template lang="html">
  <div class="w3-row">
    <div class="w3-container w3-card w3-white w3-round">
      <h4>{{ initiativePath }}</h4>
      <div class=" w3-row">
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding w3-border-blue">Overview</div>
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Actions</div>
        <div class="w3-third tablink w3-bottombar w3-hover-light-grey w3-padding">Activity</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      name: 'Ouishare FEST BCN 2017',
      subInitiativesNames: ['Primera Fase', 'Producción']
    }
  },

  computed: {
    initiativePath () {
      return this.name + ' - ' + this.subInitiativesNames.join(' - ')
    }
  }
}
</script>

<style scoped>

.tablink {
  cursor: pointer;
}
</style>
