<template>
  <div id="app" class="container-fluid">
    <component :is="view"></component>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

import LandingView from '@/components/LandingView.vue'
import LoggedView from '@/components/LoggedView.vue'

export default {
  name: 'app',

  methods: {
    ...mapActions(['initUserAuthenticated'])
  },

  computed: {
    view () {
      if (this.$store.state.user.authenticated) {
        return 'app-logged-view'
      } else {
        return 'app-landing-view'
      }
    }
  },

  components: {
    AppLandingView: LandingView,
    AppLoggedView: LoggedView
  },

  mounted () {
    this.initUserAuthenticated()
  }
}
</script>

<style>
#app {
  font-family: 'Roboto', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #026AA7;
}

#app h1, h2, h3, h4 {
  font-family: 'Open Sans', sans-serif;
}

#app p {
  font-family: 'Roboto', sans-serif;
}

#app button {
  cursor: pointer;
}

#app a {
  cursor: pointer;
}
</style>
