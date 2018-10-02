<template lang="html">
  <div class="">
    <div class="w3-row-padding">
      <div class="receivers-col w3-col m6">
        <h5>{{ $t('tokens.RECEIVERS') }}:</h5>
        <div class="w3-row users-list">
          <app-users-list :usersInit="receivers" @updated="receiversUpdated($event)"
            type="receivers">
          </app-users-list>
        </div>
        <div class="slider-container">
          <transition name="slideDownUp">
            <div v-if="donorUsers.length > 0" class="w3-row">
              <div class="w3-panel light-grey w3-padding"
                v-html="$t('tokens.DONOR_WARNING')">
              </div>
            </div>
          </transition>
        </div>
      </div>
      <div class="w3-col m6">
        <h5>{{ $t('tokens.EVALUATORS') }}: {{ sameAsReceivers ? ( '(' + $t('tokens.SAME_AS_RECEIVERS') + ')') : ''}}</h5>
        <div v-if="!sameAsReceivers" class="w3-row users-list" :class="{'covered-div': sameAsReceivers}">
          <app-users-list :usersInit="evaluators" @updated="evaluatorsUpdated($event)"
            type="evaluators">
          </app-users-list>
        </div>
        <div class="w3-row w3-center button-evaluators">
          <button @click="switchSameAsReceivers()"
            class="w3-button app-button">
            {{ sameAsReceivers ? $t('tokens.DIFF_FROM_RECEIVERS_Q') : $t('tokens.SAME_AS_RECEIVERS') }}
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
    donorUsers () {
      var donorUsers = []
      for (var ix in this.receivers) {
        var receiver = this.receivers[ix]
        if (receiver.isDonor) {
          donorUsers.push(receiver)
        }
      }
      return donorUsers
    },
    donorUsersStr () {
      var str = ''
      for (var ix = 0; ix < this.donorUsers.length; ix++) {
        if (ix === 0) {
          str += ''
        } else {
          if (ix === this.donorUsers.length - 1) {
            str += ' and '
          } else {
            str += ', '
          }
        }

        str += this.donorUsers[ix].user.nickname
      }
      return str
    }
  },

  methods: {
    receiversUpdated (receivers) {
      this.receivers = receivers
      if (this.sameAsReceivers) {
        this.evaluators = receivers
      }
      this.emitUpdated()
    },
    evaluatorsUpdated (evaluators) {
      this.evaluators = evaluators
      this.emitUpdated()
    },
    switchSameAsReceivers () {
      this.sameAsReceivers = !this.sameAsReceivers
      this.emitUpdated()
    },
    emitUpdated () {
      var selected
      if (this.sameAsReceivers) {
        selected = {
          receivers: this.receivers,
          evaluators: this.receivers
        }
      } else {
        selected = {
          receivers: this.receivers,
          evaluators: this.evaluators
        }
      }
      this.$emit('updated', selected)
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
