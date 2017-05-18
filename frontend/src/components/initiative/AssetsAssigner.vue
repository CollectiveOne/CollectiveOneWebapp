<template lang="html">
  <div class="w3-col m3">
    <label class="w3-row w3-text-indigo"><b>Total Existing Tokens</b></label>
    <p><span class="w3-row w3-tag w3-large w3-round w3-theme">5,000</span></p>
  </div>
  <div class="w3-col m3">
    <label class="w3-row w3-text-indigo"><b>Available Tokens</b></label>
    <p><span class="w3-row w3-tag w3-large w3-round w3-theme">5,000</span></p>
  </div>
  <div class="w3-col m6">
    <label class="w3-text-indigo"><b>Assign</b></label>
    <div class="w3-row-padding tokens-selector">
      <div class="w3-col m6">
        <input v-model.number="tokens" class="w3-input w3-border w3-hover-light-gray" type="number">
      </div>
      <div class="w3-col m6">
        <div class="w3-row">
          <div class="w3-col s10">
            <input v-model.number="percentage" class="w3-input w3-border w3-hover-light-gray" type="text">
          </div>
          <div class="w3-col s2">
            <i class="fa fa-percent" aria-hidden="true"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
}
</script>

<style lang="css">
</style>
