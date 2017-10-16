<template lang="html">
  <div class="">

    <div v-if="!allShown && reverse" class="w3-row w3-center w3-margin-top">
      <button @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">
        show older...
      </button>
    </div>

    <app-activity-table
      :activities="activities"
      :addContext="false"
      :reverse="reverse">
    </app-activity-table>

    <div v-if="!allShown && !reverse" class="w3-row w3-center w3-margin-top">
      <button @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">
        show older...
      </button>
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
    },
    reverse: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      activities: [],
      currentPage: 0,
      allShown: false
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
        if (response.data.data.content < 10) {
          this.allShown = true
        }
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
