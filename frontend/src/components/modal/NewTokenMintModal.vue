<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>Create brand new tokens</h2>
        </div>

        <form class="w3-container">

          <div class="w3-row w3-center">
            <div class="w3-display-container amount-container">
              <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
              <div class="w3-display-middle d2-color" style="width: 100%">
                <div class="w3-row">
                  <b class="w3-xlarge ">{{ tokensString(this.assetData.totalUnderThisHolder) }} {{ assetData.assetName }}</b>
                </div>
                <div class="w3-row">
                  <b class="w3-large">current total existing</b>
                </div>
              </div>
            </div>
          </div>

          <div class="w3-row w3-center">
            <div class="w3-display-container amount-container">
              <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
              <div class="w3-display-middle d2-color" style="width: 100%">
                <div class="w3-row">
                  <b class="w3-xlarge ">{{ newAmountStr }} {{ assetData.assetName }}</b>
                </div>
                <div class="w3-row">
                  <b class="w3-large">new total existing</b>
                </div>
              </div>
            </div>
          </div>
          <div class="w3-row label-row">
            <label class="w3-text-indigo"><b>Amount to be created</b></label>
          </div>
          <div class="w3-row-padding">
            <div class="w3-col s6">
              <input @focusout="valueUpdated($event)" :value="value" class="w3-input w3-border w3-hover-light-gray w3-round" type="number" min="0" :step="assetData.totalUnderThisHolder / 100">
            </div>
            <div class="w3-col s6">
              <div class="w3-row">
                <div class="w3-col s10">
                  <input @focusout="percentageUpdated($event)" :value="percentage" class="w3-input w3-border w3-hover-light-gray w3-round" type="number" min="0" step="1">
                </div>
                <div class="w3-col s2 d2-color">
                  <i class="fa fa-percent" aria-hidden="true"></i>
                </div>
              </div>
            </div>
          </div>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-light-gray w3-round" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-theme w3-round" @click="accept()">Accept</button>
            </div>
          </div>

        </form>

      </div>
    </div>
  </div>
</template>

<script>
import { tokensString } from '@/lib/common'

export default {
  props: {
    assetData: {
      type: Object
    }
  },

  data () {
    return {
      value: 0,
      percentage: 0
    }
  },

  computed: {
    newAmountVal () {
      return this.assetData.totalUnderThisHolder + this.value
    },
    newAmountStr () {
      return tokensString(this.newAmountVal)
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    },
    valueUpdated (event) {
      this.value = Number(event.target.value)
      let perc = this.value / this.assetData.totalUnderThisHolder * 100
      this.percentage = Math.round(perc * 1000) / 1000
    },
    percentageUpdated (event) {
      this.percentage = Number(event.target.value)
      let toks = this.percentage / 100 * this.assetData.totalUnderThisHolder
      this.value = Math.round(toks * 1000) / 1000
    },
    closeThis () {
      this.$emit('close-this')
    },
    accept () {
      this.axios.put('/1/secured/token/' + this.assetData.assetId + '/mint', {}, {
        params: {
          amount: this.value
        }
      }).then((response) => {
        this.$emit('please-update')
        this.closeThis()
      })
    }
  }

}
</script>

<style scoped>

.w3-modal {
  display: block;
}

.close-div {
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-times {
  color: rgb(255, 255, 255);
  margin-right: 20px;
  margin-top: 20px;
}

form {
  padding-top: 0px;
  padding-bottom: 35px;
}

.bottom-btns-row button {
  width: 100%;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.amount-container {
  height: 150px;
}

.fa-certificate {
  font-size: 100px;
}


</style>
