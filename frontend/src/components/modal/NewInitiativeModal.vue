<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ $t('initiatives.NEW_INIT_MODAL') }}</h2>
        </div>

        <div class="w3-container form-container">

          <br>
          <label class="">
            <b>{{ $t('general.NAME') }} <span class="w3-small error-text">({{ $t('general.REQUIRED') }})</span></b>
          </label>
          <input v-model="name"
            class="w3-input w3-hover-light-grey" type="text"
            :class="{ 'error-input' : nameErrorShow }">
          <app-error-panel
            :show="nameEmptyShow"
            :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
          </app-error-panel>
          <app-error-panel
            :show="nameTooLongShow"
            :message="$t('general.FIELD_TOO_LONG')">
          </app-error-panel>

          <br>
          <label class="">
            <b>{{ $t('general.DESCRIPTION') }}</b>
          </label>
          <textarea v-model="driver"
            class="w3-input w3-border w3-round w3-hover-light-grey">
          </textarea>
          <hr>

          <div class="w3-container assets-selector-div">
            <div class="w3-row-padding">
              <div class="w3-col m4">
                <h5><b>{{ $t('initiatives.CREATE_NEW_TOKEN_Q') }}</b></h5>
              </div>
              <div class="w3-col m4">
                <button class="w3-button transfer-button"
                  :class="{'app-button': createTokenFlag, 'app-button-light': !createTokenFlag, }"
                  type="button" name="button"
                  @click="createTokenFlag = true">
                  {{ $t('general.YES') }}
                </button>
              </div>
              <div class="w3-col m4">
                <button class="w3-button transfer-button"
                  :class="{'app-button': !createTokenFlag, 'app-button-light': createTokenFlag, }"
                  type="button" name="button"
                  @click="createTokenFlag = false">
                  {{ $t('general.NO') }}
                </button>
              </div>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="createTokenFlag" class="">
                  <div class="w3-row-padding">
                     <div class="w3-col m4">
                       <label class=""><b>{{ $t('tokens.NUMBER_OF_TOKENS') }}</b></label>
                       <input v-model.number="ownTokens.ownedByThisHolder" class="w3-input w3-border w3-hover-light-grey" type="number">
                     </div>
                     <div class="w3-col m4"  :style="{'margin-bottom': '15px'}">
                       <label class=""><b>{{ $t('tokens.TOKEN_NAME') }}</b></label>
                       <input v-model="ownTokens.assetName" class="w3-input w3-border w3-hover-light-grey" type="text">
                     </div>
                   </div>
                   <app-error-panel
                     :show="tokenNameEmptyShow"
                     :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
                   </app-error-panel>
                   <app-error-panel
                     :show="tokenNameTooLong"
                     :message="$t('general.FIELD_TOO_LONG')">
                   </app-error-panel>
                </div>
              </transition>
            </div>
          </div>

          <hr>

          <label class="init-contr-label">
            <b>{{ $t('initiatives.INITIAL_MEMBERS') }}:</b>
          </label>
          <app-members-table-container
            :members="members"
            :canEdit="true">
          </app-members-table-container>
          <app-error-panel
            :show="membersEmptyShow"
            :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
          </app-error-panel>
          <app-error-panel
            :show="noAdmingShow"
            :message="$t('initiatives.ONE_ADMIN_ATLEAST')">
          </app-error-panel>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">{{ $t('general.CANCEL') }}</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">{{ $t('general.ACCEPT') }}</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import NewToken from './support/NewToken.vue'
import MembersTableContainer from '@/components/user/MembersTableContainer.vue'

/* eslint-disable no-unused-vars */
import { tokensString } from '@/lib/common'
/* eslint-enable no-unused-vars */

