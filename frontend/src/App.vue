<template>
  <div id="app">
    <div>
        <app-header></app-header>
        <router-view></router-view>
      </div>
  </div>
</template>

<script>
import AppHeader from '@/components/Header.vue'

export default {
  name: 'app',
  components: {
    appHeader: AppHeader
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
</style>
