<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div v-if="assetData" class="w3-container form-container">

          <div class="w3-container w3-border-bottom">
            <h2>{{ $t('tokens.CONFIGURE_TOKEN') }}: {{ assetData.assetName }}</h2>
          </div>

          <div class="w3-row w3-margin-top">
            <b>{{ $t('tokens.ACTION') }}:</b>
          </div>
          <div class="w3-row-padding bottom-btns-row">
            <div class="w3-col m6 s12">
              <button class="w3-button" :class="{ 'app-button': isMint, 'app-button-light': !isMint }"
                @click="action = 'mint'">
                {{ $t('tokens.MINT') }}
              </button>
            </div>
            <div class="w3-col m6 s12">
              <button class="w3-button" :class="{ 'app-button': isBurn, 'app-button-light': !isBurn }"
              @click="action = 'burn'">
                {{ $t('tokens.BURN') }}
              </button>
            </div>
          </div>

          <div class="w3-row label-row w3-margin-top">
            <label class=""><b>{{ isMint ? $t('tokens.AMOUNT_TO_BE_CREATED') : $t('tokens.AMOUNT_TO_BE_BURNT') }}:</b></label>
            ({{ $t('tokens.STILL_AVAILABLE') }}: {{ availableToThisInitiativeValStr }} {{ assetData.assetName }})
          </div>
          <div class="w3-row-padding">
            <div class="w3-col s6">
              <input @blur="valueUpdated($event)" :value="value.toFixed(1)" class="w3-input w3-border w3-hover-light-grey w3-round" type="number" min="0" :step="assetData.totalUnderThisHolder / 100">
            </div>
            <div class="w3-col s6">
              <div class="w3-row">
                <div class="w3-col s10">
                  <input @blur="percentageUpdated($event)" :value="percentage.toFixed(1)" class="w3-input w3-border w3-hover-light-grey w3-round" type="number" min="0" step="1">
                </div>
                <div class="w3-col s2 ">
                  <i class="fa fa-percent" aria-hidden="true"></i>
                </div>
              </div>
            </div>
          </div>
          <app-error-panel
            :show="assetsZeroShow"
            :message="$t('tokens.AMOUNT_LARGER_THAN_ZERO')">
          </app-error-panel>
          <app-error-panel
            :show="assetsNotAvailableShow"
            :message="noAssetsAvailableErorrMsg">
          </app-error-panel>

          <div class="w3-row w3-margin-top">
            <label class=""><b>Motive <span class="w3-small error-text">(required)</span></b></label>
            <input v-model="motive" class="w3-input w3-hover-light-grey" :class="{ 'error-input' : motiveErrorShow }" type="text">
            <app-error-panel
              :show="motiveEmptyShow"
              message="please provide a motive for minting these tokens for future reference">
            </app-error-panel>
            <app-error-panel
              :show="motiveTooLarge"
              message="motive too large, please use the notes for long annotations">
            </app-error-panel>

            <br>

            <label class=""><b>Notes</b></label>
            <textarea v-model="notes" class="w3-input w3-border w3-round w3-hover-light-grey"></textarea>
          </div>

          <div class="w3-row summary-row">

            <div class="w3-row w3-center w3-margin-top">
              <b>{{ isMint ? $t('tokens.AMOUNT_TO_BE_CREATED') : $t('tokens.AMOUNT_TO_BE_BURNT') }}: {{ value }} {{ assetData.assetName }}</b>
            </div>

            <div class="w3-row w3-center w3-margin-top">
              <b>{{ $t('tokens.NEW_TOTAL_SUPPLY') }}: {{ newAmountStr }} {{ assetData.assetName }}</b>
            </div>

            <div class="w3-row w3-center w3-margin-top">
              <b>{{ $t('tokens.NEW_TOTAL_AVAILABLE') }}: {{ newAvailableStr }} {{ assetData.assetName }}</b>
            </div>
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

        </div>
        <div v-else class="w3-row w3-center loader-gif-container">
          <img class="" src="../../assets/loading.gif" alt="">
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { tokensString } from '@/lib/common'

export default {

  props: {
    assetId: {
      type: String
    },
    actionInit: {
      type: String,
      default: 'mint'
    },
    initiativeId: {
      type: String
    }
  },

  data () {
    return {
      assetData: null,
      value: 0,
      percentage: 0,
      motive: '',
      notes: '',
      motiveEmptyError: false,
      assetsZeroError: false,
      action: ''
    }
  },

  computed: {
    isMint () {
      return this.action === 'mint'
    },
    isBurn () {
      return this.action === 'burn'
    },
    newAmountVal () {
      return this.assetData.totalExistingTokens + (this.isMint ? this.value : -this.value)
    },
    availableToThisInitiativeVal () {
      return this.assetData.ownedByThisHolder - this.assetData.totalPending
    },
    availableToThisInitiativeValStr () {
      return tokensString(this.availableToThisInitiativeVal)
    },
    newAvailable () {
      return this.availableToThisInitiativeVal + (this.isMint ? this.value : -this.value)
    },
    newAvailableStr () {
      return tokensString(this.newAvailable)
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
    },
    noAssetsAvailableErorrMsg () {
      return (this.assetData.ownedByThisHolder > 0 ? this.$t('tokens.ONLY') + ' ' : ' ') +
        this.assetData.ownedByThisHolder.toFixed(1) + ' ' +
        this.assetData.assetName + ' ' +
        this.$t('tokens.AVAILABLE')
    },
    assetsNotAvailableShow () {
      return this.isBurn ? (this.availableToThisInitiativeVal < this.value) : false
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    },
    updateTokenData () {
      this.axios.get('/1/token/' + this.assetId, {
        params: {
          includeSubinitiatives: true,
          initiativeIdStr: this.initiativeId
        } }).then((response) => {
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
      this.$emit('close')
    },
    accept () {
      var ok = true
      if (this.assetsAreZero) {
        ok = false
        this.assetsZeroError = true
      }

      if (this.isBurn && this.assetsNotAvailableShow) {
        ok = false
      }

      if (this.motive === '') {
        this.motiveEmptyError = true
        ok = false
      }

      if (this.motiveTooLarge) {
        ok = false
      }

      if (ok) {
        if (this.isMint) {
          let mintDto = {
            value: this.value,
            motive: this.motive,
            notes: this.notes
          }
          this.axios.put('/1/token/' + this.assetData.assetId + '/mint', mintDto)
            .then((response) => {
              this.$store.commit('triggerUpdateAssets')
              this.closeThis()
            })
        } else {
          let burnDto = {
            value: this.value,
            motive: this.motive,
            notes: this.notes
          }
          this.axios.put('/1/token/' + this.assetData.assetId + '/burn', burnDto)
            .then((response) => {
              this.$store.commit('triggerUpdateAssets')
              this.closeThis()
            })
        }
      }
    }
  },

  mounted () {
    this.updateTokenData()
    this.action = this.actionInit
  }
}
</script>

<style scoped>

.form-container {
  padding-top: 0px;
  padding-bottom: 35px;
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
