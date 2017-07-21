<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Edit notifications of {{ initiative.meta.name }}</h2>
        </div>

        <div v-if="subscriber" class="w3-container form-container">

          <div class="w3-row">
            <div class="w3-left label-div">
              <label class=""><b>See notifications in this page:</b></label>
            </div>
            <div class="w3-left w3-round button-bar">
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'app-button': !isSubscribed, 'app-button-light': isSubscribed}"
                @click="subscriber.state = 'UNSUBSCRIBED'">
                No
              </button>
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'app-button': isSubscribed, 'app-button-light': !isSubscribed}"
                @click="subscriber.state = 'SUBSCRIBED'">
                Yes
              </button>
            </div>
          </div>
          <br>
          <div v-show="isSubscribed" class="w3-row">
            <div class="w3-left label-div">
              <label class=""><b>... and send an email:</b></label>
            </div>
            <div class="w3-left button-bar">
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'app-button': isNever, 'app-button-light': !isNever}"
                @click="subscriber.emailNotificationsState = 'DISABLED'">
                No
              </button>
              <button
                class="w3-bar-item w3-button w3-round"
                :class="{'app-button': isImmediate, 'app-button-light': !isImmediate}"
                @click="subscriber.emailNotificationsState = 'SEND_NOW'">
                Yes
              </button>
            </div>
          </div>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
            </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      subscriber: null
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    isSubscribed () {
      return this.subscriber.state === 'SUBSCRIBED'
    },
    isNever () {
      return this.subscriber.emailNotificationsState === 'DISABLED'
    },
    isImmediate () {
      return this.subscriber.emailNotificationsState === 'SEND_NOW'
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
      this.$store.commit('showEditNotificationsModal', false)
    },
    accept () {
      this.axios.put('/1/secured/user/notifications/subscriber/' + this.initiative.id, this.subscriber).then((response) => {
        this.closeThis()
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
  margin-right: 20px;
  margin-top: 20px;
}

.form-container {
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
