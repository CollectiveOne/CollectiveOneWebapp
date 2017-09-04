<template lang="html">
  <router-link :to="{name: 'Initiative', params: { 'initiativeId': initiativeIdinternal }}">{{ name }}</router-link>
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
    initiativeIdinternal () {
      if (this.initiative) {
        return this.initiative.id
      } else {
        return this.initiativeId
      }
    }
  }
}
</script>

<style lang="css">
</style>
