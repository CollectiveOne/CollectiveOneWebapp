<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Create a new type of token</h2>
        </div>

        <div class="w3-container form-container">
          <div class="w3-row-padding">
            <div class="w3-col m6">
              <label class=""><b>Number of Tokens</b></label>
              <input v-model.number="amount" class="w3-input w3-border w3-hover-light-grey" type="number">
            </div>
            <div class="w3-col m6"  :style="{'margin-bottom': '15px'}">
              <label class=""><b>Token Name</b></label>
              <input v-model="name" class="w3-input w3-border w3-hover-light-grey" type="text">
            </div>
          </div>
          <app-error-panel
            :show="tokenNameEmptyShow"
            message="token name cannot be empty define the name of the token.">
          </app-error-panel>
          <app-error-panel
            :show="tokenNameTooLong"
            message="token name too long.">
          </app-error-panel>

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

      </div>
    </div>
  </div>
</template>

<script>
export default {

  components: {
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      amount: 0,
      name: 'tokens',
      tokenNameEmptyError: false
    }
  },

  computed: {
    tokenNameEmpty () {
      return this.name === ''
    },
    tokenNameEmptyShow () {
      return this.tokenNameEmptyError && this.tokenNameEmpty
    },
    tokenNameTooLong () {
      return this.name.length > 10
    }
  },

  methods: {
    closeThis () {
      this.$emit('close')
    },
    tokensSelected (tokenData) {
      this.amount = tokenData.tokens
      this.name = tokenData.tokenName
    },
    accept () {
      var ok = true
      if (this.tokenNameEmpty) {
        ok = false
        this.tokenNameEmptyError = true
      }

      if (this.tokenNameTooLong) {
        ok = false
      }

      if (ok) {
        var assetDto = {
          assetName: this.name,
          ownedByThisHolder: this.amount
        }
        this.axios.post('/1/initiative/' + this.initiativeId + '/token', assetDto)
        .then((response) => {
          this.$store.dispatch('refreshInitiative')
          this.closeThis()
        })
      }
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

.form-container {
  padding-top: 20px;
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
