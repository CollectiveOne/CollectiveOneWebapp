<template lang="html">
  <div class="this-container">
    <div class="w3-row">
      <div class="w3-col m6">
        <div class="w3-row">
          <b><span class="w3-text-indigo">Asset: </span>{{ assetsData.assetName }}</b>
        </div>
        <div class="w3-row">
          <div class="w3-col s6">
            <label class="w3-row w3-text-indigo"><b>Existing</b></label>
            <span class="w3-row w3-tag w3-large w3-round w3-theme">{{ totalExistingTokens }}</span>
          </div>
          <div class="w3-col s6">
            <label class="w3-row w3-text-indigo"><b>Available</b></label>
            <span class="w3-row w3-tag w3-large w3-round w3-theme">{{ ownedByThisInitiative }}</span>
          </div>
        </div>
      </div>
      <div class="w3-col m6">
        <label class="w3-text-indigo"><b>Assign</b></label>
        <div class="w3-row-padding tokens-selector">
          <div class="w3-col s6">
            <input v-model.number="tokens" class="w3-input w3-border w3-hover-light-gray w3-round" type="number">
          </div>
          <div class="w3-col s6">
            <div class="w3-row">
              <div class="w3-col s10">
                <input v-model.number="percentage" class="w3-input w3-border w3-hover-light-gray w3-round" type="text">
              </div>
              <div class="w3-col s2">
                <i class="fa fa-percent" aria-hidden="true"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { tokensString } from '../../lib/common'

export default {
  props: {
    assetsData: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      tokens: 0,
      percentage: 0
    }
  },

  computed: {
    totalExistingTokens () {
      return tokensString(this.assetsData.totalExistingTokens)
    },
    ownedByThisInitiative () {
      return tokensString(this.assetsData.ownedByThisHolder)
    }
  },

  watch: {
    tokens () {
      let perc = this.tokens / this.assetsData.totalExistingTokens * 100
      this.percentage = Math.round(perc * 1000) / 1000
      this.$emit('updated', {
        assetId: this.assetsData.assetId,
        assetName: this.assetsData.assetName,
        value: this.tokens
      })
    },
    percentage () {
      let toks = this.percentage / 100 * this.assetsData.totalExistingTokens
      this.tokens = Math.round(toks * 1000) / 1000
      this.$emit('updated', {
        assetId: this.assetsData.assetId,
        assetName: this.assetsData.assetName,
        value: this.tokens
      })
    }
  }
}
</script>

<style scoped>

.this-container {
  text-align: center;
}

.fa-percent {
  color: #607d8b;
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.tokens-selector {
  margin-top: 15px;
}

</style>
