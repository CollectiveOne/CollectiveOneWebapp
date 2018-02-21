<template lang="html">
  <div class="messages-section-container">
    <app-message-thread
      contextType="INITIATIVE"
      :contextElementId="initiative.id"
      :onlyMessagesInit="true"
      :url="'/1/activity/initiative/' + initiative.id">
    </app-message-thread>
  </div>
</template>

<script>
import MessageThread from '@/components/notifications/MessageThread.vue'

export default {
  components: {
    'app-message-thread': MessageThread
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    }
  }
}
</script>

<style scoped>

.messages-section-container {
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 10px;
  height: 100%;
}

</style>
