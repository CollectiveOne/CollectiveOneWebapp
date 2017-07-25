<template lang="html">
  <div class="logged-view">
    <app-header @expand-nav="expandNav = !expandNav"></app-header>
    <div class="logged-content">
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

  computed: {
    windowIsSmall () {
      return window.innerWidth < 601
    }
  },

  data () {
    return {
      expandNav: false
    }
  },

  methods: {
  },

  mounted () {
    if (this.windowIsSmall) {
      this.expandNav = false
    } else {
      this.expandNav = true
    }
  }
}
</script>

<style scoped>

.logged-content {
  margin-top:65px;
}

</style>
