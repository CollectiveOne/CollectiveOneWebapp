<template lang="html">
  <div class="">
    <div class="w3-row">
      <div class="receivers-col w3-col m6">
        <label class="w3-text-indigo"><b>Receivers:</b></label>
        <div class="w3-row">
          <app-users-list :users="receiversUsers" @add-user="addReceiver($event)" @remove-user="removeReceiver($event)">
          </app-users-list>
        </div>
      </div>
      <div class="w3-col m6">
        <label class="w3-text-indigo"><b>Evaluators: {{ sameAsReceivers ? '(same as receivers)' : ''}}</b></label>
        <div class="w3-row" :class="{'covered-div': sameAsReceivers}">
          <app-users-list
            :users="actualEvaluatorsUsers" @add-user="addEvaluator($event)" @remove-user="removeEvaluator($event)"
            :addUserEnabled="!sameAsReceivers">
          </app-users-list>
        </div>
        <br>
        <div class="w3-row w3-center">
          <button @click="sameAsReceivers = !sameAsReceivers" class="w3-button w3-theme w3-round">{{ sameAsReceivers ? 'different from receivers' : 'same as receivers' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UsersList from '@/components/user/UsersList.vue'

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
    receiversUsers () {
      var users = []
      for (var ix in this.receivers) {
        users.push(this.receivers[ix].user)
      }
      return users
    },
    actualEvaluatorsUsers () {
      var users = []
      for (var ix in this.actualEvaluators) {
        users.push(this.actualEvaluators[ix].user)
      }
      return users
    },
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
    addReceiver (user) {
      if (this.indexOfReceiver(user) === -1) {
        this.receivers.push({
          user: user,
          percent: 0
        })
      }
    },
    addEvaluator (user) {
      if (this.indexOfEvaluator(user) === -1) {
        this.evaluators.push({
          user: user,
          weight: 0
        })
      }
    },
    indexOfReceiver (user) {
      for (var ix in this.receivers) {
        if (this.receivers[ix].user.c1Id === user.c1Id) {
          return ix
        }
      }
      return -1
    },
    removeReceiver (user) {
      var ix = this.indexOfReceiver(user)
      if (ix !== -1) {
        this.receivers.splice(ix, 1)
      }
    },
    indexOfEvaluator (user) {
      for (var ix in this.evaluators) {
        if (this.evaluators[ix].user.c1Id === user.c1Id) {
          return ix
        }
      }
      return -1
    },
    removeEvaluator (user) {
      var ix = this.indexOfEvaluator(user)
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
