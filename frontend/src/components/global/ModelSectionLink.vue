<template lang="html">
  <router-link :to="{
    name: 'ModelSectionContent',
    params: {
      'initiativeId': this.section ? this.section.initiativeId : '',
      'sectionId': this.section ? this.section.id : ''
      }}">
    {{ this.section ? section.title : '' }}
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
