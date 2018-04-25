<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Edit notifications of {{ elementName }}</h2>
        </div>

        <div class="w3-container form-container">

          <div v-if="subscriber" class="w3-row-padding">
            <div class="w3-col m4 s12 text-column">General Config:</div>
            <div class="w3-col m8 s12">
              <div class="w3-row-padding">
                <div class="w3-col s6">
                  <button
                    class="w3-button w3-round"
                    :class="{'app-button': subscriber.inheritConfig === 'INHERIT', 'app-button-light': subscriber.inheritConfig !== 'INHERIT'}"
                    @click="setInherit()">
                    Inherit
                  </button>
                </div>
                <div class="w3-col s6">
                  <button
                    class="w3-button w3-round"
                    :class="{'app-button': subscriber.inheritConfig === 'CUSTOM', 'app-button-light': subscriber.inheritConfig !== 'CUSTOM'}"
                    @click="subscriber.inheritConfig = 'CUSTOM'">
                    Custom
                  </button>
                </div>
              </div>
            </div>
          </div>

          <hr>

          <div v-if="applicableSubscriber" class="details-configuration">
            <div class="w3-row-padding">
              <div class="w3-col m4 s12"></div>
              <div class="w3-col m8 s12">
                <div class="w3-row-padding column-header">
                  <div class="w3-col s3">
                    disable
                  </div>
                  <div class="w3-col s3">
                    only mentions
                  </div>
                  <div class="w3-col s3">
                    only messages
                  </div>
                  <div class="w3-col s3">
                    all events
                  </div>
                </div>
              </div>
            </div>

            <div class="w3-row-padding table-row">
              <div class="w3-col m4 s12 text-column">In-App Notifications:</div>
              <div class="w3-col m8 s12">
                <div class="w3-row-padding">
                  <div class="w3-col s3">
                    <button
                      class="w3-button w3-round"
                      :class="{'app-button': applicableSubscriber.inAppConfig === 'DISABLED', 'app-button-light': applicableSubscriber.inAppConfig !== 'DISABLED'}"
                      @click="applicableSubscriber.inAppConfig = 'DISABLED'">
                      <i class="fa fa-circle" aria-hidden="true"></i>
                    </button>
                  </div>
                  <div class="w3-col s3">
                    <button
                      class="w3-button w3-round"
                      :class="{'app-button': applicableSubscriber.inAppConfig === 'ONLY_MENTIONS', 'app-button-light': applicableSubscriber.inAppConfig !== 'ONLY_MENTIONS'}"
                      @click="applicableSubscriber.inAppConfig = 'ONLY_MENTIONS'">
                      <i class="fa fa-circle" aria-hidden="true"></i>
                    </button>
                  </div>
                  <div class="w3-col s3">
                    <button
                      class="w3-button w3-round"
                      :class="{'app-button': applicableSubscriber.inAppConfig === 'ONLY_MESSAGES', 'app-button-light': applicableSubscriber.inAppConfig !== 'ONLY_MESSAGES'}"
                      @click="applicableSubscriber.inAppConfig = 'ONLY_MESSAGES'">
                      <i class="fa fa-circle" aria-hidden="true"></i>
                    </button>
                  </div>
                  <div class="w3-col s3">
                    <button
                      class="w3-button w3-round"
                      :class="{'app-button': applicableSubscriber.inAppConfig === 'ALL_EVENTS', 'app-button-light': applicableSubscriber.inAppConfig !== 'ALL_EVENTS'}"
                      @click="applicableSubscriber.inAppConfig = 'ALL_EVENTS'">
                      <i class="fa fa-circle" aria-hidden="true"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="applicableSubscriber.inAppConfig !== 'DISABLED'" class="">
              <div class="w3-row-padding table-row">
                <div class="w3-col m4 s12 text-column">Push Notifications:<br><small>Sent only when not active in the app</small></div>
                <div class="w3-col m8 s12">
                  <div class="w3-row-padding">
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.pushConfig === 'DISABLED', 'app-button-light': applicableSubscriber.pushConfig !== 'DISABLED'}"
                        @click="applicableSubscriber.pushConfig = 'DISABLED'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.pushConfig === 'ONLY_MENTIONS', 'app-button-light': applicableSubscriber.pushConfig !== 'ONLY_MENTIONS'}"
                        @click="applicableSubscriber.pushConfig = 'ONLY_MENTIONS'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.pushConfig === 'ONLY_MESSAGES', 'app-button-light': applicableSubscriber.pushConfig !== 'ONLY_MESSAGES'}"
                        @click="applicableSubscriber.pushConfig = 'ONLY_MESSAGES'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.pushConfig === 'ALL_EVENTS', 'app-button-light': applicableSubscriber.pushConfig !== 'ALL_EVENTS'}"
                        @click="applicableSubscriber.pushConfig = 'ALL_EVENTS'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="w3-row-padding table-row">
                <div class="w3-col m4 s12 text-column">Immediate Email:<br><small>Sent only when not active in the app</small></div>
                <div class="w3-col m8 s12">
                  <div class="w3-row-padding">
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailNowConfig === 'DISABLED', 'app-button-light': applicableSubscriber.emailNowConfig !== 'DISABLED'}"
                        @click="applicableSubscriber.emailNowConfig = 'DISABLED'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailNowConfig === 'ONLY_MENTIONS', 'app-button-light': applicableSubscriber.emailNowConfig !== 'ONLY_MENTIONS'}"
                        @click="applicableSubscriber.emailNowConfig = 'ONLY_MENTIONS'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailNowConfig === 'ONLY_MESSAGES', 'app-button-light': applicableSubscriber.emailNowConfig !== 'ONLY_MESSAGES'}"
                        @click="applicableSubscriber.emailNowConfig = 'ONLY_MESSAGES'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailNowConfig === 'ALL_EVENTS', 'app-button-light': applicableSubscriber.emailNowConfig !== 'ALL_EVENTS'}"
                        @click="applicableSubscriber.emailNowConfig = 'ALL_EVENTS'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="w3-row-padding table-row">
                <div class="w3-col m4 s12 text-column">Email Summary:<br><small>Sent only when not already read in app</small></div>
                <div class="w3-col m8 s12">
                  <div class="w3-row-padding table-row">
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailSummaryConfig === 'DISABLED', 'app-button-light': applicableSubscriber.emailSummaryConfig !== 'DISABLED'}"
                        @click="applicableSubscriber.emailSummaryConfig = 'DISABLED'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailSummaryConfig === 'ONLY_MENTIONS', 'app-button-light': applicableSubscriber.emailSummaryConfig !== 'ONLY_MENTIONS'}"
                        @click="applicableSubscriber.emailSummaryConfig = 'ONLY_MENTIONS'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailSummaryConfig === 'ONLY_MESSAGES', 'app-button-light': applicableSubscriber.emailSummaryConfig !== 'ONLY_MESSAGES'}"
                        @click="applicableSubscriber.emailSummaryConfig = 'ONLY_MESSAGES'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                    <div class="w3-col s3">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailSummaryConfig === 'ALL_EVENTS', 'app-button-light': applicableSubscriber.emailSummaryConfig !== 'ALL_EVENTS'}"
                        @click="applicableSubscriber.emailSummaryConfig = 'ALL_EVENTS'">
                        <i class="fa fa-circle" aria-hidden="true"></i>
                      </button>
                    </div>
                  </div>
                  <div class="w3-row-padding">
                    <div class="w3-col s6">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailSummaryPeriodConfig === 'DAILY', 'app-button-light': applicableSubscriber.emailSummaryPeriodConfig !== 'DAILY'}"
                        @click="applicableSubscriber.emailSummaryPeriodConfig = 'DAILY'">
                        daily
                      </button>
                    </div>
                    <div class="w3-col s6">
                      <button
                        class="w3-button w3-round"
                        :class="{'app-button': applicableSubscriber.emailSummaryPeriodConfig === 'WEEKLY', 'app-button-light': applicableSubscriber.emailSummaryPeriodConfig !== 'WEEKLY'}"
                        @click="applicableSubscriber.emailSummaryPeriodConfig = 'WEEKLY'">
                        weekly
                      </button>
                    </div>
                  </div>
                </div>
              </div>

            </div>

            <div v-if="subscriber.inheritConfig === 'INHERIT'" class="obfuscate-div w3-display-container">
              <div class="w3-display-middle">
                Inhereting notification preferences from: <br><b>{{ applicableSubscriberElementName }}</b>
              </div>
            </div>
          </div>

          <hr>

          <app-error-panel
            :show="showError"
            :message="errorMessage">
          </app-error-panel>

          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button id="T_notificationCancelButton" type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button  id="T_notificationAcceptButton" type="button" class="w3-button app-button" @click="accept()">Accept</button>
            </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    section: {
      type: Object,
      default: null
    },
    initiative: {
      type: Object,
      default: null
    },
    type: {
      type: String,
      default: 'SECTION'
    }
  },

  data () {
    return {
      subscriber: null,
      inheritedFrom: null,
      showError: false,
      errorMessage: 'sample'
    }
  },

  computed: {
    applicableSubscriber () {
      if (this.subscriber) {
        if (this.subscriber.inheritConfig === 'CUSTOM') {
          return this.subscriber
        } else {
          return this.inheritedFrom
        }
      }
    },
    applicableSubscriberElementName () {
      if (this.inheritedFrom) {
        switch (this.inheritedFrom.elementType) {
          case 'SECTION':
            return '"' + this.inheritedFrom.section.title + '" Section'

          case 'INITIATIVE':
            return '"' + this.inheritedFrom.initiative.meta.name + '" Initiative'

          case 'COLLECTIVEONE':
            return 'your global subscription preferences'
        }
      }
    },
    isSection () {
      return this.type === 'SECTION'
    },
    elementName () {
      if (this.isSection) {
        return '"' + this.section.title + '" Section'
      } else {
        return '"' + this.initiative.meta.name + '" Initiative'
      }
    },
    elementId () {
      if (this.isSection) {
        return this.section.id
      } else {
        return this.initiative.id
      }
    }
  },

  methods: {
    setInherit () {
      this.subscriber.inheritConfig = 'INHERIT'
      this.updateInheritedFrom()
    },
    updateInheritedFrom () {
      /* update only the inherited from subscriber, needed when user switching to inherited */
      this.axios.get('/1/notifications/subscriber/' + this.type + '/' + this.elementId + '/inheritFrom').then((response) => {
        this.inheritedFrom = response.data.data
      })
    },
    updateSubscriber () {
      this.axios.get('/1/notifications/subscriber/' + this.type + '/' + this.elementId).then((response) => {
        this.subscriber = response.data.data
        /* initialize the inherited from subscriber if available */
        if (this.subscriber.inheritConfig === 'INHERIT') {
          this.inheritedFrom = this.subscriber.applicableSubscriber
        }
      })
    },
    closeThis () {
      this.$emit('close')
    },
    accept () {
      let ok = true

      if (this.subscriber.inheritConfig === 'CUSTOM') {
        if (this.subscriber.inAppConfig === null) {
          ok = false
          this.showError = true
          this.errorMessage = 'please configure the In-App notifications'
        }

        if (this.subscriber.pushConfig === null) {
          ok = false
          this.showError = true
          this.errorMessage = 'please configure the Push notifications'
        }

        if (this.subscriber.emailNowConfig === null) {
          ok = false
          this.showError = true
          this.errorMessage = 'please configure the immediate emails'
        }

        if (this.subscriber.emailSummaryConfig === null) {
          ok = false
          this.showError = true
          this.errorMessage = 'please configure the email summary'
        }

        if (this.subscriber.emailSummaryPeriodConfig === null) {
          ok = false
          this.showError = true
          this.errorMessage = 'please configure the email summary period'
        }
      }

      if (ok) {
        this.showError = false
        this.axios.put('/1/notifications/subscriber/' + this.type + '/' + this.elementId, this.subscriber).then((response) => {
          this.closeThis()
        })
      }
    }
  },

  mounted () {
    this.updateSubscriber()
  }
}
</script>

<style scoped>

.details-configuration {
  position: relative;
  padding: 6px 0px;
}

.obfuscate-div {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: rgba(0.5, 0.5, 0.5, 0.5);
  top: 0;
  left: 0;
  border-radius: 6px;
  color: white;
  font-size: 22px;
  text-align: center;
}

.w3-col {
  min-height: 1px;
}

.form-container {
  padding-top: 35px;
  padding-bottom: 35px;
}

.text-column {
  text-align: right;
}

.table-row {
  margin-bottom: 16px;
}

.column-header {
  text-align: center;
  font-weight: bold;
  height: 30px;
}

button {
  width: 100%;
}

</style>
