<template>
  <div id="app" class="container-fluid">
    <component :is="view"></component>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

import LandingView from '@/components/LandingView.vue'
import LoggedView from '@/components/LoggedView.vue'
import NewInitiativeModal from '@/components/modal/NewInitiativeModal.vue'

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
    AppLoggedView: LoggedView,
    AppNewInitiativeModal: NewInitiativeModal
  },

  mounted () {
    this.initUserAuthenticated()
  }
}
</script>

<style>

#app html,body,h1,h2,h3,h4,h5 {
  font-family: "Open Sans", sans-serif;
}

.w3-modal {
  padding-top: 30px;
}

.l2-color {
  color: #8995d6 !important; /* w3-indigo l2*/
}

.d2-color {
  color: #334191 !important;  /* w3-indigo d2*/
}

</style>
