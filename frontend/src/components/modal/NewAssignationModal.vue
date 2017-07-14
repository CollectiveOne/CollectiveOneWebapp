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
              <h5 class="d2-color" :class="{'bold-text': isDirect}">Direct</h5>
            </div>
            <div class="w3-col s6 tablink w3-bottombar w3-hover-light-grey w3-padding"
              :class="{'w3-border-blue': isPeerReviewed}"
              @click="assignation.type = PEER_REVIEWED_ID()">
              <h5 class="d2-color" :class="{'bold-text': isPeerReviewed}">Peer Reviewed</h5>
            </div>
          </div>
          <br>

          <div class="slider-container">
            <transition name="slideDownUp" mode="out-in">
              <keep-alive>
                <component @updated="receiversUpdated($event)" :is="isDirect ? 'app-direct-assignation' : 'app-peer-reviewed-assignation'"></component>
              </keep-alive>
            </transition>
            <div v-if="notEnoughReceivers" class="w3-row w3-tag error-tag error-row w3-round">
              please add at least one receiver
            </div>
            <div v-if="notEnoughEvaluators" class="w3-row w3-tag error-tag error-row w3-round">
              please add at least one evaluator
            </div>
          </div>

          <div class="w3-row">
            <app-initiative-assets-assigner
              :initInitiativeId="initiative.id" type='member-assigner'
              @updated="assetsSelected($event)" :showError="assetsZeroShow">
            </app-initiative-assets-assigner>
            <div v-if="assetsZeroShow" class="w3-row w3-tag error-tag error-row w3-round">
              plase select the amount of tokens that will be transferred to these members
            </div>
          </div>

          <div class="w3-row">
            <label class="d2-color"><b>Motive</b></label>
            <input v-model="assignation.motive" class="w3-input w3-hover-light-gray" :class="{ 'error-input' : motiveErrorShow }" type="text">
            <div v-if="motiveEmptyShow" class="w3-row w3-tag error-tag error-row w3-round">
              please provide a motive for this transfer for future reference
            </div>
            <div v-if="motiveTooLarge" class="w3-row w3-tag error-tag error-row w3-round">
              motive too large, please use the notes for long annotations
            </div>
            <br>

            <label class="d2-color"><b>Notes</b></label>
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
      },
      motiveEmptyError: false,
      assetsZeroError: false,
      assetsAreZero: false,
      notEnoughReceivers: false,
      notEnoughEvaluators: false
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
    },
    motiveErrorShow () {
      return this.motiveEmptyShow || this.motiveTooLarge
    },
    motiveEmptyShow () {
      return this.motiveEmptyError && this.assignation.motive === ''
    },
    motiveTooLarge () {
      return this.assignation.motive.length > 55
    },
    assetsZeroShow () {
      return this.assetsZeroError && this.assetsAreZero
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
      if (this.assetsZeroShow) {
        this.areAssetsZero()
        if (this.assetsAreZero) {
          this.assetsZeroError = false
        }
      }
    },
    areAssetsZero () {
      var result = true
      if (this.assignation.assets) {
        for (var ix in this.assignation.assets) {
          if (this.assignation.assets[ix].value !== 0) {
            result = false
          }
        }
      }
      this.assetsAreZero = result
    },
    receiversUpdated (data) {
      if (this.notEnoughReceivers) {
        if (data.receivers.length > 0) {
          this.notEnoughReceivers = false
        }
      }
      if (this.notEnoughEvaluators) {
        if (data.evaluators.length > 0) {
          this.notEnoughEvaluators = false
        }
      }
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
      /* validation */
      var ok = true
      if (this.assignation.motive === '') {
        this.motiveEmptyError = true
        ok = false
      }

      if (this.motiveTooLarge) {
        ok = false
      }

      this.areAssetsZero()
      if (this.assetsAreZero) {
        this.assetsZeroError = true
        ok = false
      }

      if (this.isDirect) {
        if (!this.assignation.directReceivers) {
          this.notEnoughReceivers = true
          ok = false
        } else {
          if (this.assignation.directReceivers.length === 0) {
            this.notEnoughReceivers = true
            ok = false
          }
        }
      } else {
        if (!this.assignation.peerReviewReceivers) {
          this.notEnoughReceivers = true
          ok = false
        } else {
          if (this.assignation.peerReviewReceivers.length === 0) {
            this.notEnoughReceivers = true
            ok = false
          }
        }

        if (!this.assignation.peerReviewEvaluators) {
          this.notEnoughEvaluators = true
          ok = false
        } else {
          if (this.assignation.peerReviewEvaluators.length === 0) {
            this.notEnoughEvaluators = true
            ok = false
          }
        }
      }

      if (ok) {
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
          this.$store.dispatch('refreshTransfers')
          this.$store.commit('triggerUpdateAssets')
          this.closeThis()
        })
      }
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
