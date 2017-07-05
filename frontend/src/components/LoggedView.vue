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

  data () {
    return {
      expandNav: true
    }
  },

  methods: {
  }
}
</script>

<style scoped>

.logged-content {
  margin-top:51px;
}

</style>
