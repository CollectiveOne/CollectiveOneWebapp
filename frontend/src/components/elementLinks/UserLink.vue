<template lang="html">
  <span>{{ user.nickname }}</span>
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
