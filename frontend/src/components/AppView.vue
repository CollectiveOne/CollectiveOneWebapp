<template lang="html">
  <div class="app-view">
    <div class="w3-row app-content">
      <router-view></router-view>
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
      return this.$store.state.support.windowIsSmall
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
      this.$store.commit('setWindowIsSmall', true)
    } else {
      this.$store.commit('setWindowIsSmall', false)
    }

    window.addEventListener('resize', this.checkWindowSize)
  }
}
</script>

<style scoped>

.app-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.app-content {
  height: 100%;
  display: flex;
  flex-direction: column;
}

</style>
