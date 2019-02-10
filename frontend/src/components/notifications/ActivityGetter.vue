<template lang="html">
  <div ref="container" class="activity-getter">

    <div v-if="!allShown && reverse" class="w3-row w3-center w3-margin-top">
      <button v-if="!loadingMore" @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">
        {{ $t('notifications.SHOW_OLDER') }}...
      </button>
      <div v-else="loadingMore" class="w3-row w3-center loader-gif-container-small">
        <img class="loader-gif-small" src="../../assets/loading.gif" alt="">
      </div>
    </div>

    <div v-if="activities.length > 0 && !loading" class="">
      <app-activity-table
        :activities="activities"
        :addContext="false"
        :reverse="reverse"
        :addBorders="addBorders"
        :showMessagesText="showMessagesText"
        :contextElementId="contextElementId"
        :fullWidthCard="false"
        :showInSection="contextType !== 'MODEL_CARD'"
        :showCardsPreview="contextType === 'MODEL_SECTION'"
        @edit-message="$emit('edit-message', $event)"
        @reply-to-message="$emit('reply-to-message', $event)"
        @reset-activity="getActivity('RESET')">
      </app-activity-table>
    </div>
    <div  v-if="activities.length === 0 && !loading" class="w3-padding">
      <i>{{ $t('notifications.NOTHING_FOUND') }}</i>
    </div>
    <div v-if="loading" class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../../assets/loading.gif" alt="">
    </div>

    <div v-if="!allShown && !reverse" class="w3-row w3-center w3-margin-top">
      <button @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">
        {{ $t('notifications.SHOW_OLDER') }}...
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
    reverse: {
      type: Boolean,
      default: false
    },
    triggerUpdate: {
      type: Boolean,
      default: false
    },
    triggerRefresh: {
      type: Boolean,
      default: false
    },
    triggerShowmore: {
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
    showMessages: {
      type: Boolean,
      defaul: true
    },
    showEvents: {
      type: Boolean,
      defaul: true
    },
    contextType: {
      type: String,
      default: ''
    },
    contextElementId: {
      type: String,
      default: ''
    },
    levels: {
      type: Number,
      default: 1
    }
  },

  data () {
    return {
      activities: [],
      currentPage: 0,
      allShown: false,
      loading: false,
      loadingMore: false,
      subscription: null
    }
  },

  watch: {
    triggerUpdate () {
      this.getActivity('RESET')
    },
    triggerRefresh () {
      this.getActivity('UPDATE')
    },
    triggerShowmore () {
      this.showMoreClick()
    },
    levels () {
      this.getActivity('RESET')
    },
    contextElementId () {
      this.getActivity('RESET')
    },
    '$store.state.socket.connected' () {
      this.subscribeSocket()
    },
    showMessages () {
      this.getActivity('RESET')
    },
    showEvents () {
      this.getActivity('RESET')
    }
  },

  methods: {
    showMoreClick () {
      this.getActivity('OLDER')
    },
    getActivity (mode) {
      var askPage = 0

      switch (mode) {
        case 'RESET':
          this.loading = true
          this.currentPage = 0
          askPage = this.currentPage
          this.subscribeSocket()
          break

        case 'OLDER':
          this.loadingMore = true
          this.currentPage += 1
          askPage = this.currentPage
          break

        case 'UPDATE':
          askPage = 0
          break
      }

      let url = ''
      switch (this.contextType) {
        case 'MODEL_CARD':
          url = '/1/activity/model/card/' + this.contextElementId
          break

        case 'MODEL_SECTION':
          url = '/1/activity/model/section/' + this.contextElementId
          break
      }

      this.axios.get(url, {
        params: {
          page: askPage,
          size: 10,
          addMessages: this.showMessages,
          addEvents: this.showEvents,
          levels: this.levels
        }
      }).then((response) => {
        this.loading = false
        this.loadingMore = false

        if (response.data.data.content.length > 0) {
          this.$emit('last-activity', response.data.data.content[0])
        }
        if (response.data.data.content.length < 10) {
          this.allShown = true
        }
        if (response.data.data.content.length === 10) {
          this.allShown = false
        }

        switch (mode) {
          case 'RESET':
            this.activities = response.data.data.content
            this.$emit('updated')
            break

          case 'OLDER':
            this.activities = this.activities.concat(response.data.data.content)
            let currentHeight = this.$refs.container.offsetHeight
            this.$emit('added-older', { height: currentHeight })
            break

          case 'UPDATE':
            this.checkIfNewActivities(response.data.data.content)
            break
        }
      })
    },
    checkIfNewActivities (activityList) {
      var newIx = 0

      if (this.activities.length > 0) {
        var latestId = this.activities[0].id
        if (activityList.length > 0) {
          if (activityList[newIx].id) {
            while (activityList[newIx].id !== latestId) {
              this.activities.splice(newIx, 0, activityList[newIx])
              newIx++
            }
            if (newIx > 0) {
              this.$emit('updated')
            }
          }
        }
      } else {
        this.activities = activityList
      }
    },
    subscribeSocket () {
      let url = ''
      switch (this.contextType) {
        case 'MODEL_CARD':
          url = '/channel/activity/model/card/' + this.contextElementId
          break

        case 'MODEL_SECTION':
          url = '/channel/activity/model/section/' + this.contextElementId
          break
      }

      if (this.subscription !== null) {
        this.$store.dispatch('unsubscribe', this.subscription)
      }

      this.subscription = this.$store.dispatch('subscribe', {
        url: url,
        onMessage: (tick) => {
          var message = tick.body
          if (message === 'UPDATE') {
            this.getActivity('UPDATE')
          }
        }
      })
    }
  },

  created () {
    this.getActivity('RESET')
  },

  beforeDestroy () {
    if (this.subscription) {
      this.$store.dispatch('unsubscribe', this.subscription)
    }
  }

}
</script>

<style scoped>
</style>
