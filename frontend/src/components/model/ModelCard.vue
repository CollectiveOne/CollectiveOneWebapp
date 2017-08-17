<template lang="html">
  <div class="">
    <div v-if="card.title !== ''" class="">
      <b>{{ card.title }}</b>
    </div>
    <div class="card-text">
      <p>{{ card.text }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'model-card',

  components: {
  },

  props: {
    card: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>

.card-text p {
  margin-top: 0px;
}

</style>
