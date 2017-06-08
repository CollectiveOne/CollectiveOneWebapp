<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>{{ isPeerReviewed ? 'New Peer-Reviewed Assignation' : 'New Direct Assignation' }}</h2>
        </div>

        <div class="this-container w3-container">

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initiativeId="initiativeId" type='member-assigner'
              @updated="assetsSelected($event)">
            </app-initiative-assets-assigner>
          </div>

          <div class="section-tabs w3-row">
            <div class="w3-col s6 tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': isDirect}"
              @click="assignation.type = 'direct'">
              <h5 class="w3-text-indigo" :class="{'bold-text': isDirect}">Direct</h5>
            </div>
            <div class="w3-col s6 tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': isPeerReviewed}"
              @click="assignation.type = 'peerReviewed'">
              <h5 class="w3-text-indigo" :class="{'bold-text': isPeerReviewed}">Peer Reviewed</h5>
            </div>
          </div>
          <br>

          <div v-show="isDirect" id="direct-assignation-div" class="w3-row">
            <app-direct-assignation @updated="directReceiversSelected($event)"></app-direct-assignation>
          </div>
          <div v-show="isPeerReviewed" class="w3-row">
            <app-peer-reviewed-assignation @updated="peerReviewReceiversSelected($event)"></app-peer-reviewed-assignation>
          </div>

          <div class="w3-row">
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

        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import InitiativeAssetsAssigner from './InitiativeAssetsAssigner.vue'
import DirectAssignation from './DirectAssignation.vue'
import PeerReviewedAssignation from './PeerReviewedAssignation.vue'

export default {

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner,
    'app-direct-assignation': DirectAssignation,
    'app-peer-reviewed-assignation': PeerReviewedAssignation
  },

  props: {
    initiativeId: {
      type: String
    }
  },

  data () {
    return {
      peerReview: false,
      assignation: {
        type: 'direct'
      }
    }
  },

  computed: {
    isDirect () {
      return this.assignation.type === 'direct'
    },
    isPeerReviewed () {
      return this.assignation.type === 'peerReviewed'
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    closeThis () {
      this.$emit('close-this')
    },
    assignationUpdated (assignation) {
      this.assignation = assignation
    },
    assetsSelected (assets) {
      this.assignation.assets = assets
    },
    directReceiversSelected (data) {
      this.assignation.directReceivers = data.receivers
    },
    peerReviewReceiversSelected (data) {
      this.assignation.peerReviewReceivers = data.receivers
      this.assignation.peerReviewEvaluators = data.evaluators
    },
    accept () {
      var assignationToSend = {}
      assignationToSend.type = this.assignation.type
      assignationToSend.assets = this.assignation.assets
      assignationToSend.initiativeId = this.assignation.assets[0].senderId

      if (this.assignation.type === 'direct') {
        assignationToSend.receivers = this.assignation.directReceivers
      }

      this.axios.post('/1/secured/initiative/' + assignationToSend.initiativeId + '/assignation', assignationToSend)
      .then((response) => {
      })
    }
  }

}
</script>

<style scoped>

.w3-modal {
  display: block;
}

.this-container {
  padding-top: 10px;
  padding-bottom: 30px;
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
