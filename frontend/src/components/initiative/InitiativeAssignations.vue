<template lang="html">
  <router-view :initiative="initiative"></router-view>
</template>

<script>
export default {
  props: {
    initiative: {
      type: Object
    }
  }
}
</script>

<style scoped>

</style>
