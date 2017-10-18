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
      :reverse="reverse"
      :addBorders="addBorders"
      :showMessages="showMessages"
      :contextElementId="contextElementId">
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
    },
    triggerUpdate: {
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
    polling: {
      type: Boolean,
      default: false
    },
    contextElementId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      activities: [],
      currentPage: 0,
      allShown: false,
      intervalId: null
    }
  },

  watch: {
    triggerUpdate () {
      this.resetElements()
    }
  },

  methods: {
    resetElements () {
      this.getActivity('RESET')
    },
    showMoreClick () {
      this.getActivity('OLDER')
    },
    getActivity (mode) {
      var askPage = 0

      switch (mode) {
        case 'RESET':
          this.currentPage = 0
          askPage = this.currentPage
          break

        case 'OLDER':
          this.currentPage += 1
          askPage = this.currentPage
          break

        case 'UPDATE':
          askPage = 0
          break
      }

      this.axios.get(this.url, {
        params: {
          page: askPage,
          size: 10
        }
      }).then((response) => {
        if (response.data.data.content.length < 10) {
          this.allShown = true
        }

        switch (mode) {
          case 'RESET':
            this.activities = response.data.data.content
            this.$emit('updated')

            /* start polling after the first response */
            if (this.polling) {
              if (this.intervalId === null) {
                this.intervalId = setInterval(() => {
                  this.getActivity('UPDATE')
                }, 5000)
              }
            }
            break

          case 'OLDER':
            this.activities = this.activities.concat(response.data.data.content)
            break

          case 'UPDATE':
            var newIx = 0
            var latestId = this.activities[0].id

            while (response.data.data.content[newIx].id !== latestId) {
              this.activities.splice(newIx, 0, response.data.data.content[newIx])
              newIx++
            }
            if (newIx > 0) {
              this.$emit('updated')
            }
            break
        }
      })
    }
  },

  created () {
    this.getActivity('RESET')
  },

  beforeDestroy () {
    clearInterval(this.intervalId)
  }

}
</script>

<style scoped>
</style>
