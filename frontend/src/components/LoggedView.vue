<template lang="html">
  <div class="">
    <app-header @expand-nav="expandNav = !expandNav"></app-header>
    <div class="" style="margin-top:51px">
      <router-view :expandNav="expandNav"></router-view>
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

</style>
