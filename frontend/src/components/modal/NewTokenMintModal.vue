<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Create brand new tokens</h2>
        </div>

        <form v-if="assetData" class="w3-container">

          <div class="w3-row w3-center">
            <div class="w3-display-container amount-container">
              <div class="w3-display-middle " style="width: 100%">
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
            <label class=""><b>Amount to be created</b></label>
          </div>
          <div class="w3-row-padding">
            <div class="w3-col s6">
              <input @input="valueUpdated($event)" :value="value" class="w3-input w3-border w3-hover-light-grey w3-round" type="number" min="0" :step="assetData.totalUnderThisHolder / 100">
            </div>
            <div class="w3-col s6">
              <div class="w3-row">
                <div class="w3-col s10">
                  <input @input="percentageUpdated($event)" :value="percentage" class="w3-input w3-border w3-hover-light-grey w3-round" type="number" min="0" step="1">
                </div>
                <div class="w3-col s2 ">
                  <i class="fa fa-percent" aria-hidden="true"></i>
                </div>
              </div>
            </div>
          </div>
          <div v-if="assetsZeroShow" class="w3-row w3-tag error-panel error-row w3-round">
            plese select an amount larger than 0
          </div>

          <br>
          <div class="w3-row">
            <label class=""><b>Motive</b></label>
            <input v-model="motive" class="w3-input w3-hover-light-grey" :class="{ 'error-input' : motiveErrorShow }" type="text">
            <div v-if="motiveEmptyShow" class="w3-row w3-tag error-panel error-row w3-round">
              please provide a motive for this transfer for future reference
            </div>
            <div v-if="motiveTooLarge" class="w3-row w3-tag error-panel error-row w3-round">
              motive too large, please use the notes for long annotations
            </div>
            <br>

            <label class=""><b>Notes</b></label>
            <textarea v-model="notes" class="w3-input w3-border w3-round w3-hover-light-grey"></textarea>
            <br>
          </div>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
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

  data () {
    return {
      assetData: null,
      value: 0,
      percentage: 0,
      motive: '',
      notes: '',
      motiveEmptyError: false,
      assetsZeroError: false
    }
  },

  computed: {
    assetId () {
      return this.$store.state.modals.assetIdForMint
    },
    newAmountVal () {
      return this.assetData.totalExistingTokens + this.value
    },
    newAmountStr () {
      return tokensString(this.newAmountVal)
    },
    motiveErrorShow () {
      return this.motiveEmptyShow || this.motiveTooLarge
    },
    motiveEmptyShow () {
      return this.motiveEmptyError && this.motive === ''
    },
    motiveTooLarge () {
      return this.motive.length > 55
    },
    assetsAreZero () {
      return this.value <= 0
    },
    assetsZeroShow () {
      return this.assetsZeroError && this.assetsAreZero
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    },
    updateTokenData () {
      this.axios.get('/1/secured/token/' + this.assetId).then((response) => {
        this.assetData = response.data.data
      })
    },
    valueUpdated (event) {
      this.value = Number(event.target.value)
      this.percentage = this.value / this.assetData.totalExistingTokens * 100
    },
    percentageUpdated (event) {
      this.percentage = Number(event.target.value)
      this.value = this.percentage / 100 * this.assetData.totalExistingTokens
    },
    closeThis () {
      this.$store.commit('showNewTokenMintModal', { show: false })
    },
    accept () {
      debugger
      var ok = true
      if (this.assetsAreZero) {
        ok = false
        this.assetsZeroError = true
      }

      if (this.motive === '') {
        this.motiveEmptyError = true
        ok = false
      }

      if (this.motiveTooLarge) {
        ok = false
      }

      if (ok) {
        var mintDto = {
          value: this.value,
          motive: this.motive,
          notes: this.notes
        }
        this.axios.put('/1/secured/token/' + this.assetData.assetId + '/mint', mintDto)
        .then((response) => {
          this.$store.commit('triggerUpdateAssets')
          this.closeThis()
        })
      }
    }
  },

  mounted () {
    this.updateTokenData()
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
