<template lang="html">
  <div class="row this-container">
    <div class="col">
      <h3 class="section-title">My Initiatives</h3>
      <p v-for="initiative in initiatives"></p>
      <button class="btn btn-primary">create new initiative</button>
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

.section-title {
  font-weight: bold;
}

.this-container {
  padding-top: 15px;
  padding-left: 5px;
  /*text-align: center;*/
}

.this-container .btn {
  margin-top: 10px;
  float: right;
}

</style>
