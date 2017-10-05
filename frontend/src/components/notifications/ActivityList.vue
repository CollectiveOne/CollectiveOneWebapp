<template lang="html">
  <div class="">
    <div class="w3-row">
      <b>Activity:</b>
    </div>
    <div v-for="act in activity" class="w3-row">
      <app-activity-box :activity="act"></app-activity-box>
    </div>
    <div class="w3-row w3-center">
      <button
        @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">show more...</button>
    </div>
  </div>
</template>

<script>
import ActivityBox from '@/components/notifications/ActivityBox.vue'

export default {
  props: {
    url: {
      type: String
    }
  },

  data () {
    return {
      activity: [],
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
        this.activity = this.activity.concat(response.data.data.content)
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
