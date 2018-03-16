<template lang="html">
  <router-link :to="{
    name: 'ModelCardAlone',
    params: {
      'initiativeId': this.cardWrapper.initiativeId,
      'cardWrapperId': this.cardWrapper.id
      }}">
    {{ cardWrapper.card.title !== '' ? cardWrapper.card.title : cardWrapper.card.text.length < 25 ? cardWrapper.card.text : cardWrapper.card.text.slice(0,24) + ' ...'}}
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
