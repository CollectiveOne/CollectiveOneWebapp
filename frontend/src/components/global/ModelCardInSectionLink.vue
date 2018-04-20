<template lang="html">
  <router-link :to="{
    name: 'ModelSectionCard',
    params: {
      'initiativeId': this.cardWrapper.initiativeId,
      'sectionId': this.onSection ? this.onSection.id : '',
      'cardId': this.cardWrapper.id
      }}">
    {{ cardWrapper.card.title !== '' ? cardWrapper.card.title : 'card' }}
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
