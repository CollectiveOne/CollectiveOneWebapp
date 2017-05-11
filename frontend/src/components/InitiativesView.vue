<template lang="html">
  <div class="row">
    <div class="col-4">
      <app-initiatives-nav></app-initiatives-nav>
    </div>
    <div class="col-8">
      <app-initiatives-content></app-initiatives-content>
    </div>
  </div>
</template>

<script>
import InitiativesNav from './InitiativesNav.vue'
import InitiativesContent from './InitiativesContent.vue'

export default {
  components: {
    AppInitiativesNav: InitiativesNav,
    AppInitiativesContent: InitiativesContent
  }
}
</script>

<style lang="css">
</style>
