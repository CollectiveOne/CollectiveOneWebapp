<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>New Initiative</h2>
        </div>

        <div class="w3-container form-container">

          <br>
          <label class=""><b>Name <span class="w3-small error-text">(required)</span></b></label>
          <input id="T_nameInput" v-model="name"
            class="w3-input w3-hover-light-grey" type="text"
            :class="{ 'error-input' : nameErrorShow }">
          <app-error-panel
            :show="nameEmptyShow"
            message="please select a name for this subinitiative">
          </app-error-panel>
          <app-error-panel
            :show="nameTooLongShow"
            message="name too long">
          </app-error-panel>

          <br>
          <label class=""><b>Purpose <span class="w3-small error-text">(required)</span></b></label>
          <textarea  id="T_purposeInput" v-model="driver"
            class="w3-input w3-border w3-round w3-hover-light-grey"
            :class="{ 'error-input' : driverErrorShow }">
          </textarea>
          <app-error-panel
            :show="driverErrorShow"
            message="please include the purpose of this initiative">
          </app-error-panel>
          <hr>

          <div class="w3-container assets-selector-div">
            <div class="w3-row-padding">
              <div class="w3-col m4">
                <h5><b>Create a new token?</b></h5>
              </div>
              <div class="w3-col m4">
                <button class="w3-button transfer-button"
                  :class="{'app-button': createTokenFlag, 'app-button-light': !createTokenFlag, }"
                  type="button" name="button"
                  @click="createTokenFlag = true">
                  yes
                </button>
              </div>
              <div class="w3-col m4">
                <button class="w3-button transfer-button"
                  :class="{'app-button': !createTokenFlag, 'app-button-light': createTokenFlag, }"
                  type="button" name="button"
                  @click="createTokenFlag = false">
                  no
                </button>
              </div>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="createTokenFlag" class="">
                  <div class="w3-row-padding">
                     <div class="w3-col m4">
                       <label class=""><b>Number of Tokens</b></label>
                       <input id="T_numberTokensInput" v-model.number="ownTokens.ownedByThisHolder" class="w3-input w3-border w3-hover-light-grey" type="number">
                     </div>
                     <div class="w3-col m4"  :style="{'margin-bottom': '15px'}">
                       <label class=""><b>Token Name</b></label>
                       <input id="T_tokenNameInput" v-model="ownTokens.assetName" class="w3-input w3-border w3-hover-light-grey" type="text">
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
                </div>
              </transition>
            </div>
          </div>

          <hr>

          <label class="init-contr-label"><b>Initial Members</b></label>
          <app-members-table-container
            :members="members"
            :canEdit="true">
          </app-members-table-container>
          <app-error-panel
            :show="membersEmptyShow"
            message="please select at least one member">
          </app-error-panel>
          <app-error-panel
            :show="noAdmingShow"
            message="there must be at least one admin">
          </app-error-panel>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button id="T_cancelButton" type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button id="T_acceptButton"  type="button" class="w3-button app-button" @click="accept()">Accept</button>
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
    driverErrorShow () {
      return this.driverEmptyError && (this.driver === '')
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

      if (this.driver === '') {
        ok = false
        this.driverEmptyError = true
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
