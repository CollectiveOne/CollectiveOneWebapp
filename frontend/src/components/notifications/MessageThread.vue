<template lang="html">
  <div class="w3-display-container">
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
        @edit-message="editMessage($event)">
      </app-activity-getter>
    </div>
    <div class="w3-row w3-margin-top">
      <div v-if="editing" class="">
        <div class="success-panel w3-padding w3-margin-bottom">
          Editing message
          <span @click="cancelEdit()" class="cursor-pointer">(cancel <i class="fa fa-times"></i>)</span>
        </div>
      </div>
      <app-error-panel
        :show="showMembersOnly"
        message="sorry, only members of the initiative can send messages here">
      </app-error-panel>
      <app-markdown-editor
        v-model="newMessageText"
        placeholder="say something"
        :showToolbar="false"
        :showSend="true"
        @c-focus="writting = true"
        @c-blur="writting = false"
        @send="send($event)">
      </app-markdown-editor>
    </div>
    <div class="w3-display-topright only-messages-button">
      <button
        class="w3-button app-button" type="button" name="button"
        @click="showOnlyMessagesClicked()">
        <span v-if="!showOnlyMessages"><i class="fa fa-comment-o" aria-hidden="true"></i></span>
        <span v-else=""><i class="fa fa-list" aria-hidden="true"></i></span>
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
      lastMessage: null
    }
  },

  computed: {
    isLoggedAMember () {
      return this.$store.getters.isLoggedAMember
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
      this.editing = true
      this.messageToEdit = message
      this.newMessageText = message.text
    },
    cancelEdit () {
      this.editing = false
      this.newMessageText = ''
    },
    send () {
      var message = {
        text: this.newMessageText
      }
      if (!this.editing) {
        this.axios.post('/1/messages/' + this.contextType + '/' + this.contextElementId, message).then((response) => {
          if (response.data.result === 'success') {
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

.history-container {
  min-height: 60px;
  max-height: 50vh;
  overflow: auto;
}

.only-messages-button {
  margin-right: 25px;
  margin-top: 10px;
}

</style>
