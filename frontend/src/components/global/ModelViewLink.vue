<template lang="html">
  <router-link :to="{
    name: 'ModelView',
    params: {
      'initiativeId': this.view.initiativeId,
      'viewId': this.view.id
      }}">
    {{ view.title }}
  </router-link>
</template>

<script>
export default {
  props: {
    view: {
      type: Object
    }
  }
}
</script>

<style scoped>
</style>
