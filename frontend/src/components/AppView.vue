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

  created () {
    if (this.windowIsSmall) {
      this.expandNav = false
    } else {
      this.expandNav = true
    }
  }
}
</script>

<style scoped>

.app-content {
  margin-top:65px;
}

</style>
