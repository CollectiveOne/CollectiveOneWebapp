<template lang="html">
  <div class="w3-row-padding">
    <div class="receivers-col w3-col m6">
      <label class="w3-text-indigo"><b>Receivers:</b></label>
      <div class="w3-row">
        <app-users-list :users="receivers" @add-user="addReceiver($event)" @remove-user="removeReceiver($event)">
        </app-users-list>
      </div>
    </div>
    <div class="w3-col m6">
      <label class="w3-text-indigo"><b>Evaluators: {{ sameAsReceivers ? '(same as receivers)' : ''}}</b></label>
      <div class="w3-row" :class="{'covered-div': sameAsReceivers}">
        <app-users-list
          :users="actualEvaluators" @add-user="addEvaluator($event)" @remove-user="removeEvaluator($event)"
          :addUserEnabled="!sameAsReceivers">
        </app-users-list>
      </div>
      <br>
      <div class="w3-row w3-center">
        <button @click="sameAsReceivers = !sameAsReceivers" class="w3-button w3-theme w3-round">{{ sameAsReceivers ? 'different from receivers' : 'same as receivers' }}</button>
      </div>
    </div>
  </div>
</template>

<script>
import UsersList from '../user/UsersList.vue'

export default {
  components: {
    'app-users-list': UsersList
  },

  data () {
    return {
      receivers: [],
      evaluators: [],
      sameAsReceivers: true
    }
  },

  computed: {
    actualEvaluators () {
      if (this.sameAsReceivers) {
        return this.receivers
      } else {
        return this.evaluators
      }
    },
    selected () {
      return {
        receivers: this.receivers,
        evaluators: this.actualEvaluators
      }
    }
  },

  watch: {
    selected () {
      this.$emit('updated', this.selected)
    }
  },

  methods: {
    addReceiver (receiver) {
      if (this.indexOfReceiver(receiver) === -1) {
        this.receivers.push(receiver)
      }
    },
    addEvaluator (evaluator) {
      if (this.indexOfEvaluator(evaluator) === -1) {
        this.evaluators.push(evaluator)
      }
    },
    indexOfReceiver (receiver) {
      for (var ix in this.receivers) {
        if (this.receivers[ix].c1Id === receiver.c1Id) {
          return ix
        }
      }
      return -1
    },
    removeReceiver (receiver) {
      var ix = this.indexOfReceiver(receiver)
      if (ix !== -1) {
        this.receivers.splice(ix, 1)
      }
    },
    indexOfEvaluator (evaluator) {
      for (var ix in this.evaluators) {
        if (this.evaluators[ix].c1Id === evaluator.c1Id) {
          return ix
        }
      }
      return -1
    },
    removeEvaluator (evaluator) {
      var ix = this.indexOfEvaluator(evaluator)
      if (ix !== -1) {
        this.evaluators.splice(ix, 1)
      }
    }
  }
}
</script>

<style scoped>

.covered-div {
  position: relative;
}

.covered-div:before {
  content: " ";
  z-index: 10;
  display: block;
  position: absolute;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
}

.receivers-col {
  margin-bottom: 15px;
}

</style>
