<template lang="html">
  <router-link :to="link">{{ name }}</router-link>
</template>

<script>
export default {
  props: {
    initiative: {
      type: Object
    },
    initiativeId: {
      type: String
    },
    initiativeName: {
      type: String
    }
  },
  computed: {
    name () {
      if (this.initiative) {
        return this.initiative.meta.name
      } else {
        return this.initiativeName
      }
    },
    link () {
      var id = ''
      if (this.initiative) {
        id = this.initiative.id
      } else {
        id = this.initiativeId
      }
      return '/inits/' + id + '/overview'
    }
  }
}
</script>

<style lang="css">
</style>
