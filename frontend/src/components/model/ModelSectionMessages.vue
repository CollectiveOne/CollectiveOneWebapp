<template lang="html">
  <div class="card-messages-container">
    <app-message-thread
      contextType="MODEL_SECTION"
      :contextElementId="currentSectionId"
      :onlyMessagesInit="true"
      :levels="levels"
      :url="'/1/activity/model/section/' + currentSectionId">
    </app-message-thread>
  </div>
</template>

<script>
import MessageThread from '@/components/notifications/MessageThread'

export default {
  components: {
    'app-message-thread': MessageThread
  },
  computed: {
    currentSectionId () {
      return this.$route.params.sectionId
    },
    levels () {
      return this.$route.query.levels ? parseInt(this.$route.query.levels) : 1
    }
  }
}
</script>

<style scoped>

.card-messages-container {
  padding: 12px 12px;
}

</style>