export default {
  props: {
    asSubinitiative: {
      type: Boolean,
      default: false
    },
    parentInitId: {
      type: String,
      default: ''
    }
  },

  components: {
    'app-new-token': NewToken,
    'app-members-table-container': MembersTableContainer
  },

  data () {
    return {
      parentInitiative: null,
      name: '',
      driver: '',
      ownTokens: {
        ownedByThisHolder: 0,
        assetName: 'tokens'
      },
      members: [],
      nameEmptyError: false,
      driverEmptyError: false,
      membersEmptyError: false,
      noAdminError: false,
      tokenNameEmptyError: false,
      createTokenFlag: true
    }
  },

  computed: {
    nameErrorShow () {
      return this.nameEmptyShow || this.nameTooLongShow
    },
    nameEmptyShow () {
      return (this.nameEmptyError && (this.name === ''))
    },
    nameTooLongShow () {
      return (this.nameTooLong)
    },
    nameTooLong () {
      return this.name.length > 42
    },
    tokenNameEmpty () {
      return this.ownTokens.assetName === ''
    },
    tokenNameEmptyShow () {
      return this.tokenNameEmptyError && this.tokenNameEmpty
    },
    tokenNameTooLong () {
      return this.ownTokens.assetName.length > 10
    },
    membersEmptyShow () {
      return this.membersEmptyError && (this.members.length === 0)
    },
    noAdmingShow () {
      return this.noAdminError && !this.oneAdminExist
    },
    oneAdminExist () {
      for (var ix in this.members) {
        if (this.members[ix].role === 'ADMIN') {
          return true
        }
      }
      return false
    }
  },

  watch: {
    parentId () {
      this.updateParent()
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    tokensString (v) {
      return tokensString(v)
    },

    parentAssetsSelected (assets) {
      this.otherAssetsTransfers = JSON.parse(JSON.stringify(assets))
    },

    parentInitiativeUpdated (initiative) {
      this.parentInitiative = initiative
    },

    closeThis () {
      this.$emit('close')
    },

    accept () {
      var ok = true

      if (this.name === '') {
        ok = false
        this.nameEmptyError = true
      } else {
        if (this.nameTooLong) {
          ok = false
        }
      }

      if (this.tokenNameEmpty) {
        ok = false
        this.tokenNameEmptyError = true
      }

      if (this.tokenNameTooLong) {
        ok = false
      }

      if (this.members.length === 0) {
        ok = false
        this.membersEmptyError = true
      }

      if (!this.oneAdminExist) {
        ok = false
        this.noAdminError = true
      }

      if (ok) {
        let initiativeDto = {
          asSubinitiative: this.asSubinitiative,
          parentInitiativeId: this.asSubinitiative ? this.parentInitiative.id : null,
          name: this.name,
          driver: this.driver,
          members: this.members,
          createToken: this.createTokenFlag,
          ownTokens: this.ownTokens
        }

        this.axios.post('/1/initiative/create', initiativeDto).then((response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.commit('setExpandNav', false)
            this.$store.dispatch('updateMyInitiatives')
            this.$router.push({name: 'Initiative', params: {'initiativeId': response.data.elementId}})
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
          /* weird bug that unauthorizes the POST initiative endpoint only
          *  maybe a thing with auto REST endpoints ? */
          console.log(error.response.status === 401)
        })
      }
    },

    updateParent () {
      if (this.parentInitId !== '') {
        this.axios.get('/1/initiative/' + this.parentInitId, {
          params: {
            addAssets: true
          }
        }).then((response) => {
          this.parentInitiative = response.data.data
        })
      }
    }
  },

  mounted () {
    if (this.$store.state.user.profile) {
      var member = {}
      member.user = this.$store.state.user.profile
      member.role = 'ADMIN'
      this.members.push(member)
    }

    this.updateParent()
  }
}
</script>

<style scoped>

.form-container {
  padding-top: 0px;
  padding-bottom: 35px;
}

.transfer-button {
  width: 100%;
}

.members-container {
  margin-top: 10px;
  padding-bottom: 20px !important;
}

.new-contr-row {
  margin-top: 20px;
}

.add-btn {
  width: 100%;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
