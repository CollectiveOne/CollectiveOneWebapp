<template lang="html">
  <div class="thread-container w3-display-container">
    <div id="history-container" class="w3-row history-container w3-border">
      <app-activity-getter
        :url="url"
        :reverse="true"
        :addBorders="false"
        :showMessages="true"
        :onlyMessages="showOnlyMessages"
        :polling="true"
        :triggerUpdate="triggerUpdate"
        :contextElementId="contextElementId"
        @updated="scrollToBottom()"
        @last-activity="lastActivityReceived($event)"
        @edit-message="editMessage($event)"
        @reply-to-message="replyToMessage($event)">
      </app-activity-getter>
    </div>
    <div class="w3-row w3-margin-top bottom-container">
      <div v-if="editing" class="">
        <div class="success-panel w3-padding w3-margin-bottom">
          Editing message
          <span @click="cancelEdit()" class="cursor-pointer">(cancel <i class="fa fa-times"></i>)</span>
        </div>
      </div>
      <div v-if="replying" class="">
        <div class="success-panel w3-padding w3-margin-bottom">
          Replying to message from <b>{{ replyingToActivity.message.author.nickname }}</b> directly in the <b>{{ replyToMessageContext }}</b>.
          <span @click="cancelReply()" class="cursor-pointer">(cancel <i class="fa fa-times"></i>)</span>
        </div>
      </div>
      <app-error-panel
        :show="showMembersOnly"
        message="sorry, only members of the initiative can send messages here">
      </app-error-panel>
      <app-messages-markdown-editor
        class="editor-container"
        v-model="newMessageText"
        placeholder="say something"
        :showToolbar="false"
        :showSend="true"
        @c-focus="writting = true"
        @c-blur="writting = false"
        @send="send($event)">
      </app-messages-markdown-editor>
    </div>
    <div class="w3-display-topright only-messages-button">
      <button
        class="w3-button app-button tooltip" type="button" name="button"
        @click="showOnlyMessagesClicked()">
        <span v-if="!showOnlyMessages"><i class="fa fa-comment-o" aria-hidden="true"></i></span>
        <span v-else=""><i class="fa fa-list" aria-hidden="true"></i></span>
        <span v-if="showOnlyMessages" class="tooltiptext gray-1">show activity too</span>
        <span v-else class="tooltiptext gray-1">show only messages</span>
      </button>
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
    url: {
      type: String,
      default: ''
    },
    onlyMessagesInit: {
      type: Boolean,
      defaul: false
    }
  },

  data () {
    return {
      newMessageText: '',
      triggerUpdate: true,
      intervalId: 0,
      showOnlyMessages: false,
      editing: false,
      messageToEdit: null,
      showMembersOnly: false,
      writting: false,
      lastMessage: null,
      replying: false,
      replyingToActivity: null
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
    replyingToActivityInView () {
      if (this.replyingToActivity) {
        return this.replyingToActivity.modelView !== null
      } else {
        return false
      }
    },
    replyingToActivityInInitiative () {
      if (this.replyingToActivity) {
        return !this.replyingToActivityInView && !this.replyingToActivityInSection && !this.replyingToActivityInCard
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
      if (this.replyingToActivityInView) {
        return this.replyingToActivity.modelView.title + ' view'
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
    send () {
      var message = {
        text: this.newMessageText
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
          if (this.replyingToActivityInView) {
            contextType = 'MODEL_VIEW'
            contextElementId = this.replyingToActivity.modelView.id
          }
          if (this.replyingToActivityInInitiative) {
            contextType = 'INITIATIVE'
            contextElementId = this.replyingToActivity.initiative.id
          }
        }

        this.axios.post('/1/messages/' + contextType + '/' + contextElementId, message).then((response) => {
          if (response.data.result === 'success') {
            this.replying = false
            this.newMessageText = ''
            this.triggerUpdate = !this.triggerUpdate
          } else {
            this.showMembersOnly = true
          }
        })
      } else {
        this.axios.put('/1/messages/' + this.contextType + '/' + this.contextElementId + '/' + this.messageToEdit.id, message).then((response) => {
          if (response.data.result === 'success') {
            this.editing = false
            this.newMessageText = ''
            this.triggerUpdate = !this.triggerUpdate
          }
        })
      }
    },
    scrollToBottom () {
      this.$nextTick(() => {
        var container = this.$el.querySelector('#history-container')
        container.scrollTop = container.scrollHeight
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

  created () {
    this.showOnlyMessages = this.onlyMessagesInit
  },

  mounted () {
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
  display: flex;
  flex-direction: column;
}

.history-container {
  min-height: 60px;
  height: calc(100% - 120px);
  overflow: auto;
  flex-grow: 1;
}

.bottom-container {
  flex-grow: 1;
}

.only-messages-button {
  margin-right: 25px;
  margin-top: 10px;
}

.tooltip .tooltiptext {
    top: 8%;
    right: 105%;
}

</style>
