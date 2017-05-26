<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>{{ asSubinitiative ? 'New subinitiative of ' + parentInitiative.name : 'New Initiative' }}</h2>
        </div>

        <form class="w3-container">

          <div class="token-type-tabs w3-row">
            <div class="w3-half tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': !asSubinitiative}"
              @click="asSubinitiative = false">
              <h5 class="w3-text-indigo" :class="{'bold-text': !asSubinitiative}">Create as independent</h5>
            </div>
            <div class="w3-half tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': asSubinitiative}"
              @click="asSubinitiative = true">
              <h5 class="w3-text-indigo" :class="{'bold-text': asSubinitiative}">Create as subinitiative</h5>
            </div>
          </div>
          <br>

          <label class="w3-text-indigo"><b>{{ asSubinitiative ? 'Subinitiative' : 'Initiative'}} Name</b></label>
          <input v-model="name" class="w3-input w3-hover-light-gray" type="text">
          <br>

          <label class="w3-text-indigo"><b>{{ asSubinitiative ? 'Subinitiative' : 'Initiative'}} Driver</b></label>
          <textarea v-model="driver" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>

          <hr>

          <div class="w3-container assets-selector-div">
            <keep-alive>
              <app-subinitiative-of v-if="asSubinitiative" :parentInitiative="parentInitiative"
                @updated="parentAssetsSelected($event)" @parent-initiative-updated="parentInitiativeUpdated($event)">
              </app-subinitiative-of>
            </keep-alive>
            <keep-alive>
              <app-new-token v-if="!asSubinitiative" @updated="ownTokensSelected($event)"></app-new-token>
            </keep-alive>
          </div>

          <div v-if="assetsSelected" class="w3-panel w3-theme">
            <h5><b>Summary</b></h5>
            <p v-if="asSubinitiative">
              This initiative will receive
              <span v-for="transfer in otherAssetsTransfers">
                <span v-if="transfer.value ">
                  <b>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</b> from {{ parentInitiative.name }}
                </span>
              </span>
            </p>
            <p v-else>A token named <b>{{ ownTokens.assetName }}</b> will be created and this initiative will have <b>{{ tokensString(ownTokens.ownedByThisHolder) }} units</b></p>
          </div>
          <hr>

          <label class="init-contr-label w3-text-indigo"><b>Initial Contributors</b></label>
          <div class="w3-border w3-round w3-padding contributors-container">
            <app-initiative-contributor
              v-for="contributor in contributors"
              :key="contributor.user.c1Id"
              :contributor="contributor"
              @remove="removeContributor($event)">
            </app-initiative-contributor>
            <div class="w3-row" :style="{'margin-bottom': '5px'}">
              <label class="w3-text-indigo" :style="{'margin-bottom': '10px'}"><b>add member:</b></label>
            </div>
            <app-initiative-new-contributor class="new-contr-row" @add="addContributor($event)"></app-initiative-new-contributor>

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
import SubinitiativeOf from './SubinitiativeOf.vue'
import NewToken from './NewToken.vue'
import InitiativeNewContributor from '../initiative/InitiativeNewContributor.vue'
import InitiativeContributor from '../initiative/InitiativeContributor.vue'

/* eslint-disable no-unused-vars */
import { tokensString } from '../../lib/common'
/* eslint-enable no-unused-vars */

export default {
  props: {
    parentInitId: {
      type: String,
      default: ''
    }
  },

  components: {
    'app-subinitiative-of': SubinitiativeOf,
    'app-new-token': NewToken,
    'app-initiative-contributor': InitiativeContributor,
    'app-initiative-new-contributor': InitiativeNewContributor
  },

  data () {
    return {
      asSubinitiative: false,
      parentInitiative: {
        name: ''
      },
      name: '',
      driver: '',
      ownTokens: {
        ownedByThisHolder: 0,
        assetName: 'token'
      },
      otherAssetsTransfers: [],
      contributors: []

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
      this.updateParent
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

    addContributor (contributor) {
      var index = this.indexOfContributor(contributor.user.c1Id)
      if (index === -1) {
        this.contributors.push(contributor)
      } else {
        this.showOutputMessage('user has been already included')
      }
    },

    removeContributor (contributor) {
      var index = this.indexOfContributor(contributor.user.c1Id)
      if (index > -1) {
        this.contributors.splice(index, 1)
      }
    },

    indexOfContributor (c1Id) {
      for (var ix in this.contributors) {
        if (this.contributors[ix].user.c1Id === c1Id) {
          return ix
        }
      }
      return -1
    },

    closeThis () {
      this.$emit('close-this')
    },

    accept () {
      let intitiatveDto = {
        asSubinitiative: this.asSubinitiative,
        parentInitiativeId: this.asSubinitiative ? this.parentInitiative.id : null,
        name: this.name,
        driver: this.driver,
        contributors: this.contributors,
        ownTokens: this.ownTokens,
        otherAssetsTransfers: this.otherAssetsTransfers
      }

      this.axios.post('/1/secured/initiative', intitiatveDto).then((response) => {
        if (response.data.result === 'success') {
          this.showOutputMessage(response.data.message)
          this.closeThis()
          this.$emit('initiative-created', response.data.elementId)
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
          this.asSubinitiative = true
        })
      } else {
        this.asSubinitiative = false
      }
    }
  },

  mounted () {
    if (this.$store.state.user.profile) {
      var contributor = {}
      contributor.user = this.$store.state.user.profile
      contributor.role = 'ADMIN'
      this.contributors.push(contributor)
    }

    this.updateParent()
  }
}
</script>

<style scoped>

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

.token-type-tabs {
  text-align: center;
  user-select: none;
  cursor: pointer;
}

.bold-text {
  font-weight: bold;
}

.assets-selector-div {
}

.contributors-container {
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

.w3-modal {
  display: block;
}

</style>
