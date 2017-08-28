<template lang="html">
  <div class="">
    <h1>redirect!</h1>
  </div>
</template>

<script>
export default {
  mounted () {
    console.log(this.$store.state.user.auth0state)
    if (this.$store.state.user.authenticated) {
      this.$router.push({ name: 'InitiativesHome' })
    } else {
      this.$router.push({ name: 'Landing' })
    }
  }
}
</script>

<style scoped>
</style>
