<template lang="html">
  <div class="w3-row-padding">
    <div class="w3-col m6">
      <label class="w3-text-indigo"><b>Receivers:</b></label>
      <div class="w3-row">
        <app-users-list :users="receivers" @add-user="addReceiver($event)" @remove-user="removeReceiver($event)">
        </app-users-list>
      </div>
    </div>
    <div class="w3-col m6">
      <label class="w3-text-indigo"><b>Evaluators:</b></label>
      <app-users-list :users="evaluators" @add-user="addEvaluator($event)" @remove-user="removeEvaluator($event)">
      </app-users-list>
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
      evaluators: []
    }
  },

  methods: {
    addReceiver (receiver) {
      this.receivers.push(receiver)
    },
    addEvaluator (evaluator) {
      this.evaluators.push(evaluator)
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

<style lang="css">
</style>
