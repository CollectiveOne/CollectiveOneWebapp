<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ $t('tokens.TRANSFER_TO_INITIATIVE') }}</h2>
        </div>

        <div v-if="initiativeOptions.length > 0" class="this-container w3-container">

          <div class="w3-row">
            <label class=""><b>{{ $t('tokens.TRANSFER_TO') }}
              <span class="w3-small error-text">({{ $t('general.REQUIRED') }})</span></b>
            </label>
            <select v-model="transfer.receiverId"
              class="w3-select initiative-selector" name="transferto"
              :class="{ 'error-input' : subInitiativeEmptyShow }">
              <option v-for="option in initiativeOptions" :value="option.id">
                {{ option.meta.name }}
              </option>
            </select>
          </div>
          <app-error-panel
            :show="subInitiativeEmptyShow"
            :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
          </app-error-panel>
          <br>

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initiativeId="initiative.id"
              :initiativeName="initiative.meta.name"
              :assetId="assetId"
              :showSelector="true"
              @updated="assetsSelected($event)"
              :showError="assetsErrorShow">
            </app-initiative-assets-assigner>
          </div>
          <app-error-panel
            :show="assetsErrorShow"
            :message="$t('tokens.ZERO_TOKENS_ERROR')">
          </app-error-panel>

          <div class="w3-row">
            <label class=""><b>{{ $t('tokens.MOTIVE') }}
              <span class="w3-small error-text">({{ $t('general.REQUIRED') }})</span></b>
            </label>
            <input v-model="transfer.motive"
              class="w3-input w3-hover-light-grey" type="text"
              :class="{ 'error-input' : motiveErrorShow }">
            <app-error-panel
              :show="motiveEmptyShow"
              :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
            </app-error-panel>
            <app-error-panel
              :show="motiveTooLarge"
              :message="$t('general.FIELD_TOO_LONG')">
            </app-error-panel>
            <br>

            <label class=""><b>{{ $t('general.NOTES') }}</b></label>
            <app-markdown-editor
              v-model="transfer.notes"
              :keepBackup="false"
              :showBorder="true">
            </app-markdown-editor>
            <br>
          </div>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">
                {{ $t('general.CANCEL') }}
              </button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">
                {{ $t('general.ACCEPT') }}
              </button>
            </div>
          </div>

        </div>
        <div v-else class="w3-row w3-margin-top">
          <div class="error-panel w3-padding">
            {{ $t('tokens.NO_PARENTS_ERROR', { name: initiative.meta.name }) }}
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

  props: {
    initiative: {
      type: Object
    },
    assetId: {
      type: String
    }
  },

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner
  },

  data () {
    return {
      transfer: {
        motive: '',
        notes: '',
        assets: []
      },
      subInitiativeEmptyError: false,
      motiveEmptyError: false,
      assetsError: false
    }
  },

  computed: {
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
      return this.subInitiativeEmptyError && this.isSubInitiativeEmpty
    },
    isSubInitiativeEmpty () {
      if (typeof (this.transfer.receiverId) === 'undefined') return true
      if (this.transfer.receiverId === '') return true
      return false
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
      this.$emit('close')
    },
    assetsSelected (assets) {
      if (assets.length > 0) {
        this.transfer.assetId = assets[0].assetId
        this.transfer.value = assets[0].value
        this.transfer.senderId = assets[0].senderId
      }
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

      if (this.isSubInitiativeEmpty) {
        this.subInitiativeEmptyError = true
        ok = false
      }

      if (this.assetsAreZero) {
        this.assetsError = true
        ok = false
      }

      if (ok) {
        this.axios.post('/1/initiative/' + this.initiative.id + '/transferToInitiative', this.transfer)
        .then((response) => {
          this.$store.commit('triggerUpdateAssets')
          this.$emit('created')
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
