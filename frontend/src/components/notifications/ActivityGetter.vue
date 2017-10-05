<template lang="html">
  <div class="">
    <app-activity-table
      :activities="activities"
      :addContext="false">
    </app-activity-table>
    <div class="w3-row w3-center w3-margin-top">
      <button
        @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">show more...</button>
    </div>
  </div>
</template>

<script>
import ActivityTable from '@/components/notifications/ActivityTable.vue'

export default {
  components: {
    'app-activity-table': ActivityTable
  },
  props: {
    url: {
      type: String
    }
  },

  data () {
    return {
      activities: [],
      currentPage: 0
    }
  },

  methods: {
    showMoreClick () {
      this.currentPage += 1
      this.getMore()
    },
    getMore () {
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10
        }
      }).then((response) => {
        this.activities = this.activities.concat(response.data.data.content)
      })
    }
  },

  created () {
    this.getMore()
  }
}
</script>

<style scoped>
</style>
