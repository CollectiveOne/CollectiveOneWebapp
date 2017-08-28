<template lang="html">
  <div class="w3-display-container seal-display-container">
    <div class="w3-display-middle" style="width: 100%">
      <div class="w3-row w3-center">
        <b class="w3-xlarge ">{{ tokensString(value) }} {{ assetName }}</b>
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

.fa {
  font-size: 70px;
}

</style>
