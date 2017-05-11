<template lang="html">
  <div class="">
    <app-header></app-header>
    <div class="w3-container w3-content" style="max-width:1400px;margin-top:80px">
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

  data () {
    return {
    }
  },

  methods: {
  }
}
</script>

<style scoped>

</style>
