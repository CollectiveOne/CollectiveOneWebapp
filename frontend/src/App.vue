<template>
  <div id="app" class="container-fluid">
    <component :is="modal"></component>
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
    },

    modal () {
      if (this.$store.state.modals.showNewInitiative) {
        return 'app-new-initiative-modal'
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

</style>
