<template lang="html">
  <router-link :to="link">transfer</router-link>
</template>

<script>
export default {
  props: {
    assignation: {
      type: Object
    }
  },
  computed: {
    link () {
      return '/inits/' + this.assignation.initiativeId + '/assignations/' + this.assignation.id
    }
  }
}
</script>

<style lang="css">
</style>
