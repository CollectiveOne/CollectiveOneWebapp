<template lang="html">
  <b-navbar toggleable type="inverse" variant="success">
    <b-nav-toggle target="nav_collapse"></b-nav-toggle>

    <b-link class="navbar-brand" to="/">
      <img class="logo" src="../assets/images/Logo-Light.png"></img>
    </b-link>

    <b-collapse is-nav id="nav_collapse">
      <b-nav is-nav-bar>
        <b-nav-item to="/projectList">projects</b-nav-item>
        <b-nav-item to="/cbtionList">contributions</b-nav-item>
        <b-nav-item to="/decisionList">decisions</b-nav-item>
        <b-nav-item to="/recentActivityList">recent activity</b-nav-item>
        <b-nav-item to="/projectList">help</b-nav-item>
      </b-nav>

      <b-nav is-nav-bar class="ml-auto">

        <b-nav v-if="!isUserLogged" is-nav-bar >
            <b-nav-item style="cursor: pointer" v-b-modal="'loginModal'">login</b-nav-item>
        </b-nav>
        <b-nav v-else is-nav-bar>
          <b-nav-item-dropdown text="new" right-alignment>
            <b-dropdown-item to="/cbtionNew">Contribution</b-dropdown-item>
            <b-dropdown-item to="/goalNew">Goal</b-dropdown-item>
            <b-dropdown-item to="/decisionNew">Decision</b-dropdown-item>
            <b-dropdown-item to="/projectNew">Project</b-dropdown-item>
          </b-nav-item-dropdown>
          <b-nav-item-dropdown  right-alignment>
            <template slot="text">
              <span style="font-weight: bold;">{{ usernameLogged }}</span>
            </template>

            <b-dropdown-item to="#">Profile</b-dropdown-item>
            <b-dropdown-item @click="logoutUser()">Signout</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-nav>
      </b-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  computed: {
    ...mapGetters([
      'isUserLogged',
      'usernameLogged'
    ])
  },

  methods: {
    ...mapActions([
      'logoutUser'
    ])
  }
}
</script>

<style scoped>

.logo {
  width: 200px;
}

.nav {
  font-size: 14px;
}

</style>
