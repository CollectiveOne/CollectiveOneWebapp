<template lang="html">
  <div class="">

    <div v-if="!allShown && reverse" class="w3-row w3-center w3-margin-top">
      <button v-if="!loadingMore" @click="showMoreClick()"
        class="w3-button app-button-light" type="button" name="button">
        show older...
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
        :showMessages="showMessages"
        :contextElementId="contextElementId"
        @edit-message="$emit('edit-message', $event)"
        @reply-to-message="$emit('reply-to-message', $event)">
      </app-activity-table>
    </div>
    <div  v-if="activities.length === 0 && !loading" class="w3-padding">
      <i>no {{ onlyMessages ? 'messages' : 'activity' }} found</i>
    </div>
    <div v-if="loading" class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../../assets/loading.gif" alt="">
    </div>

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
import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'

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
    onlyMessages: {
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
      intervalIds: [],
      loading: false,
      loadingMore: false,
      connected: false
    }
  },

  watch: {
    triggerUpdate () {
      this.resetElements()
    },
    levels () {
      this.getActivity('RESET')
    },
    url () {
      this.getActivity('RESET')
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
          this.loading = true
          this.currentPage = 0
          askPage = this.currentPage
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

      this.axios.get(this.url, {
        params: {
          page: askPage,
          size: 10,
          onlyMessages: this.onlyMessages,
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

            /* start polling after the first response */
            // if (this.polling) {
            //   if (this.intervalIds.length === 0) {
            //     this.intervalIds.push(setInterval(() => {
            //       this.getActivity('UPDATE')
            //     }, 5000))
            //   }
            // }
            break

          case 'OLDER':
            this.activities = this.activities.concat(response.data.data.content)
            break

          case 'UPDATE':
            this.remapActivities(response)
            break
        }
      })
    },
    remapActivities (response) {
      var newIx = 0

      if (this.activities.length > 0) {
        var latestId = this.activities[0].id
        if (response.data.data.content.length > 0) {
          if (response.data.data.content[newIx].id) {
            while (response.data.data.content[newIx].id !== latestId) {
              this.activities.splice(newIx, 0, response.data.data.content[newIx])
              newIx++
            }
            if (newIx > 0) {
              this.$emit('updated')
            }
          }
        }
      }
    },
    connect () {
      this.socket = new SockJS(process.env.WEBSOCKET_SERVER_URL)
      this.stompClient = Stomp.over(this.socket)
      this.stompClient.connect(
        {},
        frame => {
          this.connected = true
          this.stompClient.subscribe('/topic/messages/' + this.contextElementId, tick => {
            var response = {data: JSON.parse(tick.body)}
            // console.log(response)
            if (response.data.data.content.length > 0) {
              this.$emit('last-activity', response.data.data.content[0])
            }
            if (response.data.data.content.length < 10) {
              this.allShown = true
            }
            if (response.data.data.content.length === 10) {
              this.allShown = false
            }

            this.remapActivities(response)
          })
        },
        error => {
          console.log(error)
          this.connected = false
        }
      )
    },
    disconnect () {
      if (this.stompClient) {
        this.stompClient.disconnect()
      }
      this.connected = false
    },
    tickleConnection () {
      this.connected ? this.disconnect() : this.connect()
    }
  },

  created () {
    this.getActivity('RESET')
    this.connect()
  },

  beforeDestroy () {
    this.intervalIds.forEach((id) => {
      clearInterval(id)
    })
  }

}
</script>

<style scoped>
</style>
