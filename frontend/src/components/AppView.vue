<template lang="html">
  <div>
    <app-header @expand-nav="expandNav = !expandNav"></app-header>
    <div class="w3-row app-content">
      <router-view @hide-nav="expandNav = false" :expandNav="expandNav"></router-view>
    </div>
  </div>
</template>

<script>
import Header from './Header.vue'

export default {
  components: {
    AppHeader: Header
  },

  data () {
    return {
      expandNav: false
    }
  },

  computed: {
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
    }
  },

  watch: {
    expandNav () {
      this.$store.commit('setExpandNav', this.expandNav)
    }
  },

  methods: {
    checkWindowSize () {
      var windowIsSmallNow = window.innerWidth < 600

      if (this.windowIsSmall !== windowIsSmallNow) {
        this.$store.commit('setWindowIsSmall', windowIsSmallNow)
      }
    }
  },

  created () {
    this.checkWindowSize()

    if (this.windowIsSmall) {
      this.expandNav = false
      this.$store.commit('setWindowIsSmall', true)
    } else {
      this.expandNav = true
      this.$store.commit('setWindowIsSmall', false)
    }

    window.addEventListener('resize', this.checkWindowSize)
  }
}
</script>

<style scoped>

.app-content {
  margin-top:65px;
}

</style>
