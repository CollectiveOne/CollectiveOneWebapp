<template lang="html">
  <router-link :to="{
    name: 'InitiativeAssignation',
    params: {
      'initiativeId': this.assignation.initiativeId,
      'assignationId': this.assignation.id
      }}">
    transfer
  </router-link>
</template>

<script>
export default {
  props: {
    assignation: {
      type: Object
    }
  }
}
</script>

<style lang="css">
</style>
