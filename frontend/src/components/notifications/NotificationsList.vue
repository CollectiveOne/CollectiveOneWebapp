<template lang="html">
  <div class="">

    <div v-if="notifications.length > 0" class="bell-button cursor-pointer w3-display-container"
      @click="showNotificationsClicked()">
      <i class="fa fa-circle circle"></i>
    </div>

    <div v-show="showTable"
      v-click-outside="clickOutsideNotifications"
      class="notifications-container w3-white w3-card-4 w3-bar-block">
      <div class="w3-row-padding w3-border-bottom">
        <div class="w3-col s8 text-div">
          {{ notifications.length }} new events under {{ section.title }}
        </div>
        <button class="w3-col s4 w3-margin-top w3-margin-bottom w3-button app-button"
          @click="notificationsRead()">
          mark as read
        </button>
      </div>

      <app-activity-table :activities="activities"></app-activity-table>

      <div class="w3-row w3-center">
        <button v-if="!allShown"
          id="T_showMoreButton"
          @click="showMore()"
          class="w3-margin-top w3-margin-bottom w3-button app-button-light" type="button" name="button">
          show more...
        </button>
      </div>
    </div>

  </div>
</template>

<script>
import ActivityTable from './ActivityTable.vue'

export default {

  components: {
    'app-activity-table': ActivityTable
  },

  props: {
    section: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      showTable: false,
      notifications: [],
      currentPage: 0,
      showingMoreNotifications: false,
      allShown: false
    }
  },

  computed: {
    contextType () {
      return 'MODEL_SECTION'
    },
    contextElementId () {
      return this.section.id
    },
    activities () {
      return this.notifications.map(function (n) { return n.activity })
    },
    url () {
      return '/1/notifications/' + this.contextType + '/' + this.contextElementId
    },
    triggerUpdateNotifications () {
      return this.$store.state.support.triggerUpdateNotifications
    }
  },

  watch: {
    triggerUpdateNotifications () {
      /* only update if there is notifications to be removed */
      if (this.notifications.length > 0) {
        this.updateNotifications()
      }
    }
  },

  methods: {
    updateNotifications () {
      if (!this.showingMoreNotifications) {
        /* dont update if the user is scrolling down de notifications */
        this.axios.get(this.url, {
          params: {
            page: 0,
            size: 10
          }
        }).then((response) => {
          /* check that new notifications arrived */
          this.notifications = response.data.data
          this.allShown = this.notifications.length < 10

          /* send push notifications */
          this.$store.dispatch('addPushNotifications', this.notifications)
        }).catch(function (error) {
          console.log(error)
        })
      }
    },

    addNotifications () {
      this.showingMoreNotifications = true
      this.axios.get(this.url, {
        params: {
          page: this.currentPage,
          size: 10
        }
      }).then((response) => {
        /* check that new notifications arrived */
        if (response.data.data.length < 10) {
          this.allShown = true
        }

        this.notifications = this.notifications.concat(response.data.data)
      }).catch(function (error) {
        console.log(error)
      })
    },

    notificationsRead () {
      this.axios.put(this.url + '/read', {
        }).then((response) => {
          /* check that new notifications arrived */
          this.$store.commit('triggerUpdateNotifications')
          this.updateNotifications()
          this.hide()
        }).catch(function (error) {
          console.log(error)
        })
    },

    showMore () {
      this.currentPage += 1
      this.addNotifications()
    },

    clickOutsideNotifications () {
      // console.log('clickOutsideNotifications')
      if (!this.preventClickOutside) {
        if (this.showTable) {
          this.hide()
        }
      }
    },
    showNotificationsClicked () {
      if (!this.showTable) {
        this.show()
        this.preventClickOutside = true
        setTimeout(() => {
          this.preventClickOutside = false
        }, 500)
      } else {
        this.hide()
      }
    },
    hide () {
      this.showTable = false
      this.showingMoreNotifications = false
    },
    show () {
      this.showTable = true
    }
  },

  created () {
    this.updateNotifications()
  }
}
</script>

<style scoped>

.text-div {
  padding: 16px 12px;
  text-align: right;
}

.bell-button {
  width: 30px;
  text-align: center;
}

.circle {
  color: #b91414;
  font-size: 10px;
}

.notifications-container {
  width:420px;
  position: absolute;
  margin-left: -212px;
  max-height: calc(100vh - 80px);
  overflow-y: auto;
  z-index: 2;
}

</style>
