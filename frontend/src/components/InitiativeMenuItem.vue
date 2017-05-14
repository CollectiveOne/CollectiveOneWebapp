<template lang="html">
  <router-link :to="'/inits/'+initiative.id+'/overview'">
    <h5>{{ initiative.name }}</h5>
  </router-link>
</template>

<script>
export default {
  props: {
    initiative: {
      type: Object,
      default: () => {
        return {
          name: 'Initiative Name',
          driver: 'Initiative Driver',
          subInitiatives: []
        }
      }
    }
  }
}
</script>

<style scoped>


</style>
