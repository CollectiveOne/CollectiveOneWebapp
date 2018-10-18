<template lang="html">
  <router-link :to="{
    name: 'ModelSectionContent',
    params: {
      'initiativeId': this.section ? this.section.initiativeId : '',
      'sectionId': this.section ? this.section.id : ''
      }}">
    {{ this.anchorText }}
  </router-link>
</template>

<script>
export default {
  props: {
    section: {
      type: Object
    },
    text: {
      type: String,
      default: ''
    }
  },

  computed: {
    anchorText () {
      return this.text === '' ? (this.section ? this.section.title : '') : this.text
    }
  }
}
</script>

<style scoped>
</style>
