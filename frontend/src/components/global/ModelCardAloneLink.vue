<template lang="html">
  <span class="">
   {{ cardWrapper.card.title !== '' ? cardWrapper.card.title : (cardWrapper.card.text.length < 25 ? cardWrapper.card.text : cardWrapper.card.text.slice(0,24) + ' ...') }}
 </span>
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
