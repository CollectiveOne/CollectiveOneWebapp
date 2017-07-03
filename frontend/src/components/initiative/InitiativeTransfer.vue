<template lang="html">
  <div class="w3-card-2 w3-padding this-container">
    <div class="w3-row-padding">
      <div class="w3-col l4 w3-center">
        <div class="w3-container">
          <div class="w3-display-container transfer-container">
            <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
            <div class="w3-display-middle d2-color" style="width: 100%">
              <div class="w3-row">
                <b class="w3-xlarge ">{{ tokensString(transfer.value) }} {{ transfer.assetName }}</b>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="w3-col l8 status-col">
        <div class="w3-row">
          <div class="w3-tag w3-theme-l2 w3-round">
            <b>{{ transfer.receiverName }}</b>
          </div>
        </div>
        <h5 class="d2-color w3-padding w3-center">{{ transfer.motive }}</h5>
      </div>

    </div>
  </div>
</template>

<script>
import { tokensString } from '@/lib/common'

export default {
  props: {
    transfer: {
      type: Object
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

.this-container {
  padding-top: 20px !important;
  padding-bottom: 15px !important;
}

.transfer-container {
  height: 100px;
}

.fa-certificate {
  font-size: 100px;
}

.status-col {
}

.status-col .w3-tag {
  width: 100%;
  margin-top: 5px;
}

</style>
