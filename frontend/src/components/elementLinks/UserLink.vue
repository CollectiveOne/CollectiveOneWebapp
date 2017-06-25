<template lang="html">
  <router-link :to="link">{{ user.nickname }}</router-link>
</template>

<script>
export default {
  props: {
    user: {
      type: Object
    }
  },
  computed: {
    link () {
      return '/user/' + this.user.c1Id
    }
  }
}
</script>

<style lang="css">
</style>
