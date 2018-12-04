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

        let lastVisitedInitiatveId = localStorage.getItem('lastVisitedInitiatveId')
        let lastVisitedSectionId = localStorage.getItem('lastVisitedSectionId')

        console.log(window.location.href)

        if ((lastVisitedInitiatveId != null) && (lastVisitedInitiatveId != null)) {
          console.log('redirecting to last visited section')
          this.$router.replace({
            name: 'ModelSectionContent',
            params: {
              initiativeId: lastVisitedInitiatveId,
              sectionId: lastVisitedSectionId
            }
          })
        } else {
          this.$router.replace({ name: 'InitiativesHome' })
        }
      } else {
        this.$router.replace(path)
      }
    } else {
      this.$router.replace({ name: 'Landing' })
    }
  }
}
</script>

<style scoped>
</style>
