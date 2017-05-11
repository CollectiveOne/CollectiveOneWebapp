<template lang="html">
  <div class="this-container">
    <b-navbar toggleable type="inverse" variant="inverse">
      <b-nav-toggle target="nav_collapse"></b-nav-toggle>
      <b-link class="navbar-brand" to="/home">
        <img src="../assets/Logo-Light.png" class="logo">
      </b-link>
      <b-collapse is-nav id="nav_collapse">
        <b-nav is-nav-bar class="ml-auto">
          <b-nav-item right-alignment>{{ loggedUserNickname() }}</b-nav-item>
          <b-nav-item @click="logoutUser()" right-alignment>logout</b-nav-item>
        </b-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  components: {
  },

  methods: {
    ...mapGetters(['loggedUserNickname']),
    ...mapActions(['logoutUser'])
  }
}
</script>

<style scoped>

.this-container {
  font-weight: bold;
}

.navbar {
  background-color: #026AA7 !important;
  padding-top: 0px;
  padding-bottom: 0px;
}

.logo {
  width: 200px;
}

</style>
