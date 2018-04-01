<template lang="html">
  <div class="">
    
    <div class="w3-row">
      <WSDebug></WSDebug>
    </div>

  </div>
</template>

<script>
import WSDebug from '@/components/websocket/debug/WSDebug.vue'

export default {
  components: {
    WSDebug
  },

  methods: {
  }
}
</script>

<style scoped>

.navbar {
}

.logo {
}

.welcome-header {
  background-color: rgb(247, 247, 247);
  padding-top: 195px;
  height: 500px;
  text-align: center;
}

.welcome-header button {
  width: 150px;
}

</style>
