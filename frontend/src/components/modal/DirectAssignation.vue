<template lang="html">
  <div class="">

    <label class="w3-text-indigo"><b>Receivers:</b></label>
    <div v-for="receiver in receivers" class="w3-row">
      <div class="w3-col s6">
        <app-user-avatar :user="receiver.user"></app-user-avatar>
      </div>
      <div class="w3-col s4 w3-center">
        <div class="w3-row w3-padding input-div">
          <div class="w3-col s10">
            <input v-model.number="receiver.percent" class="w3-input w3-border w3-hover-light-gray w3-round" type="number">
          </div>
          <div class="w3-col s2 d2-color">
            <i class="fa fa-percent" aria-hidden="true"></i>
          </div>
        </div>
      </div>
      <div class="w3-col s2 w3-xxlarge w3-button w3-center">
        <div @click="removeReceiver(receiver)"><i class="fa fa-times-circle-o l1-color" aria-hidden="true"></i></div>
      </div>
    </div>
    <hr v-if="receivers.length > 0">

    <div class="w3-row">
      <div class="w3-col s7">
        <app-user-selector class="user-selector"
          anchor="c1Id" label="nickname"
          url="/1/secured/users/suggestions"
          @select="newReceiverUser = $event">
        </app-user-selector>
      </div>

      <div class="w3-col s3 w3-center percent-div">
        <div class="w3-row">
          <div class="w3-col s10">
            <input v-model.number="newReceiverPercent" class="w3-input w3-border w3-hover-light-gray w3-round" type="number">
          </div>
          <div class="w3-col s2 d2-color">
            <i class="fa fa-percent" aria-hidden="true"></i>
          </div>
        </div>
      </div>

      <div class="w3-col s2 w3-center">
        <button class="w3-button w3-theme-l1 w3-round" @click="addReceiver()">add</button>
      </div>

    </div>

    <hr>
    <div v-if="!percentagesOk" class="w3-panel w3-theme w3-round">
      Percentages should sum 100%, please  {{ missingPercent < 0 ? 'remove ' + Math.abs(missingPercent) + '%' : 'add ' + Math.abs(missingPercent) + '%' }}
    </div>


  </div>
</template>

<script>
import UserAvatar from '../user/UserAvatar.vue'
import UserSelector from '../user/UserSelector.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar,
    'app-user-selector': UserSelector
  },

  data () {
    return {
      receivers: [],
      newReceiverUser: null,
      newReceiverPercent: 0
    }
  },

  computed: {
    percentagesOk () {
      if (this.receivers.length > 0) {
        if (this.missingPercent !== 0) {
          return false
        } else {
          return true
        }
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

  watch: {
    receivers () {
      var data = {
        receivers: this.receivers,
        title: this.title,
        description: this.description
      }
      this.$emit('updated', data)
    }
  },

  methods: {
    addReceiver () {
      if (this.indexOfReceiver(this.newReceiverUser) === -1) {
        this.receivers.push(
          {
            user: this.newReceiverUser,
            percent: this.newReceiverPercent
          }
        )
      }
    },
    removeReceiver (receiver) {
      var ix = this.indexOfReceiver(receiver.user)
      if (ix !== -1) {
        this.receivers.splice(ix, 1)
      }
    },
    indexOfReceiver (user) {
      for (var ix in this.receivers) {
        if (this.receivers[ix].user.c1Id === user.c1Id) {
          return ix
        }
      }
      return -1
    }
  }
}
</script>

<style scoped>

.input-div {
  padding-top: 16px !important;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

</style>
