<template lang="html">
  <div class="">
    <div class="w3-row">
      <div class="receivers-col w3-col m6">
        <label class="d2-color"><b>Receivers:</b></label>
        <div class="w3-row users-list">
          <app-users-list :usersInit="receivers" @updated="receiversUpdated($event)"
            type="receivers">
          </app-users-list>
        </div>
      </div>
      <div class="w3-col m6">
        <label class="d2-color"><b>Evaluators: {{ sameAsReceivers ? '(same as receivers)' : ''}}</b></label>
        <div v-if="!sameAsReceivers" class="w3-row users-list" :class="{'covered-div': sameAsReceivers}">
          <app-users-list :usersInit="evaluators" @updated="evaluatorsUpdated($event)"
            type="evaluators">
          </app-users-list>
        </div>
        <div class="w3-row w3-center button-evaluators">
          <button @click="sameAsReceivers = !sameAsReceivers"
            class="w3-button w3-theme w3-round">
            {{ sameAsReceivers ? 'different from receivers?' : 'same as receivers' }}
          </button>
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
    receiversUpdated (receivers) {
      this.receivers = receivers
      if (this.sameAsReceivers) {
        this.evaluators = receivers
      }
    },
    evaluatorsUpdated (evaluators) {
      this.evaluators = evaluators
    }
  }
}
</script>

<style scoped>

.receivers-col {
  margin-bottom: 15px;
}

.users-list {
  margin-top: 10px;
}

.button-evaluators {
  margin-top: 10px;
}


</style>
