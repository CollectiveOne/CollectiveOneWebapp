<template lang="html">
  <router-link :to="{
    name: 'ModelSection',
    params: {
      'initiativeId': this.cardWrapper.initiative.id,
      'sectionId': this.onSection ? this.onSection.id : '',
      'cardWrapperId': this.cardWrapper.id
      }}">
    {{ cardWrapper.title !== '' ? cardWrapper.title : 'card' }}
  </router-link>
</template>

<script>
export default {
  props: {
    onSection: {
      type: Object
    },
    cardWrapper: {
      type: Object
    }
  }
}
</script>

<style scoped>
</style>
