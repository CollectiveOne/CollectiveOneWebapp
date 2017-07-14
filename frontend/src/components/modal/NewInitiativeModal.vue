<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>New Initiative</h2>
        </div>

        <form class="w3-container">

          <br>

          <label class="d2-color"><b>Name</b></label>
          <input v-model="name" class="w3-input w3-hover-light-gray" type="text">
          <br>

          <label class="d2-color"><b>Driver</b></label>
          <textarea v-model="driver" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>

          <hr>

          <div class="w3-container assets-selector-div">
            <app-new-token @updated="ownTokensSelected($event)"></app-new-token>
          </div>

          <div v-if="assetsSelected" class="w3-panel w3-theme">
            <h5><b>Summary</b></h5>
            <p>A token named <b>{{ ownTokens.assetName }}</b> will be created and this initiative will have <b>{{ tokensString(ownTokens.ownedByThisHolder) }} units</b></p>
          </div>
          <hr>

          <label class="init-contr-label d2-color"><b>Initial Members</b></label>
          <div class="w3-border w3-round w3-padding member-container">
            <app-initiative-member
              v-for="member in members"
              :key="member.user.c1Id"
              :member="member"
              :canEdit="true"
              @remove="removeMember($event)">
            </app-initiative-member>
            <div class="w3-row" :style="{'margin-bottom': '5px'}">
              <label class="d2-color" :style="{'margin-bottom': '10px'}"><b>add member:</b></label>
            </div>
            <app-initiative-new-member class="new-contr-row" @add="addMember($event)"></app-initiative-new-member>

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
import { mapActions } from 'vuex'
import NewToken from './support/NewToken.vue'
import InitiativeNewMember from '@/components/user/InitiativeNewMember.vue'
import InitiativeMember from '@/components/user/InitiativeMember.vue'

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
    'app-initiative-member': InitiativeMember,
    'app-initiative-new-member': InitiativeNewMember
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
      otherAssetsTransfers: [],
      members: []

    }
  },

  computed: {
    assetsSelected () {
      if (this.asSubinitiative) {
        if (this.otherAssetsTransfers) {
          if (this.otherAssetsTransfers.length > 0) {
            for (var ix in this.otherAssetsTransfers) {
              if (this.otherAssetsTransfers[ix].value > 0) {
                return true
              } else {
                return false
              }
            }
            return true
          }
        }
      } else {
        if (this.ownTokens.ownedByThisHolder > 0) {
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

    addMember (member) {
      var index = this.indexOfMember(member.user.c1Id)
      if (index === -1) {
        this.members.push(JSON.parse(JSON.stringify(member)))
      } else {
        this.showOutputMessage('user has been already included')
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
      this.$store.commit('showNewInitiativeModal', false)
    },

    accept () {
      let intitiatveDto = {
        asSubinitiative: this.asSubinitiative,
        parentInitiativeId: this.asSubinitiative ? this.parentInitiative.id : null,
        name: this.name,
        driver: this.driver,
        members: this.members,
        ownTokens: this.ownTokens,
        otherAssetsTransfers: this.otherAssetsTransfers
      }

      this.axios.post('/1/secured/initiative', intitiatveDto).then((response) => {
        if (response.data.result === 'success') {
          this.showOutputMessage(response.data.message)
          this.closeThis()
          this.$store.dispatch('refreshInitiative')
        } else {
          this.showOutputMessage(response.data.message)
        }
      }).catch((error) => {
        console.log(error)
      })
    },

    updateParent () {
      if (this.parentInitId !== '') {
        this.axios.get('/1/secured/initiative/' + this.parentInitId, {
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

.section-tabs {
  text-align: center;
  user-select: none;
  cursor: pointer;
}

.bold-text {
  font-weight: bold;
}

.assets-selector-div {
}

.members-container {
  margin-top: 10px;
  padding-bottom: 20px !important;
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
