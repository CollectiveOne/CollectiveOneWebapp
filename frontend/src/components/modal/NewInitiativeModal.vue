<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>New Initiative</h2>
        </div>

        <div class="w3-container form-container">

          <br>
          <label class=""><b>Name <span class="w3-small error-text">(required)</span></b></label>
          <input v-model="name"
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
          <textarea v-model="driver"
            class="w3-input w3-border w3-round w3-hover-light-grey"
            :class="{ 'error-input' : driverErrorShow }">
          </textarea>
          <app-error-panel
            :show="driverErrorShow"
            message="please include the purpose of this initiative">
          </app-error-panel>
          <hr>

          <div class="w3-container assets-selector-div">
            <app-new-token @updated="ownTokensSelected($event)"></app-new-token>
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
        assetName: 'token'
      },
      members: [],
      nameEmptyError: false,
      driverEmptyError: false,
      membersEmptyError: false,
      noAdminError: false
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
      return this.name.length > 30
    },
    driverErrorShow () {
      return this.driverEmptyError && (this.driver === '')
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

    ownTokensSelected (tokensData) {
      this.ownTokens.ownedByThisHolder = tokensData.tokens
      this.ownTokens.assetName = tokensData.tokenName
    },

    parentInitiativeUpdated (initiative) {
      this.parentInitiative = initiative
    },

    closeThis () {
      this.$store.commit('showNewInitiativeModal', false)
    },

    accept () {
      var ok = true

      if (this.name === '') {
        ok = false
        this.nameEmptyError = true
      } else {
        if (this.name.length > 30) {
          ok = false
        }
      }

      if (this.driver === '') {
        ok = false
        this.driverEmptyError = true
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
          ownTokens: this.ownTokens
        }

        this.axios.post('/1/initiative', initiativeDto).then((response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.dispatch('updatedMyInitiatives')
            this.$router.push({name: 'Initiative', params: {'initiativeId': response.data.elementId}})
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
          console.log(error)
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
