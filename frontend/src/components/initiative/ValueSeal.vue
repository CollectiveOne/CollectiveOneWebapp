<template lang="html">
  <div class="w3-display-container seal-display-container">
    <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
    <div class="w3-display-middle d2-color" style="width: 100%">
      <div class="w3-row w3-center">
        <b class="w3-large ">{{ tokensString(value) }} {{ assetName }}</b>
      </div>
    </div>
  </div>
</template>

<script>
import { tokensString } from '@/lib/common'

export default {
  props: {
    value: {
      type: Number
    },
    assetName: {
      type: String
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    }
  }
}
</script>

<style scoped>

.seal-display-container {
  display: block;
  height: 70px;
}

.fa-certificate {
  font-size: 70px;
}

</style>
