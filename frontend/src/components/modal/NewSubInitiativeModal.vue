<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div v-if="parentInitiative" class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ $t('initiatives.NEW_SUBINITIATIVE_OF') }} {{ parentInitiative.meta.name }}</h2>
        </div>

        <div class="w3-container form-container">

          <br>
          <label class=""><b>{{ $t('general.NAME') }} <span class="w3-small error-text">(required)</span></b></label>
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

          <label class=""><b>{{ $t('general.DESCRIPTION') }}</b></label>
          <textarea v-model="driver"
            class="w3-input w3-border w3-round w3-hover-light-grey">
          </textarea>

          <hr>

          <div class="w3-container assets-selector-div">
            <div class="w3-row-padding">
              <div class="w3-col m4">
                <h5><b>{{ $t('tokens.TRANSFER_ASSETS_Q') }}</b></h5>
              </div>
              <div class="w3-col m4">
                <button class="w3-button transfer-button"
                  :class="{'app-button': transferAssetsFlag, 'app-button-light': !transferAssetsFlag, }"
                  type="button" name="button"
                  @click="transferAssetsFlag = true">
                  {{ $t('general.YES') }}
                </button>
              </div>
              <div class="w3-col m4">
                <button class="w3-button transfer-button"
                  :class="{'app-button': !transferAssetsFlag, 'app-button-light': transferAssetsFlag, }"
                  type="button" name="button"
                  @click="transferAssetsFlag = false">
                  {{ $t('general.NO') }}
                </button>
              </div>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <keep-alive>
                  <app-initiative-assets-assigner
                    v-if="transferAssetsFlag"
                    :initiativeId="parentInitiative.id" type='initiative-assigner'
                    :initiativeName="parentInitiative.meta.name" @updated="parentAssetsSelected($event)"
                    :showError="assetsErrorShow">
                  </app-initiative-assets-assigner>
                </keep-alive>
              </transition>
            </div>

          </div>
          <app-error-panel
            :show="assetsErrorShow"
            :message="$t('tokens.NO_ASSETS_TRANSFERRED_CONF')">
          </app-error-panel>

          <hr>

          <label class="init-contr-label "><b>{{ $t('initiatives.INITIAL_MEMBERS') }}</b></label>
          <app-members-table-container
            :members="members"
            :canEdit="true">
          </app-members-table-container>
          <div class="w3-row" :style="{'margin-bottom': '5px'}">
            <label class="" :style="{'margin-bottom': '10px'}"><b>or</b></label>
            <br>
            <button
              class="w3-button app-button w3-small w3-margin-left"
              @click="setAllParentMembers()" type="button" name="button">
              select all members of "{{ parentInitiative.meta.name }}"
            </button>
          </div>
          <app-error-panel
            :show="membersEmptyShow"
            :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
          </app-error-panel>
          <app-error-panel
            :show="noAdmingShow"
            :message="$t('initiatives.ONE_ADMIN_ATLEAST')">
          </app-error-panel>

          <hr>
          <div v-if="assetsErrorShow" class="w3-row error-panel w3-padding w3-round w3-margin-bottom">
            <div class="w3-col l10">
              {{ $t('tokens.NO_ASSETS_TRANSFERRED_MSG', { initName: parentInitiative.meta.name }) }}
            </div>
            <div class="w3-col l2 w3-center">
              <button
                class="w3-button app-button w3-round-large" name="button"
                @click="assetsEmptyErrorConfirmed = true">{{ $t('general.OK') }}</button>
            </div>
          </div>
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
import InitiativeAssetsAssigner from '@/components/transfers/InitiativeAssetsAssigner.vue'
import MembersTableContainer from '@/components/user/MembersTableContainer.vue'

/* eslint-disable no-unused-vars */
import { tokensString } from '../../lib/common'
/* eslint-enable no-unused-vars */

export default {

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner,
    'app-members-table-container': MembersTableContainer
  },

  props: {
    parentInitiative: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      name: '',
      driver: '',
      assetsTransfers: [],
      members: [],
      nameEmptyError: false,
      assetsEmptyError: false,
      membersEmptyError: false,
      noAdminError: false,
      assetsEmptyErrorConfirmed: false,
      transferAssetsFlag: true
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
    assetsErrorShow () {
      return this.assetsEmptyError && !this.assetsSelected && !this.assetsEmptyErrorConfirmed
    },
    membersEmptyShow () {
      return this.membersEmptyError && (this.members.length === 0)
    },
    noAdmingShow () {
      return this.noAdminError && !this.oneAdminExist
    },
    assetsSelected () {
      if (this.assetsTransfers) {
        if (this.assetsTransfers.length > 0) {
          for (var ix in this.assetsTransfers) {
            if (this.assetsTransfers[ix].value > 0) {
              return true
            }
          }
          return false
        }
      }
      return false
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

  methods: {
    ...mapActions(['showOutputMessage']),

    tokensString (v) {
      return tokensString(v)
    },

    parentAssetsSelected (assets) {
      this.assetsTransfers = JSON.parse(JSON.stringify(assets))
      this.updateParent()
    },

    setAllParentMembers () {
      this.parentInitiative.initiativeMembers.members.forEach((e) => {
        this.addMember(e, false)
      })
    },

    addMember (member, show) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index === -1) {
        this.members.push(JSON.parse(JSON.stringify(member)))
      } else {
        if (show) {
          this.showOutputMessage('user has been already included')
        }
      }
    },

    removeMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index > -1) {
        this.members.splice(index, 1)
      }
    },

    indexOfMember (c1Id) {
      for (var ix in this.members) {
        if (this.members[ix].user.c1Id === c1Id) {
          return ix
        }
      }
      return -1
    },

    closeThis () {
      this.$emit('close')
    },

    accept () {
      /* validation */

      var ok = true
      if (this.parentInitiative.id === '') {
        ok = false
      }

      if (this.name === '') {
        ok = false
        this.nameEmptyError = true
      } else {
        if (this.nameTooLong) {
          ok = false
        }
      }

      if (this.transferAssetsFlag) {
        if (!this.assetsSelected) {
          if (!this.assetsEmptyErrorConfirmed) {
            ok = false
            this.assetsEmptyError = true
          }
        }
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
        let intitiatveDto = {
          asSubinitiative: true,
          parentInitiativeId: this.parentInitiative.id,
          name: this.name,
          driver: this.driver,
          members: this.members,
          assetsTransfers: this.transferAssetsFlag ? this.assetsTransfers : []
        }

        this.axios.post('/1/initiative/create', intitiatveDto).then((response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.dispatch('updateMyInitiatives')
            this.$router.push({ name: 'Initiative', params: { initiativeId: response.data.elementId } })
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
          console.log(error)
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
  padding-top: 20px !important;
  padding-bottom: 20px !important;
}

.initiative-member-row {
  margin-bottom: 10px;
}

.new-contr-row {
  margin-top: 20px;
}

.fa-times-circle-o {
  color: #607d8b;
}

.add-btn {
  width: 100%;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
