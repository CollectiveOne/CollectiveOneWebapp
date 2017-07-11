<template lang="html">
  <div class="w3-panel w3-theme-l5 transfer-container">
    <div class="w3-row">
      <div class="w3-col l4">
        <app-value-seal :value="transfer.value" :assetName="transfer.assetName"></app-value-seal>
      </div>

      <div class="w3-col l8 data-col w3-container">
        <div class="w3-row">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>from:</b></label> {{ transfer.senderName }}
          </div>
        </div>
        <div class="w3-row">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>to:</b></label> {{ transfer.receiverName }}
          </div>
        </div>
        <div class="w3-row">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>motive:</b></label> {{ transfer.motive }}
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import ValueSeal from '@/components/transfers/ValueSeal.vue'

export default {
  components: {
    'app-value-seal': ValueSeal
  },
  props: {
    transfer: {
      type: Object
    }
  }
}
</script>

<style scoped>

.transfer-container {
  padding-top: 10px;
  padding-bottom: 10px;
}

.data-col {
}

</style>
