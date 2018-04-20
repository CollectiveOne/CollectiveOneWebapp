<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Edit notifications of "{{ elementName }}"</h2>
        </div>

        <div v-if="subscriber" class="w3-container form-container">

          <div class="w3-row-padding">
            <div class="w3-col m4 s12 text-column">General Config:</div>
            <div class="w3-col m8 s12">
              <div class="w3-row-padding">
                <div class="w3-col s6">
                  <button
                    class="w3-button w3-round"
                    :class="{'app-button': subscriber.inheritConfig === 'INHERIT', 'app-button-light': subscriber.inheritConfig !== 'INHERIT'}"
                    @click="subscriber.inheritConfig = 'INHERIT'">
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

          <div class="slider-container">
            <transition name="slideDownUp">
              <div v-if="subscriber.inheritConfig === 'CUSTOM'" class="">
                <hr>

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
                          :class="{'app-button': subscriber.inAppConfig === 'DISABLED', 'app-button-light': subscriber.inAppConfig !== 'DISABLED'}"
                          @click="subscriber.inAppConfig = 'DISABLED'">
                          <i class="fa fa-circle" aria-hidden="true"></i>
                        </button>
                      </div>
                      <div class="w3-col s3">
                        <button
                          class="w3-button w3-round"
                          :class="{'app-button': subscriber.inAppConfig === 'ONLY_MENTIONS', 'app-button-light': subscriber.inAppConfig !== 'ONLY_MENTIONS'}"
                          @click="subscriber.inAppConfig = 'ONLY_MENTIONS'">
                          <i class="fa fa-circle" aria-hidden="true"></i>
                        </button>
                      </div>
                      <div class="w3-col s3">
                        <button
                          class="w3-button w3-round"
                          :class="{'app-button': subscriber.inAppConfig === 'ONLY_MESSAGES', 'app-button-light': subscriber.inAppConfig !== 'ONLY_MESSAGES'}"
                          @click="subscriber.inAppConfig = 'ONLY_MESSAGES'">
                          <i class="fa fa-circle" aria-hidden="true"></i>
                        </button>
                      </div>
                      <div class="w3-col s3">
                        <button
                          class="w3-button w3-round"
                          :class="{'app-button': subscriber.inAppConfig === 'ALL_EVENTS', 'app-button-light': subscriber.inAppConfig !== 'ALL_EVENTS'}"
                          @click="subscriber.inAppConfig = 'ALL_EVENTS'">
                          <i class="fa fa-circle" aria-hidden="true"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-if="subscriber.inAppConfig !== 'DISABLED'" class="">
                  <div class="w3-row-padding table-row">
                    <div class="w3-col m4 s12 text-column">Push Notifications:<br><small>Sent only when not active in the app</small></div>
                    <div class="w3-col m8 s12">
                      <div class="w3-row-padding">
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.pushConfig === 'DISABLED', 'app-button-light': subscriber.pushConfig !== 'DISABLED'}"
                            @click="subscriber.pushConfig = 'DISABLED'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.pushConfig === 'ONLY_MENTIONS', 'app-button-light': subscriber.pushConfig !== 'ONLY_MENTIONS'}"
                            @click="subscriber.pushConfig = 'ONLY_MENTIONS'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.pushConfig === 'ONLY_MESSAGES', 'app-button-light': subscriber.pushConfig !== 'ONLY_MESSAGES'}"
                            @click="subscriber.pushConfig = 'ONLY_MESSAGES'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.pushConfig === 'ALL_EVENTS', 'app-button-light': subscriber.pushConfig !== 'ALL_EVENTS'}"
                            @click="subscriber.pushConfig = 'ALL_EVENTS'">
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
                            :class="{'app-button': subscriber.emailNowConfig === 'DISABLED', 'app-button-light': subscriber.emailNowConfig !== 'DISABLED'}"
                            @click="subscriber.emailNowConfig = 'DISABLED'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailNowConfig === 'ONLY_MENTIONS', 'app-button-light': subscriber.emailNowConfig !== 'ONLY_MENTIONS'}"
                            @click="subscriber.emailNowConfig = 'ONLY_MENTIONS'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailNowConfig === 'ONLY_MESSAGES', 'app-button-light': subscriber.emailNowConfig !== 'ONLY_MESSAGES'}"
                            @click="subscriber.emailNowConfig = 'ONLY_MESSAGES'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailNowConfig === 'ALL_EVENTS', 'app-button-light': subscriber.emailNowConfig !== 'ALL_EVENTS'}"
                            @click="subscriber.emailNowConfig = 'ALL_EVENTS'">
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
                            :class="{'app-button': subscriber.emailSummaryConfig === 'DISABLED', 'app-button-light': subscriber.emailSummaryConfig !== 'DISABLED'}"
                            @click="subscriber.emailSummaryConfig = 'DISABLED'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailSummaryConfig === 'ONLY_MENTIONS', 'app-button-light': subscriber.emailSummaryConfig !== 'ONLY_MENTIONS'}"
                            @click="subscriber.emailSummaryConfig = 'ONLY_MENTIONS'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailSummaryConfig === 'ONLY_MESSAGES', 'app-button-light': subscriber.emailSummaryConfig !== 'ONLY_MESSAGES'}"
                            @click="subscriber.emailSummaryConfig = 'ONLY_MESSAGES'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                        <div class="w3-col s3">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailSummaryConfig === 'ALL_EVENTS', 'app-button-light': subscriber.emailSummaryConfig !== 'ALL_EVENTS'}"
                            @click="subscriber.emailSummaryConfig = 'ALL_EVENTS'">
                            <i class="fa fa-circle" aria-hidden="true"></i>
                          </button>
                        </div>
                      </div>
                      <div class="w3-row-padding">
                        <div class="w3-col s6">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailSummaryPeriodConfig === 'DAILY', 'app-button-light': subscriber.emailSummaryPeriodConfig !== 'DAILY'}"
                            @click="subscriber.emailSummaryPeriodConfig = 'DAILY'">
                            daily
                          </button>
                        </div>
                        <div class="w3-col s6">
                          <button
                            class="w3-button w3-round"
                            :class="{'app-button': subscriber.emailSummaryPeriodConfig === 'WEEKLY', 'app-button-light': subscriber.emailSummaryPeriodConfig !== 'WEEKLY'}"
                            @click="subscriber.emailSummaryPeriodConfig = 'WEEKLY'">
                            weekly
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>

                </div>

              </div>
            </transition>
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
      showError: false,
      errorMessage: 'sample'
    }
  },

  computed: {
    isSection () {
      return this.type === 'SECTION'
    },
    elementName () {
      if (this.isSection) {
        return this.section.title
      } else {
        return this.initiative.meta.name
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
    updateSubscriber () {
      this.axios.get('/1/notifications/subscriber/' + this.type + '/' + this.elementId).then((response) => {
        this.subscriber = response.data.data
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
