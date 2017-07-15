<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div v-if="assignation" class="w3-card-2 assignation-container w3-display-container">

        <div class="w3-row">

          <div class="w3-col l4 seal-container">
            <div class="w3-row">
              <app-value-seal :value="assignation.assets[0].value" :assetName="assignation.assets[0].assetName"></app-value-seal>
            </div>
            <div class="w3-row from-row w3-center d2-color">
              <div class="w3-tag w3-theme w3-round noselect">
                <b>{{ assignation.initiativeName }}</b>
              </div>
            </div>
          </div>

          <div class="w3-col l8 w3-container">

            <div class="w3-row w3-center">
              <h3>{{ assignation.motive }}</h3>
            </div>

            <div v-if="assignation.notes.length > 0" class="w3-row w3-center">
              <p>{{ assignation.notes }}</p>
            </div>

            <div class="w3-ro tags-row w3-center">
              <div class="w3-tag w3-theme-l2 w3-round noselect w3-small">
                <b>{{ assignation.type }}</b>
              </div>
              <div class="w3-tag w3-theme-l2 w3-round noselect w3-small">
                <b>{{ assignation.state }}</b>
              </div>
            </div>

            <div class="w3-row w3-center">
              <div class="">
                <div class="receivers-container w3-left" v-for="receiver in assignation.receivers" :key="receiver.id" >
                  <app-user-avatar :user="receiver.user"
                    class="w3-left"
                    :small="true" :showName="false">
                  </app-user-avatar>
                </div>
              </div>
            </div>

          </div>
        </div>

        <div v-if="isDirect" class="w3-row">
        </div>

        <hr>
        <div class="w3-row">
          <div v-if="isEvaluator" class="w3-col my-evaluation-div" :class="{'l6': isDone, '12': !isDone}">
            <div class="w3-row w3-center">
              <h6 class="d2-color"><b>My evaluation</b></h6>
            </div>
            <br>
            <transition name="fade" mode="out-in">
              <app-users-percentages
                :usersDataInit="evaluationReceivers"
                userAnchor="receiverUser"
                :disable="disableEvaluations"
                :key="disableEvaluations">
              </app-users-percentages>
            </transition>
            <div v-if="isOpen" class="bottom-btns-row w3-center">
              <button v-if="!disableEvaluations"
                type="button" class="w3-button w3-theme w3-round"
                @click="sendEvaluation()" :disabled="!arePercentagesOk">
                Save
              </button>
              <button v-else
                type="button" class="w3-button w3-theme w3-round"
                @click="updateEvaluation = true">
                Change my evaluation
              </button>

              <div class="slider-container">
                <transition name="slideDownUp">
                  <div v-if="evaluationSavedMessage" class="w3-panel warning-panel w3-padding w3-center">
                    your evaluation has been saved
                  </div>
                </transition>
              </div>
            </div>
          </div>
          <div v-if="isDone" class="w3-col l6">
            <div class="w3-row w3-center">
              <h6 class="d2-color"><b>Results</b></h6>
            </div>
            <br>
            <app-users-percentages
              :usersDataInit="assignation.receivers"
              :disable="true"
              :showSelfBiases="assignation.selfBiasVisible">
            </app-users-percentages>
          </div>
        </div>
        <div v-if="showEvaluations" class="w3-row">
          <hr>
          <div class="w3-row w3-center">
            <h6 class="d2-color"><b>All Evaluations</b></h6>
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
          class="w3-button d2-color">
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
      updateEvaluation: false,
      evaluationSavedMessage: false
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
    isEvaluator () {
      return this.assignation.thisEvaluation !== null
    },
    evaluationReceivers () {
      return this.assignation.thisEvaluation.evaluationGrades
    },
    showEvaluations () {
      return this.isDone && this.isEvaluator && this.assignation.evaluationsVisible
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
      if (this.sumOfPercents === 100) {
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
      })
    },
    sendEvaluation () {
      if (this.arePercentagesOk) {
        this.axios.post('/1/secured/assignation/' + this.assignation.id + '/evaluate', this.assignation.thisEvaluation)
        .then((response) => {
          this.updateEvaluation = false
          this.updateAssignationData()
          this.evaluationSavedMessage = true
          setTimeout(() => {
            this.evaluationSavedMessage = false
          }, 2000)
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
  margin-bottom: 5px;
}

.tags-row .w3-tag {
  width: 110px;;
  margin-top: 5px;
}

.from-row {
}

.receivers-row {
  margin-bottom: 5px;
}

.receivers-container {
  margin-left: 15px;
}

.motive-container {
  margin-bottom: 10px;
}

.fa-certificate {
  font-size: 80px;
}



.action-btn {
  width: 100%;
  max-width: 220px;
  padding-top: 2px !important;
  padding-bottom: 2px !important;
}

.percent-input {
  max-width: 100px;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.not-sure-col {
  margin-left: 20px;
}

.not-sure-col button {
  width: 120px;
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
