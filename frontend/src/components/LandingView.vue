<template lang="html">
  <div class="">
    <b-navbar toggleable type="inverse" variant="inverse">
      <b-nav-toggle target="nav_collapse"></b-nav-toggle>
      <b-link class="navbar-brand" to="#">
        <img src="../assets/Logo-Light.png" class="logo">
      </b-link>
      <b-collapse is-nav id="nav_collapse">
        <b-nav is-nav-bar>
          <b-nav-item>learn more</b-nav-item>
          <b-nav-item>contact</b-nav-item>
        </b-nav>
      </b-collapse>
    </b-navbar>

    <div class="welcome-header">
      <h1>Welcome to CollectiveOne</h1>
      <button class="btn btn-primary" @click="login()">login / signup</button>
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
  background-color: rgb(61, 61, 61) !important;
}

.logo {
  width: 200px;
  margin-bottom: 5px;
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
