<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>{{ peerReview ? 'New Peer-Reviewed assignation' : 'New Direct Assignation' }}</h2>
        </div>

        <form class="w3-container">
          <div class="section-tabs w3-row">
            <div class="w3-half tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': !peerReview}"
              @click="peerReview = false">
              <h5 class="w3-text-indigo" :class="{'bold-text': !peerReview}">Direct</h5>
            </div>
            <div class="w3-half tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': peerReview}"
              @click="peerReview = true">
              <h5 class="w3-text-indigo" :class="{'bold-text': peerReview}">Peer Reviewed</h5>
            </div>
          </div>
          <br>

          <div class="w3-row">
            <div class="w3-col m8 assset-assigner">
              <app-initiative-assets-assigner
                v-if="initiative" :initiative="initiative" type='member-assigner'
                @updated="assetsSelected($event)">
              </app-initiative-assets-assigner>
            </div>
            <div class="w3-col m4">
              <div class="w3-left">
                <h5 class="w3-text-indigo w3-left"><b>Transfer to </b></h5>
              </div>
              <app-user-selector class="w3-left"
                anchor="c1Id" label="nickname"
                url="/1/secured/users/suggestions"
                @select="receiver = $event">
              </app-user-selector>
            </div>
          </div>

          <div class="w3-row">
            <div v-if="transferReady" class="w3-panel w3-theme w3-round-xlarge">
              <h5><b>Summary</b></h5>
              <p>
                {{ receiver.nickname }} will receive <span v-for="transfer in assetsTransfers">
                <span v-if="transfer.value ">
                  <b>{{ tokensString(transfer.value) }} {{ transfer.assetName }}</b> from {{ initiative.name }}
                </span>
              </span>
              </p>
            </div>
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
import InitiativeAssetsAssigner from './InitiativeAssetsAssigner.vue'
import UserSelector from '../user/UserSelector.vue'

/* eslint-disable no-unused-vars */
import { tokensString } from '../../lib/common'
/* eslint-enable no-unused-vars */

export default {

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner,
    'app-user-selector': UserSelector
  },

  props: {
    initiativeId: {
      type: String
    }
  },

  data () {
    return {
      peerReview: false,
      initiative: null,
      receiver: null,
      assetsTransfers: []
    }
  },

  computed: {
    transferReady () {
      if (this.receiver != null) {
        if (this.assetsTransfers.length > 0) {
          for (var ix in this.assetsTransfers) {
            if (this.assetsTransfers[ix].value > 0) {
              return true
            } else {
              return false
            }
          }
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
    closeThis () {
      this.$emit('close-this')
    },
    assetsSelected (assetsTransfers) {
      this.assetsTransfers = JSON.parse(JSON.stringify(assetsTransfers))
    },
    accept () {
      if (this.transferReady) {
        /* complete the transfer information */
        for (var ix in this.assetsTransfers) {
          this.assetsTransfers[ix].receiverId = this.receiver.c1Id
          this.assetsTransfers[ix].receiverName = this.receiver.nickname
        }
        this.axios.post('/1/secured/initiative/' + this.initiative.id + '/transferAssets', this.assetsTransfers)
        .then((response) => {

        })
      } else {
        this.showOutputMessage('missing data')
      }
    },
    updateInitiative () {
      this.axios.get('/1/secured/initiative/' + this.initiativeId, {
        params: {
          addAssets: true
        }
      }).then((response) => {
        this.initiative = response.data.data
      })
    }
  },

  mounted () {
    this.updateInitiative()
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

.assset-assigner {
  margin-bottom: 10px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
