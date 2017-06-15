<template lang="html">
  <div v-if="assignation" class="w3-card-2">
    <header class="w3-container w3-theme-l2">
      <h4>{{ assignation.motive }}</h4>
    </header>

    <div class="w3-row-padding w3-padding">
      <div class="w3-col m4 w3-center">
        <div class="w3-container">
          <div class="w3-row" v-for="bill in assignation.assets">
            <div class="w3-display-container bill-container">
              <i class="w3-display-middle fa fa-certificate l3-color" aria-hidden="true"></i>
              <div class="w3-display-middle d2-color" style="width: 100%">
                <div class="w3-row">
                  <b class="w3-xlarge ">{{ tokensString(bill.value) }} {{ bill.assetName }}</b>
                </div>
                <div class="w3-row">
                  <b class="">(10% of {{ initiative.name }})</b>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="w3-col m8 status-col">
        <div class="w3-row">
          <div class="w3-tag w3-theme-l2 w3-round">
            <b>{{ assignation.type }}</b>
          </div>
        </div>
        <div class="w3-row">
          <div class="w3-tag w3-theme-l2 w3-round">
            <b>{{ assignation.state }}</b>
          </div>
        </div>
        <div class="w3-row">
          <div class="w3-tag w3-theme-l2 w3-round">
            CLOSES IN <b>8 DAYS</b>
          </div>
        </div>
      </div>
    </div>

    <div class="w3-row-padding w3-padding">
      <div @click="expand()" class="w3-tag w3-theme w3-round w3-button more-btn">
        {{ nextAction }}
      </div>
    </div>
    <div v-show="showExpanded" class="w3-container">
      <hr>
      <label class="w3-text-indigo"><b>Receivers:</b></label>

      <div v-for="grade in grades" class="w3-row">
        <div class="w3-col s6">
          <app-user-avatar :user="grade.receiverUser"></app-user-avatar>
        </div>
        <div class="w3-col s3 w3-center">
          <div class="w3-row w3-padding input-div">
            <div class="w3-col s10">
              <input v-model.number="grade.percent" class="w3-input w3-border w3-hover-light-gray w3-round"
              type="number" step="5" min="0" :disabled="isDontKnow(grade)">
            </div>
            <div class="w3-col s2 d2-color">
              <i class="fa fa-percent" aria-hidden="true"></i>
            </div>
          </div>
        </div>
        <div class="w3-col s3 w3-center not-sure-col">
          <button class="w3-button w3-theme w3-round" @click="toggleDontKnow(grade)">{{ isDontKnow(grade) ? 'set' : 'dont know' }}</button>
        </div>
      </div>

      <div class="w3-panel w3-round w3-padding w3-center" :class="{'w3-theme': !arePercentagesOk, 'w3-green' : arePercentagesOk}">
        <span v-if="!arePercentagesOk">The sum of all percentages being set must be 100%, <b>please  {{ missingPercent < 0 ? 'remove ' + Math.abs(missingPercent) + '%' : 'add ' + Math.abs(missingPercent) + '%' }}</b></span>
        <span v-else>Well done, the current assignation is valid!</span>
      </div>

      <hr>
      <div class="bottom-btns-row w3-row-padding">
        <div class="w3-col m6">
          <button type="button" class="w3-button w3-light-gray w3-round" @click="cancel()">Cancel</button>
        </div>
        <div class="w3-col m6">
          <button type="button" class="w3-button w3-theme w3-round" @click="send()" :disabled="!arePercentagesOk">{{ isNotEvaluated ? 'Send' : 'Update' }}</button>
        </div>
      </div>

    </div>

  </div>
</template>

<script>
import UserAvatar from '../user/UserAvatar.vue'

import { tokensString } from '@/lib/common'

export default {
  components: {
    'app-user-avatar': UserAvatar
  },

  props: {
    initiative: {
      type: Object
    },
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
      if (this.isNotEvaluated) {
        return 'evaluate'
      }

      if (this.isEvaluated) {
        return 'review my evaluation'
      }
    },
    isNotEvaluated () {
      return this.assignation.thisEvaluation.evaluationState === 'PENDING'
    },
    isEvaluated () {
      return this.assignation.thisEvaluation.evaluationState === 'DONE'
    },
    grades () {
      return this.assignation.thisEvaluation.evaluationGrades
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
    tokensString (v) {
      return tokensString(v)
    },
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
    expand () {
      this.showExpanded = true
    },
    cancel () {
      this.showExpanded = false
    },
    send () {
      this.axios.post('/1/secured/initiative/' + this.initiative.id + '/assignation/' + this.assignation.id + '/evaluate', this.assignation.thisEvaluation)
      .then((response) => {
        this.showExpanded = false
        this.$emit('please-update')
      })
    }
  }
}
</script>

<style scoped>

.bill-container {
  height: 100px;
}

.fa-certificate {
  font-size: 100px;
}

.status-col {
  padding-top: 8px;
}

.status-col .w3-tag {
  width: 100%;
  margin-top: 5px;
}

.more-btn {
  width: 100%;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.not-sure-col {
  padding-top: 8px;
}

.bottom-btns-row {
  margin-top: 10px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
}


</style>
