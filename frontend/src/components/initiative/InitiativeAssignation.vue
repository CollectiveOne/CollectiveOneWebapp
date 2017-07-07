<template lang="html">
  <div v-if="assignation" class="w3-card-2 assignation-container">
    <div class="w3-row top-container">
      <div class="w3-col l4 seal-container">
        <app-value-seal :value="assignation.assets[0].value" :assetName="assignation.assets[0].assetName"></app-value-seal>
      </div>

      <div class="w3-col l8 w3-container">

        <div class="w3-row from-row">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>from:</b></label> {{ assignation.initiativeName }}
          </div>
        </div>
        <div class="w3-row receivers-row">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>to:</b></label>
          </div>
          <div class="w3-left receivers-container" v-for="receiver in assignation.receivers" :key="receiver.id" >
            <app-user-avatar :user="receiver.user"
              class="w3-left"
              :small="true" :showName="false">
            </app-user-avatar>
          </div>
        </div>
        <div class="w3-row motive-container">
          <div class="w3-left d2-color data-label">
            <label class="noselect"><b>motive:</b></label> {{ assignation.motive }}
          </div>
        </div>

        <div class="w3-row-padding tags-row">
          <div class="w3-col s6">
            <div class="w3-tag w3-theme-l2 w3-round noselect w3-small">
              <b>{{ assignation.type }}</b>
            </div>
          </div>
          <div class="w3-col s6">
            <div class="w3-tag w3-theme-l2 w3-round noselect w3-small">
              <b>{{ assignation.state }}</b>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="w3-row-padding w3-center">
      <router-link :to="{ name: 'InitiativeAssignation', params: { assignationId: assignation.id } }" class="w3-tag w3-round w3-button w3-theme action-btn">
        more
      </router-link>
    </div>

  </div>
</template>

<script>
import UserAvatar from '../user/UserAvatar.vue'
import ValueSeal from './ValueSeal.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar,
    'app-value-seal': ValueSeal
  },

  props: {
    assignation: {
      type: Object
    }
  },

  data () {
    return {
      showExpanded: false
    }
  },

  computed: {
    nextAction () {
      if (this.isPeerReviewed) {
        if (this.isNotEvaluated) {
          return 'evaluate'
        }

        if (this.isEvaluated) {
          return 'review my evaluation'
        }

        if (this.isDone) {
          return 'see receivers'
        }
      }

      if (this.isDirect) {
        if (this.isDone) {
          return 'see receivers'
        }
      }

      return 'options'
    },
    isDirect () {
      return (this.assignation.type === 'DIRECT')
    },
    isPeerReviewed () {
      return (this.assignation.type === 'PEER_REVIEWED')
    },
    isNotEvaluated () {
      return ((this.assignation.state === 'OPEN') && (this.assignation.thisEvaluation.evaluationState === 'PENDING'))
    },
    isEvaluated () {
      return ((this.assignation.state === 'OPEN') && (this.assignation.thisEvaluation.evaluationState === 'DONE'))
    },
    isOpen () {
      return this.assignation.state === 'OPEN'
    },
    isDone () {
      return this.assignation.state === 'DONE'
    },
    grades () {
      if (this.isOpen) {
        return this.assignation.thisEvaluation.evaluationGrades
      }
      if (this.isClosed) {
        return this.assignation.receivers
      }
    },
    missingPercent () {
      var sum = 0.0
      for (var ix in this.grades) {
        var grade = this.grades[ix]
        if (!this.isDontKnow(grade)) {
          sum += grade.percent
        }
      }
      return (100 - sum)
    },
    arePercentagesOk () {
      if (this.missingPercent === 0) {
        return true
      } else {
        return false
      }
    }
  },

  methods: {
    isDontKnow (grade) {
      if (grade.type) {
        if (grade.type === 'SET') {
          return false
        }
        if (grade.type === 'DONT_KNOW') {
          return true
        }
      } else {
        return false
      }
    },
    toggleDontKnow (grade) {
      if (this.isDontKnow(grade)) {
        grade.type = 'SET'
      } else {
        grade.percent = 0
        grade.type = 'DONT_KNOW'
      }
    },
    send () {
      this.axios.post('/1/secured/assignation/' + this.assignation.id + '/evaluate', this.assignation.thisEvaluation)
      .then((response) => {
        this.showExpanded = false
        this.$emit('please-update')
      })
    }
  }
}
</script>

<style scoped>

.assignation-container {
  padding-top: 10px;
  padding-bottom: 10px;
}

.top-container {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.seal-container {
  flex: 1 1 auto;
}

.data-label {
  padding-top: 8px;
}

.from-row {
  margin-bottom: 5px;
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

.tags-row .w3-tag {
  width: 100%;
  margin-top: 5px;
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
  margin-top: 10px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
}


</style>
