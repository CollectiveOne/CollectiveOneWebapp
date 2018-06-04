<template lang="html">
  <div class="w3-row w3-center loader-gif-container">
    <img class="loader-gif" src="../assets/loading.gif" alt="">
  </div>
</template>

<script>
export default {
  mounted () {
    if (this.$store.state.user.authenticated) {
      var path = this.$store.state.user.auth0state
      console.log(path)
      if (!path.startsWith('/app')) {
        this.$router.push({ name: 'InitiativesHome' })
      } else {
        this.$router.push(path)
      }
    } else {
      this.$router.push({ name: 'Landing' })
    }
  }
}
</script>

<style scoped>
</style>
