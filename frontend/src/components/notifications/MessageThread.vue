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
        @edit-message="editMessage($event)">
      </app-activity-getter>
    </div>
    <div class="w3-row w3-margin-top">
      <div v-if="editing" class="">
        <div class="success-panel w3-padding">
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
      showMembersOnly: false
    }
  },

  computed: {
    isLoggedAMember () {
      return this.$store.getters.isLoggedAMember
    }
  },

  methods: {
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
    }
  },

  created () {
    this.showOnlyMessages = this.onlyMessagesInit
  },

  mounted () {
    this.scrollToBottom()
  },

  destroyed () {
  }
}
</script>

<style scoped>

.CodeMirror, .CodeMirror-scroll {
	min-height: 50px !important;
}

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
