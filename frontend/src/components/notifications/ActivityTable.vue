<template lang="html">
  <table class="w3-table" :class="{'w3-bordered': addBorders}">
    <app-activity-row v-for="activity in activitiesOrdered"
      :key="activity.id"
      :activity="activity"
      :addContext="addContext"
      :showMessages="showMessages"
      :contextElementId="contextElementId">
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
    showMessages: {
      type: Boolean,
      default: false
    },
    contextElementId: {
      type: String,
      default: ''
    }
  },

  computed: {
    activitiesOrdered () {
      if (this.reverse) {
        return JSON.parse(JSON.stringify(this.activities)).reverse()
      } else {
        return JSON.parse(JSON.stringify(this.activities))
      }
    }
  },

  methods: {
  }
}
</script>

<style scoped>
</style>
