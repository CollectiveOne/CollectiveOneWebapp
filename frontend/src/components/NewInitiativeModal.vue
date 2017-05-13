<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="w3-container w3-theme">
          <h2>New Initiative</h2>
        </div>

        <form class="w3-container">

          <div v-if="asSubinitiative">
            <div class="w3-row">
              <div class="w3-col m9">
                <label class="w3-text-indigo"><b>Create as Sub-Initiative of</b></label>
                <app-initiative-selector></app-initiative-selector>
              </div>
              <div class="w3-col m3">
                <button type="button" class="cancel-btn w3-button w3-teal w3-round" @click="asSubinitiative = false">Remove</button>
              </div>
            </div>
          </div>
          <button v-else type="button" class="new-parent-btn w3-button w3-teal w3-round" @click="asSubinitiative = true">Set Parent (optional)</button>
          <br>

          <label class="w3-text-indigo"><b>Name</b></label>
          <input v-model="name" class="w3-input w3-hover-light-gray" type="text">
          <br>

          <label class="w3-text-indigo"><b>Driver</b></label>
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

          <div class="w3-row-padding">
            <div class="w3-col m3">
              <label class="w3-text-indigo"><b>Number of Tokens</b></label>
              <input v-model="nTokens" class="w3-input w3-border w3-hover-light-gray" type="number">
            </div>
            <div class="w3-col m4">
              <label class="w3-text-indigo"><b>Token Name</b></label>
              <input v-model="tokenName" class="w3-input w3-border w3-hover-light-gray" type="text">
            </div>
          </div>

          <hr>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-light-gray w3-round" @click="cancel()">Cancel</button>
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
import { mapMutations, mapActions } from 'vuex'
import InitiativeSelector from '@/components/InitiativeSelector.vue'
import UserSelector from '@/components/UserSelector.vue'
import UserAvatar from '@/components/UserAvatar.vue'

export default {
  components: {
    AppInitiativeSelector: InitiativeSelector,
    AppUserSelector: UserSelector,
    AppUserAvatar: UserAvatar
  },

  data () {
    return {
      asSubinitiative: false,
      parentInitiative: null,
      name: '',
      driver: '',
      contributors: [],
      nTokens: 1000,
      tokenName: 'tokens'
    }
  },

  methods: {
    ...mapMutations(['showNewInitiativeModal']),
    ...mapActions(['showOutputMessage', 'updateUserInitiatives']),

    cancel () {
      this.showNewInitiativeModal(false)
    },

    accept () {
      let intitiatveDto = {
        parentInitiative: this.parentInitiative,
        name: this.name,
        driver: this.driver,
        contributors: this.contributors,
        nTokens: this.ntokens,
        tokenName: this.tokenName
      }

      this.axios.post('/1/secured/initiative', intitiatveDto).then((response) => {
        if (response.status === 200) {
          if (response.data.result === 'success') {
            this.showOutputMessage(response.data.message)
            this.updateUserInitiatives()
            this.showNewInitiativeModal(false)
          } else {
            this.showOutputMessage(response.data.message)
          }
        } else {
          response.message
        }
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  created () {
    var loggedUser = this.$store.state.user.profile
    loggedUser.role = 'admin'
    if (loggedUser) {
      this.contributors.push(loggedUser)
    }
  }
}
</script>

<style scoped>

form {
  padding-top: 35px;
  padding-bottom: 35px;
}

.new-parent-btn {
  margin-bottom: 15px;
}

.contributors-container {
  margin-top: 10px;
  padding-bottom: 20px !important;
}

.new-contr-row {
  margin-top: 20px;
}

.cancel-btn {
  margin-top: 23px;
  float: right;
}

.add-btn {
  width: 100%;
}

.remove-btn {
  margin-top: 10px;
  width: 100%;
}

.bottom-btns-row button {
  width: 100%;
}

.w3-modal {
  display: block;
}

</style>
