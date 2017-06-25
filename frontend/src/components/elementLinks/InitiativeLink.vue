<template lang="html">
  <router-link :to="link">{{ initiative.name }}</router-link>
</template>

<script>
export default {
  props: {
    initiative: {
      type: Object
    }
  },
  computed: {
    link () {
      return '/inits/' + this.initiative.id + '/overview'
    }
  }
}
</script>

<style lang="css">
</style>
