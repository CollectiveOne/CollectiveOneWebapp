<template lang="html">
  <div class="thread-container w3-display-container">
    <div ref="historyContainer" class="w3-row history-container w3-border" >
      <app-activity-getter
        :reverse="true"
        :addBorders="false"
        :showMessagesText="true"
        :showMessages="showMessages"
        :showEvents="showEvents"
        :triggerRefresh="triggerRefresh"
        :triggerUpdate="triggerUpdate"
        :contextType="contextType"
        :contextElementId="contextElementId"
        :levels="levels"
        @updated="scrollToBottom()"
        @added-older="addedOlder($event)"
        @last-activity="lastActivityReceived($event)"
        @edit-message="editMessage($event)"
        @reply-to-message="replyToMessage($event)">
      </app-activity-getter>
    </div>
    <div v-if="$store.state.user.authenticated" class="w3-row w3-margin-top bottom-container">
      <div v-if="editing" class="">
        <div class="success-panel w3-padding w3-margin-bottom">
          {{ $t('notifications.EDITING_MESSAGE') }}
          <span @click="cancelEdit()" class="cursor-pointer">({{ $t('notifications.CANCEL') }} <i class="fa fa-times"></i>)</span>
        </div>
      </div>
      <div v-if="replying" class="">
        <div class="success-panel w3-padding w3-margin-bottom">
          <span v-html="$t('notifications.REPLYING_TO', { author: replyingToActivity.message.author.nickname, context: replyToMessageContext })"></span>.
          <span @click="cancelReply()" class="cursor-pointer">({{ $t('notifications.CANCEL') }} <i class="fa fa-times"></i>)</span>
        </div>
      </div>
      <app-error-panel
        :show="showSendError"
        :message="sendErrorMessage">
      </app-error-panel>
      <app-markdown-editor
        class="editor-container"
        v-model="newMessageText"
        :placeholder="$t('notifications.SAY_SOMETHING')"
        :showToolbar="false"
        :showSendAndMentions="true"
        :elementId="contextElementId"
        @c-focus="writting = true"
        @c-blur="writting = false"
        @send="send($event)">
      </app-markdown-editor>
    </div>
  </div>
</template>

<script>
import ActivityGetter from '@/components/notifications/ActivityGetter.vue'

