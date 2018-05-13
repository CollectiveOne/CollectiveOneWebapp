<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div v-if="assignation" class="w3-card-2 assignation-container w3-display-container">

        <div class="w3-row">

          <div class="w3-col l4 seal-container">
            <div class="w3-row">
              <app-value-seal :value="assignation.assets[0].value" :assetName="assignation.assets[0].assetName"></app-value-seal>
            </div>
            <div class="w3-row from-row w3-center ">
              <div class="w3-tag w3-round noselect" :style="{'background-color': $store.getters.colorOfInitiative(assignation.initiativeId)}">
                from
                <b>{{ assignation.initiativeName }}</b>
              </div>
            </div>
            <div class="w3-row w3-center w3-margin-top">
              {{ dateString(assignation.creationDate) }}
            </div>
          </div>

          <div class="w3-col l8 w3-container ">

            <div class="w3-row w3-center">
              <h3>{{ assignation.motive }}</h3>
            </div>

            <div v-if="assignation.notes.length > 0" class="w3-row w3-center">
              <p>{{ assignation.notes }}</p>
            </div>

            <div class="w3-row w3-center">
              <div class="receivers-container">
                <div class="receiver-container"
                  v-for="receiver in assignation.receivers" :key="receiver.id" >
                  <app-user-avatar :user="receiver.user"
                    class="w3-left"
                    :small="true" :showName="false">
                  </app-user-avatar>
                </div>
              </div>
            </div>

            <div class="w3-row tags-row w3-center">
              <div class="w3-tag gray-1 w3-round noselect w3-small">
                <b>{{ assignation.type }}</b>
              </div>
              <div class="w3-tag w3-round noselect w3-small" :class="stateClass">
                <b>{{ assignation.state }}</b>
              </div>
            </div>

            <div v-if="$store.getters.isLoggedAnAdmin && isDone" class="w3-row tags-row w3-center">
              <button
                class="w3-button app-button"
                @click="revertTransaction = true">
                revert transaction
              </button>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="revertTransaction" class="w3-row tags-row w3-center">
                  <div class="w3-padding w3-round light-grey w3-margin-bottom">
                    <p>
                      <b>Warning:</b> This will order the transfer of tokens from the receivers
                      back to the initiative. Please confirm you would like to revert this transaction.
                    </p>
                  </div>

                  <button
                    class="w3-button app-button-light button-pair"
                    @click="revertTransaction = false">cancel
                  </button>
                  <button
                    class="w3-button app-button-danger button-pair"
                    @click="orderRevert()">confirm
                  </button>
                </div>
              </transition>
            </div>

            <div v-if="$store.getters.isLoggedAnAdmin && isOpen" class="w3-row tags-row w3-center">
              <button
                class="w3-button app-button"
                @click="deleteTransaction = true">
                delete
              </button>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="deleteTransaction" class="w3-row tags-row w3-center">
                  <div class="w3-padding w3-round light-grey w3-margin-bottom">
                    <p>
                      <b>Warning:</b> This will delete this assignation. All evaluations made
                      will be lost and no tokens will be transferred. Please confirm.
                    </p>
                  </div>

                  <button
                    class="w3-button app-button-light button-pair"
                    @click="deleteTransaction = false">cancel
                  </button>
                  <button
                    class="w3-button app-button-danger button-pair"
                    @click="deleteAssignation()">confirm
                  </button>
                </div>
              </transition>
            </div>

            <div v-if="$store.getters.isLoggedAnAdmin && isOnHold" class="w3-row tags-row w3-center">
              <button
                class="w3-button app-button"
                @click="openAssignation = true">
                open - start evaluations
              </button>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="openAssignation" class="w3-row tags-row w3-center">
                  <div class="w3-padding w3-round light-grey w3-margin-bottom">
                    <p>
                      <b>Warning:</b> This will open the peer-review process and evaluators will have
                      {{ assignation.config.maxDuration }} days to do provide their evaluations.
                    </p>
                  </div>

                  <button
                    class="w3-button app-button-light button-pair"
                    @click="openAssignation = false">cancel
                  </button>
                  <button
                    class="w3-button app-button-danger button-pair"
                    @click="openAssignationClick()">confirm
                  </button>
                </div>
              </transition>
            </div>

            <div v-if="isReceiverApproval" class="w3-center">
              <div class="w3-padding w3-round light-grey w3-margin-bottom">
                <p>
                  <b>Attention:</b> One of the admins would like to revert this
                  transation. Do you approve this revert?
                </p>
              </div>
              <div v-if="!this.loggedReceiver.revertApproval" class="">
                <button
                  class="w3-button app-button-light button-pair"
                  @click="approveRevert(false)">reject
                </button>
                <button
                  class="w3-button app-button button-pair"
                  @click="approveRevert(true)">approve
                </button>
              </div>
              <div v-else class="">
                <div class="w3-tag w3-padding app-button button-pair noselect">approved!
                </div>
              </div>
            </div>

            <div v-if="showStatus" class="w3-row w3-center">
              <b>{{ assignation.evaluationsPending + ' evaluations pending' }}, closes in {{ getTimeStrUntil(assignation.config.maxClosureDate) }}</b>
            </div>

          </div>
        </div>

        <div v-if="isDirect" class="w3-row">
          <div v-if="assignation.receivers.length > 1" class="">
            <app-users-percentages
              :usersData="assignation.receivers"
              :disable="true"
              :showSelfBiases="false"
              :showTotal="true"
              :totalTokens="assignation.assets[0].value">
            </app-users-percentages>
          </div>
        </div>

        <hr>
        <div class="w3-row-padding">
          <div v-if="isEvaluator && !isDone && !isDeleted && isOpen" class="w3-col l12 my-evaluation-div w3-margin-bottom">
            <div class="w3-row w3-center">
              <h5 class=""><b>My evaluation</b></h5>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="!disableEvaluations" class="w3-panel light-grey">
                  <p><b>Tip:</b> fill the values with numbers that make sense relative to
                  each other, <b>even if they dont sum 100%</b>, and then click the "autoscale" button bellow.</p>
                </div>
              </transition>
            </div>

            <br>
            <transition name="fade" mode="out-in">
              <app-users-percentages
                :usersData="evaluationReceivers"
                userAnchor="receiverUser"
                :disable="disableEvaluations"
                :key="disableEvaluations"
                :totalTokens="assignation.assets[0].value">
              </app-users-percentages>
            </transition>

            <div v-if="isOpen" class="bottom-btns-row w3-center">
              <button v-if="!disableEvaluations"
                type="button" class="w3-button app-button"
                @click="sendEvaluation()" :disabled="!arePercentagesOk">
                Save
              </button>
              <button v-else
                type="button" class="w3-button app-button"
                @click="updateEvaluation = true">
                Change my evaluation
              </button>
            </div>
          </div>
          <div v-if="showResults" class="w3-col l12">
            <div class="w3-row w3-center">
              <h5 class=""><b>Results</b></h5>
            </div>
            <br>
            <app-users-percentages
              :usersData="assignation.receivers"
              :disable="true"
              :showSelfBiases="assignation.config.selfBiasVisible"
              :myEvaluations="evaluationReceivers"
              :totalTokens="assignation.assets[0].value">
            </app-users-percentages>
          </div>
        </div>
        <div v-if="showEvaluations" class="w3-row">
          <hr>
          <div class="w3-row w3-center">
            <h5 class=""><b>All Evaluations</b></h5>
          </div>
          <br>
          <app-peer-reviewed-evaluations
            :evaluators="assignation.evaluators"
            :receivers="assignation.receivers">
          </app-peer-reviewed-evaluations>
        </div>
      </div>

      <div class="w3-display-topright w3-xlarge close-div" >
        <router-link tag="div" :to="{ name: 'InitiativeAssignations' }"
          class="">
          <i class="fa fa-times" aria-hidden="true"></i>
        </router-link>
      </div>

    </div>
  </div>
