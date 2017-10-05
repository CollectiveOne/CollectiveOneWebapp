<template lang="html">
  <table class="w3-table w3-bordered">
    <app-activity-row v-for="activity in activities"
      :key="activity.id"
      :activity="activity"
      :addContext="addContext">
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
    }
  }
}
</script>

<style scoped>
</style>
