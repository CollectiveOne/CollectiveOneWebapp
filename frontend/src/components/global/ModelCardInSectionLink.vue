<template lang="html">
  <router-link :to="{
    name: 'ModelSectionCard',
    params: {
      'initiativeId': this.onSection ? this.onSection.initiativeId : '',
      'sectionId': this.onSection ? this.onSection.id : '',
      'cardId': this.cardWrapper.id
      }}">
    {{ cardWrapper.card.title !== '' ? cardWrapper.card.title : (cardWrapper.card.text ? (cardWrapper.card.text.length < 25 ? cardWrapper.card.text : cardWrapper.card.text.slice(0,24) + ' ...') : 'card') }}
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