export default {
  components: {
    'app-activity-getter': ActivityGetter
  },

  props: {
    contextType: {
      type: String,
      default: ''
    },
    contextElementId: {
      type: String,
      default: ''
    },
    contextOfContextElementId: {
      type: String,
      default: ''
    },
    url: {
      type: String,
      default: ''
    },
    showMessages: {
      type: Boolean,
      defaul: true
    },
    showEvents: {
      type: Boolean,
      defaul: true
    },
    levels: {
      type: Number,
      default: 1
    }
  },

  data () {
    return {
      newMessageText: '',
      intervalId: 0,
      editing: false,
      messageToEdit: null,
      showSendError: false,
      sendErrorMessage: '',
      writting: false,
      lastMessage: null,
      replying: false,
      replyingToActivity: null,
      triggerRefresh: false,
      triggerUpdate: false
    }
  },

  computed: {
    isLoggedAMember () {
      return this.$store.getters.isLoggedAMember
    },
    replyingToActivityInCard () {
      if (this.replyingToActivity) {
        return this.replyingToActivity.modelCardWrapper !== null
      } else {
        return false
      }
    },
    replyingToActivityInSection () {
      if (this.replyingToActivity) {
        return this.replyingToActivity.modelSection !== null
      } else {
        return false
      }
    },
    replyingToActivityInInitiative () {
      if (this.replyingToActivity) {
        return !this.replyingToActivityInSection && !this.replyingToActivityInCard
      } else {
        return false
      }
    },
    replyToMessageContext () {
      if (this.replyingToActivityInCard) {
        return this.replyingToActivity.modelCardWrapper.card.title + ' card'
      }
      if (this.replyingToActivityInSection) {
        return this.replyingToActivity.modelSection.title + ' section'
      }
      if (this.replyingToActivityInInitiative) {
        return this.replyingToActivity.initiative.meta.name + ' initiative'
      }
    }
  },

  methods: {
    lastActivityReceived (activity) {
      if (activity.type === 'MESSAGE_POSTED') {
        this.lastMessage = activity.message
      } else {
        this.lastMessage = null
      }
    },
    editMessage (message) {
      this.replying = false
      this.editing = true
      this.messageToEdit = message
      this.newMessageText = message.text
    },
    replyToMessage (activity) {
      this.editing = false
      this.replying = true
      this.replyingToActivity = activity
    },
    cancelEdit () {
      this.editing = false
      this.newMessageText = ''
    },
    cancelReply () {
      this.replying = false
    },
    send (data) {
      var message = {
        text: this.newMessageText,
        uuidsOfMentions: data.mentions
      }
      if (!this.editing) {
        var contextType = ''
        var contextElementId = ''

        if (!this.replying) {
          contextType = this.contextType
          contextElementId = this.contextElementId
        } else {
          if (this.replyingToActivityInCard) {
            contextType = 'MODEL_CARD'
            contextElementId = this.replyingToActivity.modelCardWrapper.id
          }
          if (this.replyingToActivityInSection) {
            contextType = 'MODEL_SECTION'
            contextElementId = this.replyingToActivity.modelSection.id
          }
          if (this.replyingToActivityInInitiative) {
            contextType = 'INITIATIVE'
            contextElementId = this.replyingToActivity.initiative.id
          }
        }

        this.axios.post(
          '/1/messages/' + contextType + '/' + contextElementId,
          message, {
            params: {
              contextOfContextElementId: this.contextOfContextElementId
            }
          }).then((response) => {
          if (response.data.result === 'success') {
            this.replying = false
            this.newMessageText = ''
            this.triggerRefresh = !this.triggerRefresh
          } else {
            this.showSendError = true
            if (this.data.message === 'not authorized') {
              this.sendErrorMessage = this.$t('notifications.ONLY_MEMBERS_CAN_COMMENT')
            } else {
              this.sendErrorMessage = response.data.message
            }
          }
        })
      } else {
        this.axios.put('/1/messages/' + this.contextType + '/' + this.contextElementId + '/' + this.messageToEdit.id, message).then((response) => {
          if (response.data.result === 'success') {
            this.editing = false
            this.newMessageText = ''
            this.triggerRefresh = !this.triggerRefresh
          }
        })
      }
    },
    scrollToBottom () {
      this.$nextTick(() => {
        var container = this.$refs.historyContainer
        if (container != null) {
          container.scrollTop = container.scrollHeight
        }
      })
    },
    addedOlder (payload) {
      this.$nextTick(() => {
        var container = this.$refs.historyContainer
        if (container != null) {
          var newHeight = container.childNodes[0].offsetHeight
          container.scrollTop = newHeight - payload.height
        }
      })
    },
    showOnlyMessagesClicked () {
      this.showOnlyMessages = !this.showOnlyMessages
      this.triggerUpdate = !this.triggerUpdate
    },
    atKeydown (e) {
      /* detect upkey to auto edit last message */
      if (this.writting && this.newMessageText === '' && this.lastMessage !== null) {
        /* esc */
        if (e.keyCode === 38) {
          this.editMessage(this.lastMessage)
        }
      }
    }
  },

  mounted () {
    this.showOnlyMessages = this.onlyMessagesInit
    this.scrollToBottom()
    window.addEventListener('keydown', this.atKeydown)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }
}
</script>

<style scoped>

.thread-container {
  height: 100%;
  max-height: 100vh;
  display: flex;
  flex-grow: 1;
  flex-direction: column;
}

.history-container {
  min-height: 60px;
  overflow: auto;
  flex-grow: 1;
}

.bottom-container {
  min-height: 50px;
  flex-shrink: 0;
}

.only-messages-button {
  margin-right: 25px;
  margin-top: 10px;
}

.tooltip .tooltiptext {
  top: 8%;
  right: 105%;
  background: blue;
}

.only-messages-button button {
  background-color: rgba(21, 165, 204, 0.4) !important;
}

</style>
