<template lang="html">
  <div v-if="showOutput" class="message-container w3-panel w3-teal w3-card-4 w3-display-topmiddle">
    <h5>{{ outputMessage }}</h5>
  </div>
</template>

<script>
export default {

  computed: {
    showOutput () {
      return this.$store.state.messages.showOutputMessage
    },

    outputMessage () {
      return this.$store.state.messages.outputMessage
    }
  }
}
</script>

<style scoped>

.message-container {
  z-index: 1300;
}

</style>
