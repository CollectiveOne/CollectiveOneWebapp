<template lang="html">
  <div class="">
    <div class="w3-row">
      <h4 class="">Create a new token</h4>
    </div>
    <div class="w3-row-padding new-token-inputs">

    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      tokenName: 'tokens',
      tokens: 0,
      valid: true
    }
  },

  watch: {
    tokens () {
      this.emitUpdated()
    },
    tokenName () {
      this.emitUpdated()
    }
  },

  methods: {
    emitUpdated () {
      this.$emit('updated', {
        tokenName: this.tokenName,
        tokens: this.tokens
      })
    }
  }
}
</script>

<style lang="css">
</style>
