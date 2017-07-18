<template lang="html">
  <div class="">
    <div class="w3-row">
      <h4 class="">Create a new token</h4>
    </div>
    <div class="w3-row-padding new-token-inputs">
      <div class="w3-col m4">
        <label class=""><b>Number of Tokens</b></label>
        <input v-model.number="tokens" class="w3-input w3-border w3-hover-light-grey" type="number">
      </div>
      <div class="w3-col m4"  :style="{'margin-bottom': '15px'}">
        <label class=""><b>Token Name</b></label>
        <input v-model="tokenName" class="w3-input w3-border w3-hover-light-grey" type="text">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      tokenName: 'tokens',
      tokens: 0
    }
  },

  watch: {
    tokens () {
      this.$emit('updated', {
        tokenName: this.tokenName,
        tokens: this.tokens
      })
    },
    tokenName () {
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
