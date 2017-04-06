<template>
  <div id="app">
    <div class="container-fluid">
        <app-header></app-header>
        <router-view></router-view>
      </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import AppHeader from '@/components/Header.vue'

export default {
  name: 'app',
  components: {
    appHeader: AppHeader
  },

  methods: {
    ...mapActions(['updateProjects'])
  },

  created () {
    this.updateProjects()
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.container-fluid {
  padding-left: 0px;
  padding-right: 0px;
}
</style>
