<template lang="html">
  <div class="w3-card-2 w3-round w3-white">
    <div class="w3-container w3-padding">
      <h4 class="w3-opacity">My Initiatives</h4>
      <button type="button" class="w3-button w3-theme w3-round"><i class="fa fa-plus-circle"></i>  create new</button>
    </div>
  </div>
</template>

<script>
export default {

  data () {
    return {
      initiatives: []
    }
  },

  mounted () {
    this.axios.get('/1/secured/initiatives/mines').then((response) => {
      this.initiatives = response.data.data
    }).catch()
  }
}
</script>

<style scoped>

</style>
