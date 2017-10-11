<template lang="html">
  <div class="">
    <label for=""><b>Activity:</b></label>
    <app-thread-story :threadId="threadId"></app-thread-story>
    <app-new-message @send="send($event)"></app-new-message>
  </div>
</template>

<script>
import ThreadStory from '@/components/conversations/ThreadStory.vue'
import NewMessage from '@/components/conversations/NewMessage.vue'

export default {
  components: {
    'app-thread-story': ThreadStory,
    'app-new-message': NewMessage
  },

  props: {
    threadId: {
      type: String,
      default: ''
    },
    contextType: {
      type: String,
      default: ''
    },
    contextElementId: {
      type: String,
      default: ''
    }
  },

  methods: {
    send (message) {
      var newMessageDto = {
        contextType: this.contextType,
        contextElementId: this.contextElementId,
        text: message
      }

      if (this.threadId === '') {
        this.axios.post('/1/messages', newMessageDto).then((response) => {
        })
      } else {
        this.axios.post('/1/messages/' + this.threadId, newMessageDto).then((response) => {
        })
      }
    }
  }
}
</script>

<style scoped>
</style>
