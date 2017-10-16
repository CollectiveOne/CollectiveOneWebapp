<template lang="html">
  <div class="">
    <div class="w3-row">
      <app-activity-getter :url="url" :reverse="reverse"></app-activity-getter>
    </div>
    <div class="w3-row w3-margin-top">
      <app-new-message @send="send($event)"></app-new-message>
    </div>
  </div>
</template>

<script>
import ActivityGetter from '@/components/notifications/ActivityGetter.vue'
import NewMessage from '@/components/conversations/NewMessage.vue'

export default {
  components: {
    'app-activity-getter': ActivityGetter,
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
    },
    url: {
      type: String,
      default: ''
    },
    reverse: {
      type: Boolean,
      default: false
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
      }
    }
  }
}
</script>

<style scoped>
</style>
