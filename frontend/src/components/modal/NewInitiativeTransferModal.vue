<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Direct transfer to a parent or child initiative</h2>
        </div>

        <div v-if="initiativeOptions.length > 0" class="this-container w3-container">

          <div class="w3-row">
            <label class=""><b>Transfer to <span class="w3-small error-text">(required)</span></b></label>
            <select v-model="transfer.receiverId"
              class="w3-select initiative-selector" name="transferto"
              :class="{ 'error-input' : subInitiativeEmptyShow }">
              <option v-for="option in initiativeOptions" :value="option.id">{{ option.meta.name }}</option>
            </select>
          </div>
          <app-error-panel
            :show="subInitiativeEmptyShow"
            message="please select a subinitiative to transfer the tokens to">
          </app-error-panel>
          <br>

          <div class="w3-row">
            <label class=""><b>Motive <span class="w3-small error-text">(required)</span></b></label>
            <input v-model="transfer.motive"
              class="w3-input w3-hover-light-grey" type="text"
              :class="{ 'error-input' : motiveErrorShow }">
            <app-error-panel
              :show="motiveEmptyShow"
              message="please provide a motive for this transfer for future reference">
            </app-error-panel>
            <app-error-panel
              :show="motiveTooLarge"
              message="motive too large, please use the notes for long annotations">
            </app-error-panel>
            <br>

            <label class=""><b>Notes</b></label>
            <textarea v-model="transfer.notes" class="w3-input w3-border w3-round w3-hover-light-grey"></textarea>
            <br>
          </div>

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initInitiativeId="initiative.id"
              @updated="assetsSelected($event)"
              :showError="assetsErrorShow">
            </app-initiative-assets-assigner>
          </div>
          <app-error-panel
            :show="assetsErrorShow"
            message="please select the amount of assets to be transfer">
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
        <div v-else class="w3-row w3-margin-top">
          <div class="error-panel w3-padding">
            {{ initiative.meta.name }} does not have parent or child initiatives
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
receiverId
<script>
import { mapActions } from 'vuex'
import InitiativeAssetsAssigner from '@/components/transfers/InitiativeAssetsAssigner.vue'

export default {

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner
  },

  data () {
    return {
      transfer: {
        motive: '',
        notes: '',
        receiverId: ''
      },
      subInitiativeEmptyError: false,
      motiveEmptyError: false,
      assetsError: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    initiativeOptions () {
      var options = []

      if (this.initiative.parents.length > 0) {
        options.push(this.initiative.parents[this.initiative.parents.length - 1])
      }

      for (var ix in this.initiative.subInitiatives) {
        options.push(this.initiative.subInitiatives[ix])
      }

      return options
    },
    motiveEmptyShow () {
      return this.motiveEmptyError && this.transfer.motive === ''
    },
    motiveErrorShow () {
      return this.motiveEmptyShow || this.motiveTooLarge
    },
    motiveTooLarge () {
      return this.transfer.motive.length > 55
    },
    subInitiativeEmptyShow () {
      return this.subInitiativeEmptyError && this.transfer.receiverId === ''
    },
    assetsAreZero () {
      if (this.transfer.assetId) {
        if (this.transfer.value > 0) {
          return false
        } else {
          return true
        }
      } else {
        return true
      }
    },
    assetsErrorShow () {
      return this.assetsError && this.assetsAreZero
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    closeThis () {
      this.$store.commit('showNewInitiativeTransferModal', false)
    },
    assetsSelected (assets) {
      this.transfer.assetId = assets[0].assetId
      this.transfer.value = assets[0].value
      this.transfer.senderId = assets[0].senderId
    },
    accept () {
      var ok = true
      if (this.transfer.motive === '') {
        this.motiveEmptyError = true
        ok = false
      }

      if (this.motiveTooLarge) {
        ok = false
      }

      if (this.transfer.receiverId === '') {
        this.subInitiativeEmptyError = true
        ok = false
      }

      if (this.assetsAreZero) {
        this.assetsError = true
        ok = false
      }

      if (ok) {
        this.axios.post('/1/secured/initiative/' + this.transfer.senderId + '/transferToInitiative', this.transfer)
        .then((response) => {
          this.$store.commit('triggerUpdateAssets')
          this.closeThis()
        })
      }
    }
  }

}
</script>



<style scoped>

.this-container {
  padding-top: 10px;
  padding-bottom: 30px;
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

.initiative-selector {
  margin-top: 5px;
  padding-left: 10px;
}

.assset-assigner {
  margin-bottom: 10px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
