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

      <div v-for="receiver in receivers" class="w3-row">
        <div class="w3-col s8">
          <app-user-avatar :user="receiver.user"></app-user-avatar>
        </div>
        <div class="w3-col s4 w3-center">
          <div class="w3-row w3-padding input-div">
            <div class="w3-col s10">
              <input v-model.number="receiver.percent" class="w3-input w3-border w3-hover-light-gray w3-round" type="number" step="5">
            </div>
            <div class="w3-col s2 d2-color">
              <i class="fa fa-percent" aria-hidden="true"></i>
            </div>
          </div>
        </div>
      </div>

      <div v-if="!percentagesOk" class="w3-panel w3-theme w3-round w3-padding">
        Percentages should sum 100%, please  {{ missingPercent < 0 ? 'remove ' + Math.abs(missingPercent) + '%' : 'add ' + Math.abs(missingPercent) + '%' }}
      </div>

      <hr>
      <div class="bottom-btns-row w3-row-padding">
        <div class="w3-col m6">
          <button type="button" class="w3-button w3-light-gray w3-round" @click="cancel()">Cancel</button>
        </div>
        <div class="w3-col m6">
          <button type="button" class="w3-button w3-theme w3-round" @click="send()">{{ isEvaluatorNotEvaluated ? 'Send' : 'Update' }}</button>
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
      if (this.isEvaluatorNotEvaluated) {
        return 'evaluate'
      }

      if (this.isEvaluatorEvaluated) {
        return 'review my evaluation'
      }
    },
    isEvaluatorNotEvaluated () {
      return this.assignation.thisEvaluation.evaluationState === 'OPEN'
    },
    isEvaluatorEvaluated () {
      return this.assignation.thisEvaluation.evaluationState === 'DONE'
    },
    receivers () {
      if (this.isEvaluatorNotEvaluated) {
        return this.assignation.receivers
      }
      if (this.isEvaluatorEvaluated) {
        return this.assignation.thisEvaluation.receivers
      }
    },
    percentagesOk () {
      if (this.missingPercent !== 0) {
        return false
      } else {
        return true
      }
    },
    missingPercent () {
      var sum = 0.0
      for (var ix in this.receivers) {
        sum += this.receivers[ix].percent
      }
      return (100 - sum)
    }
  },

  methods: {
    tokensString (v) {
      return tokensString(v)
    },

    expand () {
      this.showExpanded = true
    },

    cancel () {
      this.showExpanded = false
    },

    send () {
      var evaluation = {}
      evaluation.receivers = this.receivers
      this.axios.post('/1/secured/initiative/' + this.initiative.id + '/assignation/' + this.assignation.id + '/evaluate', evaluation)
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

.bottom-btns-row {
  margin-top: 10px;
  margin-bottom: 20px;
}

.bottom-btns-row button {
  width: 100%;
}


</style>
