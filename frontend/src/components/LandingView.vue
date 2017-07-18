<template lang="html">
  <div class="">
    <div class="welcome-header">
      <h1>Welcome to CollectiveOne</h1>
      <button class="w3-button app-button" @click="login()">login / signup</button>
    </div>

  </div>
</template>

<script>
export default {

  methods: {
    login () {
      this.$store.state.user.lock.show()
    }
  }
}
</script>

<style scoped>

.navbar {
}

.logo {
}

.welcome-header {
  background-color: rgb(247, 247, 247);
  padding-top: 195px;
  height: 500px;
  text-align: center;
}

.welcome-header button {
  width: 150px;
}

</style>
