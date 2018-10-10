<template lang="html">
  <table class="w3-table" :class="{'w3-bordered': addBorders}">
    <app-activity-row v-for="activity in activitiesOrdered"
      :key="activity.id"
      :activity="activity"
      :addInAppState="addInAppState"
      :addContext="addContext"
      :showMessagesText="showMessagesText"
      :contextElementId="contextElementId"
      @edit-message="$emit('edit-message', $event)"
      @reply-to-message="$emit('reply-to-message', $event)"
      @reset-activity="$emit('reset-activity')">
    </app-activity-row>
  </table>
</template>

<script>
import ActivityRow from '@/components/notifications/ActivityRow.vue'

export default {
  components: {
    'app-activity-row': ActivityRow
  },

  props: {
    activities: {
      type: Array
    },
    addContext: {
      type: Boolean,
      default: true
    },
    reverse: {
      type: Boolean,
      default: false
    },
    addBorders: {
      type: Boolean,
      default: true
    },
    showMessagesText: {
      type: Boolean,
      default: false
    },
    contextElementId: {
      type: String,
      default: ''
    },
    addInAppState: {
      type: Boolean,
      default: false
    }
  },

  computed: {
    activitiesOrdered () {
      if (this.reverse) {
        return this.activities.slice().reverse()
      } else {
        return this.activities.slice()
      }
    }
  },

  methods: {
  }
}
</script>

<style scoped>
</style>
