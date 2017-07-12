<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">
        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <transition name="fadeenter" mode="out-in">
            <h2 :key="isPeerReviewed">{{ isPeerReviewed ? 'Peer-reviewed transfer to users' : 'Direct transfer to user(s)' }}</h2>
          </transition>
        </div>

        <div class="this-container w3-container">

          <div class="section-tabs w3-row">
            <div class="w3-col s6 tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': isDirect}"
              @click="assignation.type = DIRECT_ID()">
              <h5 class="w3-text-indigo" :class="{'bold-text': isDirect}">Direct</h5>
            </div>
            <div class="w3-col s6 tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': isPeerReviewed}"
              @click="assignation.type = PEER_REVIEWED_ID()">
              <h5 class="w3-text-indigo" :class="{'bold-text': isPeerReviewed}">Peer Reviewed</h5>
            </div>
          </div>
          <br>

          <div class="slider-container">
            <transition name="slideDownUp" mode="out-in">
              <keep-alive>
                <component @updated="receiversUpdated($event)" :is="isDirect ? 'app-direct-assignation' : 'app-peer-reviewed-assignation'"></component>
              </keep-alive>
            </transition>
          </div>

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initInitiativeId="initiative.id" type='member-assigner'
              @updated="assetsSelected($event)">
            </app-initiative-assets-assigner>
          </div>

          <div class="w3-row">
            <label class="w3-text-indigo"><b>Motive</b></label>
            <input v-model="assignation.motive" class="w3-input w3-hover-light-gray" type="text">
            <br>

            <label class="w3-text-indigo"><b>Notes</b></label>
            <textarea v-model="assignation.notes" class="w3-input w3-border w3-round w3-hover-light-gray"></textarea>
            <br>
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
import InitiativeAssetsAssigner from '@/components/transfers/InitiativeAssetsAssigner.vue'
import DirectAssignation from './support/DirectAssignation.vue'
import PeerReviewedAssignation from './support/PeerReviewedAssignation.vue'

export default {

  components: {
    'app-initiative-assets-assigner': InitiativeAssetsAssigner,
    'app-direct-assignation': DirectAssignation,
    'app-peer-reviewed-assignation': PeerReviewedAssignation
  },

  data () {
    return {
      assignation: {
        type: this.DIRECT_ID(),
        motive: '',
        notes: ''
      }
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    isDirect () {
      return this.assignation.type === this.DIRECT_ID()
    },
    isPeerReviewed () {
      return this.assignation.type === this.PEER_REVIEWED_ID()
    }
  },

  methods: {
    ...mapActions(['showOutputMessage']),

    DIRECT_ID () {
      return 'DIRECT'
    },

    PEER_REVIEWED_ID () {
      return 'PEER_REVIEWED'
    },

    closeThis () {
      this.$store.commit('showNewAssignationModal', false)
    },
    assignationUpdated (assignation) {
      this.assignation = assignation
    },
    assetsSelected (assets) {
      this.assignation.assets = assets
    },
    receiversUpdated (data) {
      if (this.isDirect) {
        this.directReceiversSelected(data)
      } else {
        this.peerReviewReceiversSelected(data)
      }
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
      assignationToSend.motive = this.assignation.motive
      assignationToSend.notes = this.assignation.notes

      if (this.isDirect) {
        assignationToSend.receivers = this.assignation.directReceivers
      } else {
        assignationToSend.receivers = this.assignation.peerReviewReceivers
        assignationToSend.evaluators = this.assignation.peerReviewEvaluators
      }

      this.axios.post('/1/secured/initiative/' + assignationToSend.initiativeId + '/assignation', assignationToSend)
      .then((response) => {
        this.$store.commit('triggerUpdateAssets')
        this.closeThis()
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
