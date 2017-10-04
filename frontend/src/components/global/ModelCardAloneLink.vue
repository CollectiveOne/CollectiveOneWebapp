<template lang="html">
  <router-link :to="{
    name: 'ModelCardAlone',
    params: {
      'initiativeId': this.cardWrapper.initiativeId,
      'cardWrapperId': this.cardWrapper.id
      }}">
    {{ cardWrapper.card.title !== '' ? cardWrapper.card.title : 'card' }}
  </router-link>
</template>

<script>
export default {
  props: {
    cardWrapper: {
      type: Object
    }
  }
}
</script>

<style scoped>
</style>
