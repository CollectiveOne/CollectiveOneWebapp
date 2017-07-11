<template lang="html">
  <div class="">

    <div v-if="!multipleReceivers" class="w3-row receiver-label">
      <label class="w3-text-indigo"><b>Receiver:</b></label>
    </div>

    <div v-if="multipleReceivers" class="w3-row receiver-label">
      <label class="w3-text-indigo"><b>Select and add new receiver:</b></label>
    </div>
    <div class="w3-row receivers-selector">
      <div class="w3-col s7">
        <app-user-selector class="user-selector"
          anchor="c1Id" label="nickname"
          url="/1/secured/users/suggestions"
          @select="newReceiverSelected($event)">
        </app-user-selector>
      </div>

      <div v-if="multipleReceivers" class="">
        <div class="w3-col s3 percent-div">
          <div class="w3-row">
            <div class="w3-col s8">
              <input v-model.number="newReceiverPercent" class="w3-input w3-border w3-hover-light-gray w3-round perc-input w3-right" type="number" step="5" min="0">
            </div>
            <div class="w3-col s4 d2-color w3-left">
              <i class="fa fa-percent" aria-hidden="true"></i>
            </div>
          </div>
        </div>

        <div class="w3-col s2 w3-center">
          <button class="w3-button w3-theme w3-round add-btn" @click="addReceiver()">add</button>
        </div>
      </div>
      <div v-else class="">
        <button
          class="w3-button w3-theme w3-round" @click="multipleReceivers = true"
          type="button" name="button">distribute among multiple receivers
        </button>
      </div>

    </div>

    <hr v-if="multipleReceivers">
    <div v-if="multipleReceivers" class="w3-row receiver-label">
      <label class="w3-text-indigo"><b>Selected receivers are:</b></label>
    </div>

    <div v-if="multipleReceivers" class="receivers-list-container">
      <div v-for="receiver in receivers" class="w3-row">
        <div class="w3-col s6">
          <app-user-avatar :user="receiver.user"></app-user-avatar>
        </div>
        <div class="w3-col s4">
          <div class="w3-row w3-padding input-div">
            <div class="w3-col s10">
              <input v-model.number="receiver.percent" class="w3-input w3-border w3-hover-light-gray w3-round perc-input w3-right" type="number" step="5" min="0">
            </div>
            <div class="w3-col s2 d2-color w3-left">
              <i class="fa fa-percent" aria-hidden="true"></i>
            </div>
          </div>
        </div>
        <div class="w3-col s2 w3-xxlarge w3-button w3-center">
          <div @click="removeReceiver(receiver)"><i class="fa fa-times-circle-o l1-color" aria-hidden="true"></i></div>
        </div>
      </div>
    </div>

    <div v-if="multipleReceivers && receivers.length > 0" class="w3-row">
      <div v-if="!percentagesOk" class="w3-panel w3-round w3-padding warning-panel">
        Percentages should sum 100%, <b>please  {{ missingPercent < 0 ? 'remove ' + Math.abs(missingPercent) + '%' : 'add ' + Math.abs(missingPercent) + '%' }}</b>
      </div>
      <div v-else class="w3-panel w3-round success-panel w3-padding">
        Well done, percentages sum 100%
      </div>
    </div>
    <hr v-if="multipleReceivers">

  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserSelector from '@/components/user/UserSelector.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar,
    'app-user-selector': UserSelector
  },

  data () {
    return {
      multipleReceivers: false,
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
    newReceiverSelected (_user) {
      if (!this.multipleReceivers) {
        var receiver = {
          user: _user,
          percent: 100
        }
        this.receivers = [ receiver ]
      } else {
        this.newReceiverUser = _user
      }
    },
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

.receiver-label {
  margin-bottom: 10px;
}

.perc-input {
  max-width: 100px;
}

.receivers-selector {
  margin-bottom: 20px;
}

.input-div {
  padding-top: 16px !important;
}

.fa-percent {
  font-size: 22px;
  margin-top: 7.5px;
  margin-left: 5px;
}

.add-btn {
  width: 100%;
}

.receivers-list-container {
  max-width: 600px;
  margin: 0 auto;
}

</style>
