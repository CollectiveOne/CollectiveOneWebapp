<template lang="html">
  <div v-if="showOutput" class="message-container w3-tag w3-theme-l2 w3-display-topmiddle">
    <p>{{ outputMessage }}</p>
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
