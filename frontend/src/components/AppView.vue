<template lang="html">
  <div>
    <app-header @expand-nav="expandNav = !expandNav"></app-header>
    <div v-if="userAuthenticated" class="app-content">
      <router-view @hide-nav="expandNav = false" :expandNav="expandNav"></router-view>
    </div>
    <div v-else class="w3-center logged-out-content">
      <h2>please login to continue</h2>
      <button class="w3-button app-button" @click="login()">login</button>
    </div>
  </div>
</template>

<script>
import Header from './Header.vue'

export default {
  components: {
    AppHeader: Header
  },

  computed: {
    windowIsSmall () {
      return window.innerWidth < 601
    },
    userAuthenticated () {
      return this.$store.state.user.authenticated
    }
  },

  data () {
    return {
      expandNav: false
    }
  },

  methods: {
    login () {
      if (this.$store.state.user.lock) {
        this.$store.state.user.lock.show()
      }
    }
  },

  mounted () {
    if (this.windowIsSmall) {
      this.expandNav = false
    } else {
      this.expandNav = true
    }

    if (!this.userAuthenticated) {
      if (this.$store.state.user.lock) {
        this.$store.state.user.lock.show()
      }
    }
  }
}
</script>

<style scoped>

.app-content {
  margin-top:65px;
}

</style>
