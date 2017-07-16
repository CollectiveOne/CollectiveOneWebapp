<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-theme">
          <h2>Edit notifications of {{ initiative.meta.name }}</h2>
        </div>

        <form v-if="subscriber" class="w3-container">

          <div class="w3-row">
            <div class="w3-left label-div">
              <label class="d2-color"><b>Receive notifications:</b></label>
            </div>
            <div class="w3-left w3-round button-bar">
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'w3-theme': !isSubscribed, 'w3-light-gray': isSubscribed}"
                @click="subscriber.state = 'UNSUBSCRIBED'">
                No
              </button>
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'w3-theme': isSubscribed, 'w3-light-gray': !isSubscribed}"
                @click="subscriber.state = 'SUBSCRIBED'">
                Yes
              </button>
            </div>
          </div>
          <br>
          <div v-show="isSubscribed" class="w3-row">
            <div class="w3-left label-div">
              <label class="d2-color"><b>... and send an email:</b></label>
            </div>
            <div class="w3-left button-bar">
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'w3-theme': isNever, 'w3-light-gray': !isNever}"
                @click="subscriber.emailNotificationsState = 'DISABLED'">
                Never
              </button>
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'w3-theme': isImmediate, 'w3-light-gray': !isImmediate}"
                @click="subscriber.emailNotificationsState = 'SEND_NOW'">
                Immediately
              </button>
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'w3-theme': isOnceADay, 'w3-light-gray': !isOnceADay}"
                @click="subscriber.emailNotificationsState = 'SEND_ONCEADAY'">
                Once a day
              </button>
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'w3-theme': isOnceAWeek, 'w3-light-gray': !isOnceAWeek}"
                @click="subscriber.emailNotificationsState = 'SEND_ONCEAWEEK'">
                Once a week
              </button>
            </div>
          </div>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-light-gray w3-round" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button w3-theme w3-round" @click="accept()">Accept</button>
            </div>
          </div>

        </form>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    initiative: {
      type: Object
    }
  },

  data () {
    return {
      subscriber: null
    }
  },

  computed: {
    isSubscribed () {
      return this.subscriber.state === 'SUBSCRIBED'
    },
    isNever () {
      return this.subscriber.emailNotificationsState === 'DISABLED'
    },
    isImmediate () {
      return this.subscriber.emailNotificationsState === 'SEND_NOW'
    },
    isOnceADay () {
      return this.subscriber.emailNotificationsState === 'SEND_ONCEADAY'
    },
    isOnceAWeek () {
      return this.subscriber.emailNotificationsState === 'SEND_ONCEAWEEK'
    }
  },

  mounted () {
    this.updateSubscriber()
  },

  methods: {
    updateSubscriber () {
      this.axios.get('/1/secured/user/notifications/subscriber/' + this.initiative.id).then((response) => {
        this.subscriber = response.data.data
      })
    },
    closeThis () {
      this.$emit('close-this')
    },
    accept () {
      this.axios.put('/1/secured/user/notifications/subscriber/' + this.initiative.id, this.subscriber).then((response) => {
        this.closeThis()
        this.$emit('subscriber-updated')
      })
    }
  }
}
</script>

<style scoped>

.w3-modal {
  display: block;
}

.close-div {
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-times {
  color: rgb(255, 255, 255);
  margin-right: 20px;
  margin-top: 20px;
}

form {
  padding-top: 35px;
  padding-bottom: 35px;
}

.label-div {
  padding-top: 4px;
  width: 200px;
  text-align: right;
  margin-right: 20px;
}

.button-bar {
}

.button-bar .w3-bar-item {
  width: 110px;
  font-size: 12px;
}

.bottom-btns-row button {
  width: 100%;
}

</style>
