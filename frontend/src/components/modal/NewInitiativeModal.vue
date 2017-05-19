<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <div v-if="asSubinitiative">
            <h2>New subinitiative of {{ parentInitiative.name }}</h2>
          </div>
          <div v-else>
            <h2>New initiative</h2>
          </div>
        </div>

        <form class="w3-container">
          <div v-if="asSubinitiative">
            <!-- If as subinitiative, select the initiative and the amount of tokens or other assets
                 to be take from it -->
            <div class="w3-row sub-initiative-first-row">
              <div class="w3-col m12">
                <h4 class="w3-text-indigo">Create as subinitiative or
                  <button type="button" class="inline-btn w3-button w3-teal w3-round" @click="asSubinitiative = false">create new token</button>
                </h4>
                <app-initiative-selector class="initiative-selector"
                  anchor="id" label="name" :init="parentInitiative"
                  url="/1/secured/initiatives/search"
                  @select="parentInitiativeSelected($event)">
                </app-initiative-selector>
              </div>
            </div>
            <div class="w3-row assigner-div">
              <app-assets-assigner :assetsData="parentInitiative.ownTokens"></app-assets-assigner>
            </div>
          </div>
          <div v-else class="">
            <div class="w3-row">
              <h4 class="w3-text-indigo">Create a new token or
                <button type="button" class="inline-btn w3-button w3-teal w3-round" @click="asSubinitiative = true">set parent</button>
              </h4>
            </div>
            <div class="w3-row-padding new-token-inputs">
              <div class="w3-col m4">
                <label class="w3-text-indigo"><b>Number of Tokens</b></label>
                <input v-model="tokens" class="w3-input w3-border w3-hover-light-gray" type="number">
              </div>
              <div class="w3-col m4"  :style="{'margin-bottom': '15px'}">
                <label class="w3-text-indigo"><b>Token Name</b></label>
                <input v-model="tokenName" class="w3-input w3-border w3-hover-light-gray" type="text">
              </div>
            </div>
          </div>
          <br>

          <label class="w3-text-indigo"><b>Initiative Name</b></label>
          <input v-model="name" class="w3-input w3-hover-light-gray" type="text">
          <br>

          <label class="w3-text-indigo"><b>Initiative Driver</b></label>
          <textarea v-model="driver" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>
          <br>

          <label class="init-contr-label w3-text-indigo"><b>Initial Contributors</b></label>
          <div class="w3-border w3-round w3-padding contributors-container">
            <div class="w3-row-padding" v-for="contributor in contributors" :key="contributor.auth0Id">
              <div class="w3-col m7">
                <app-user-avatar :userData="contributor"></app-user-avatar>
              </div>
              <div class="w3-col m2">
                <p><span class="w3-tag w3-large w3-round w3-theme">{{ contributor.role }}</span></p>
              </div>
              <div class="w3-col m3">
                <button type="button" class="remove-btn w3-button w3-teal w3-round" @click="removeContributor(contributor)">Remove</button>
              </div>
            </div>

            <div class="new-contr-row w3-row-padding">
              <div class="w3-col m7">
                <app-user-selector></app-user-selector>
              </div>
              <div class="w3-col m2">
                <input class="w3-input w3-border w3-hover-light-gray" type="text" placeholder="role">
              </div>
              <div class="w3-col m3">
                <button type="button" class="add-btn w3-button w3-teal w3-round" @click="addContributor()">Add</button>
              </div>
            </div>
          </div>
          <br>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-light-gray w3-round" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-teal w3-round" @click="accept()">Accept</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import InitiativeSelector from '../initiative/InitiativeSelector.vue'
import AssetsAssigner from '../initiative/AssetsAssigner.vue'
import UserSelector from '../user/UserSelector.vue'
import UserAvatar from '../user/UserAvatar.vue'

export default {
  props: {
    parentInitId: {
      type: String,
      default: ''
    }
  },

  components: {
    AppInitiativeSelector: InitiativeSelector,
    AppUserSelector: UserSelector,
    AppUserAvatar: UserAvatar,
    AppAssetsAssigner: AssetsAssigner
  },

  data () {
    return {
      asSubinitiative: false,
      parentInitiative: null,
      name: '',
      driver: '',
      contributors: [],
      tokens: 0,
      tokenName: 'tokens',
      percentage: 0
    }
  },

  methods: {
    ...mapActions(['showOutputMessage', 'updateUserInitiatives']),

    parentInitiativeSelected (initiative) {
      this.parentInitiative = initiative
      this.asSubinitiative = true

      this.axios.get('/1/secured/initiative/tokens').then((response) => {
        this.parentInitiativeTokensData = response.data.data
      })
    },

    closeThis () {
      this.$emit('close-this')
    },

    accept () {
      let intitiatveDto = {
        parentInitiativeId: this.asSubinitiative ? this.parentInitiative.id : null,
        name: this.name,
        driver: this.driver,
        contributors: this.contributors,
        tokenName: this.tokenName,
        tokens: this.tokens
      }

      this.axios.post('/1/secured/initiative', intitiatveDto).then((response) => {
        this.showOutputMessage(response.data.message)
        this.updateUserInitiatives()
        this.closeThis()
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    var loggedUser = this.$store.state.user.profile
    loggedUser.role = 'admin'
    if (loggedUser) {
      this.contributors.push(loggedUser)
    }

    if (this.parentInitId !== '') {
      this.axios.get('/1/secured/initiative/' + this.parentInitId, {
        params: {
          level: 'withAssets'
        }
      }).then((response) => {
        this.parentInitiative = response.data.data
        this.asSubinitiative = true
      })
    }
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
  padding-top: 15px;
  padding-bottom: 35px;
}

.inline-btn {
  height: 30px;
  padding: 0px 20px 0px 20px;
}

.initiative-selector {
  font-size: 20px;
}

.new-token-inputs {
  margin-top: 10px;
  text-align: center;
}

.new-token-inputs input {
  max-width: 150px;
  margin: 0 auto;
}

.sub-initiative-first-row {
  margin-bottom: 15px;
}

.sub-initiative-right-col {
  text-align: center;
  padding-top: 15px;
}

.contributors-container {
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

.w3-modal {
  display: block;
}

</style>