</template>

<script>
import ValueSeal from '@/components/transfers/ValueSeal.vue'
import UsersPercentages from '@/components/user/UsersPercentages.vue'
import PeerReviewedEvaluations from '@/components/transfers/PeerReviewedEvaluations.vue'
import { getTimeStrUntil, dateString } from '@/lib/common.js'

export default {

  components: {
    'app-value-seal': ValueSeal,
    'app-users-percentages': UsersPercentages,
    'app-peer-reviewed-evaluations': PeerReviewedEvaluations
  },

  data () {
    return {
      assignation: null,
      updateEvaluation: false,
      revertTransaction: false,
      deleteTransaction: false,
      openAssignation: false
    }
  },

  computed: {
    assignationId () {
      return this.$route.params.assignationId
    },
    isDirect () {
      return (this.assignation.type === 'DIRECT')
    },
    isPeerReviewed () {
      return (this.assignation.type === 'PEER_REVIEWED')
    },
    isOnHold () {
      return (this.assignation.state === 'ON_HOLD')
    },
    isOpen () {
      return (this.assignation.state === 'OPEN')
    },
    isDone () {
      return (this.assignation.state === 'DONE')
    },
    isRevertOrdered () {
      return (this.assignation.state === 'REVERT_ORDERED')
    },
    isReverted () {
      return (this.assignation.state === 'REVERTED')
    },
    isDeleted () {
      return (this.assignation.state === 'DELETED')
    },
    showResults () {
      if (this.isDirect) {
        if (this.assignation.receivers.lenth > 1 && !this.isDeleted) {
          return true
        }
      }

      if (this.isPeerReviewed && !this.isDeleted) {
        return this.isDone
      }

      return false
    },
    showStatus () {
      return this.isPeerReviewed && this.isOpen
    },
    isEvaluator () {
      return this.assignation.thisEvaluation !== null
    },
    loggedReceiver () {
      for (var ix in this.assignation.receivers) {
        if (this.assignation.receivers[ix].user.c1Id === this.$store.state.user.profile.c1Id) {
          return this.assignation.receivers[ix]
        }
      }
      return null
    },
    isReceiver () {
      if (this.loggedReceiver) {
        return true
      } else {
        return false
      }
    },
    evaluationReceivers () {
      if (this.assignation.thisEvaluation) {
        return this.assignation.thisEvaluation.evaluationGrades
      } else {
        return []
      }
    },
    showEvaluations () {
      return this.isDone && this.assignation.config.evaluationsVisible && !this.isDeleted
    },
    sumOfPercents () {
      var sum = 0.0
      for (var ix in this.assignation.thisEvaluation.evaluationGrades) {
        var thisUserData = this.assignation.thisEvaluation.evaluationGrades[ix]
        if (!this.isDontKnow(thisUserData)) {
          sum += thisUserData.percent
        }
      }
      return sum
    },
    arePercentagesOk () {
      if (Math.abs(this.sumOfPercents - 100.0) < 0.001) {
        return true
      } else {
        return false
      }
    },
    evaluationDone () {
      return this.assignation.thisEvaluation.evaluationState === 'DONE'
    },
    disableEvaluations () {
      return this.evaluationDone && !this.updateEvaluation
    },
    isReceiverApproval () {
      return this.isRevertOrdered && this.isReceiver
    },
    stateClass () {
      if (this.isOpen) {
        return {
          'open-color': true
        }
      }

      if (this.isDone) {
        return {
          'done-color': true
        }
      }

      if ((this.isRevertOrdered) || (this.isReverted) || (this.isDeleted)) {
        return {
          'revert-color': true
        }
      }

      return {
        'gray-1': true
      }
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    getTimeStrUntil (v) {
      return getTimeStrUntil(v)
    },
    openAssignationClick () {
      this.openAssignation = false
      this.axios.put('/1/assignation/' + this.assignationId + '/open').then((response) => {
        this.updateAssignationData()
      })
    },
    isDontKnow (userData) {
      if (userData.type) {
        if (userData.type === 'SET') {
          return false
        }
        if (userData.type === 'DONT_KNOW') {
          return true
        }
      } else {
        return false
      }
    },
    updateAssignationData () {
      this.axios.get('/1/assignation/' + this.assignationId, {
        params: {
          addAllEvaluations: true
        }
      }).then((response) => {
        this.assignation = response.data.data
        this.$store.commit('triggerUpdateAssets')
      })
    },
    sendEvaluation () {
      if (this.arePercentagesOk) {
        this.axios.post('/1/assignation/' + this.assignation.id + '/evaluate', this.assignation.thisEvaluation)
        .then((response) => {
          this.updateEvaluation = false
          this.updateAssignationData()
          this.evaluationSavedMessage = true
        })
      }
    },
    orderRevert () {
      this.revertTransaction = false
      this.axios.put('/1/assignation/' + this.assignation.id + '/revert', {})
      .then((response) => {
        this.updateAssignationData()
      })
    },
    approveRevert (val) {
      this.axios.put('/1/assignation/' + this.assignation.id + '/approveRevert', {}, {
        params: {
          approveFlag: val
        }
      }).then((response) => {
        this.updateAssignationData()
      })
    },
    deleteAssignation () {
      this.deleteTransaction = false
      this.axios.put('/1/assignation/' + this.assignation.id + '/delete', {})
      .then((response) => {
        this.updateAssignationData()
      })
    }
  },

  mounted () {
    this.updateAssignationData()
  }
}
</script>

<style scoped>

.assignation-container {
  padding-top: 20px;
  padding-bottom: 30px;
}

.top-right-div {
}

.seal-container {
}

.tags-row {
  margin-top: 5px;
}

.tags-row .w3-tag {
  min-width: 110px;;
  margin-top: 5px;
}

.motive-container {
  margin-bottom: 10px;
}

.tags-row {
  margin-bottom: 15px;
}

.receiver-container {
  margin-left: 5px;
  display: inline-block;
}

.button-pair {
  min-width: 120px;
}

.bottom-btns-row {
  margin-top: 20px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
  max-width: 300px;
}

</style>
