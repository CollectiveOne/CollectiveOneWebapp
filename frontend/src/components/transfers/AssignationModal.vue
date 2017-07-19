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

            <div class="w3-ro tags-row w3-center">
              <div class="w3-tag gray-1 w3-round noselect w3-small">
                <b>{{ assignation.type }}</b>
              </div>
              <div class="w3-tag gray-1 w3-round noselect w3-small">
                <b>{{ assignation.state }}</b>
              </div>
            </div>

            <div v-if="showStatus" class="w3-row w3-center">
              <b>{{ assignation.evaluationsPending + ' evaluations pending' }}, closes in {{ getTimeStrUntil(assignation.config.maxClosureDate) }}</b>

            </div>

          </div>
        </div>

        <div v-if="isDirect" class="w3-row">
        </div>

        <hr>
        <div class="w3-row-padding">
          <div v-if="isEvaluator && !isDone" class="w3-col l12 my-evaluation-div w3-margin-bottom">
            <div class="w3-row w3-center">
              <h5 class=""><b>My evaluation</b></h5>
            </div>
            <div class="slider-container">
              <transition name="slideDownUp">
                <div v-if="!disableEvaluations" class="w3-panel light-grey">
                  <p><b>Tip:</b> fill the values with numbers that <b>make sense relative to
                  each other</b> and then click the "autoscale" button bellow.</p>
                  <p>For example, evaluate three persons with 5, 10 and 12 and then click "autoscale".
                    This will convert your evaluations to 19%, 37%, and 44% to make sure they sum 100% while keeping
                    the relation among them the way you wanted. Dont worry about the decimal places.</p>
                </div>
              </transition>
            </div>

            <br>
            <transition name="fade" mode="out-in">
              <app-users-percentages
                :usersData="evaluationReceivers"
                userAnchor="receiverUser"
                :disable="disableEvaluations"
                :key="disableEvaluations">
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
              :myEvaluations="evaluationReceivers">
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

      <div class="w3-display-topright">
        <router-link tag="div" :to="{ name: 'InitiativeAssignations' }"
          class="w3-button ">
          <i class="fa fa-times" aria-hidden="true"></i>
        </router-link>
      </div>

    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import ValueSeal from '@/components/transfers/ValueSeal.vue'
import UsersPercentages from '@/components/user/UsersPercentages.vue'
import PeerReviewedEvaluations from '@/components/transfers/PeerReviewedEvaluations.vue'
import { getTimeStrUntil, dateString } from '@/lib/common.js'

export default {

  components: {
    'app-user-avatar': UserAvatar,
    'app-value-seal': ValueSeal,
    'app-users-percentages': UsersPercentages,
    'app-peer-reviewed-evaluations': PeerReviewedEvaluations
  },

  data () {
    return {
      assignation: null,
      updateEvaluation: false
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
    isOpen () {
      return (this.assignation.state === 'OPEN')
    },
    isDone () {
      return (this.assignation.state === 'DONE')
    },
    showResults () {
      if (this.isDirect) {
        if (this.assignation.receivers.lenth > 1) {
          return true
        }
      }

      if (this.isPeerReviewed) {
        return this.isDone
      }
    },
    showStatus () {
      return this.isPeerReviewed && this.isOpen
    },
    isEvaluator () {
      return this.assignation.thisEvaluation !== null
    },
    evaluationReceivers () {
      if (this.assignation.thisEvaluation) {
        return this.assignation.thisEvaluation.evaluationGrades
      } else {
        return []
      }
    },
    showEvaluations () {
      return this.isDone && this.isEvaluator && this.assignation.config.evaluationsVisible
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
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    getTimeStrUntil (v) {
      return getTimeStrUntil(v)
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
      this.axios.get('/1/secured/assignation/' + this.assignationId, {
        params: {
          addAllEvaluations: true
        }
      }).then((response) => {
        this.assignation = response.data.data
        this.$store.dispatch('refreshTransfers')
      })
    },
    sendEvaluation () {
      if (this.arePercentagesOk) {
        this.axios.post('/1/secured/assignation/' + this.assignation.id + '/evaluate', this.assignation.thisEvaluation)
        .then((response) => {
          this.updateEvaluation = false
          this.updateAssignationData()
          this.evaluationSavedMessage = true
        })
      }
    }
  },

  mounted () {
    this.updateAssignationData()
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
  font-size: 20px;
}

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
  width: 110px;;
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

.bottom-btns-row {
  margin-top: 20px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
  max-width: 300px;
}

</style>
