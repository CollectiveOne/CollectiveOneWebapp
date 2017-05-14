<template lang="html">
  <div class="w3-row-padding">
    <div class="w3-col m4 w3-hide-small">
      <app-initiatives-nav></app-initiatives-nav>
    </div>
    <div class="w3-col m8">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import InitiativesNav from './InitiativesNav.vue'

export default {
  components: {
    AppInitiativesNav: InitiativesNav
  }
}
</script>

<style lang="css">
</style>
