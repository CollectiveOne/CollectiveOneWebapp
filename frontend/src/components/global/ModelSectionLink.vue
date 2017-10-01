<template lang="html">
  <router-link :to="{
    name: 'ModelSection',
    params: {
      'initiativeId': this.section.initiativeId,
      'sectionId': this.section.id
      }}">
    {{ section.title }}
  </router-link>
</template>

<script>
export default {
  props: {
    section: {
      type: Object
    }
  }
}
</script>

<style scoped>
</style>
