<template lang="html">
  <div class="">
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
      return parseInt(this.$route.query.levels)
    }
  }
}
</script>

<style lang="css">
</style>
