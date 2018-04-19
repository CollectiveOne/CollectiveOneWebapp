<template lang="html">
  <div class="">

    <div v-if="notifications.length > 0" class="bell-button cursor-pointer w3-display-container"
      @click="showNotificationsClicked()">
      <i class="fa fa-circle circle"></i>
    </div>

    <div v-show="showTable"
      v-click-outside="clickOutsideNotifications"
      class="notifications-container w3-white w3-card-4 w3-bar-block w3-center">
      <app-activity-table :activities="activities"></app-activity-table>
      <button v-if="!allShown"
        id="T_showMoreButton"
        @click="showMore()"
        class="w3-margin-top w3-margin-bottom w3-button app-button-light" type="button" name="button">show more...</button>
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
    contextType: {
      type: String
    },
    contextElementId: {
      type: String
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
    activities () {
      return this.notifications.map(function (n) { return n.activity })
    },
    url () {
      return '/1/notifications/' + this.contextType + '/' + this.contextElementId
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
      if (!this.showingMoreNotifications) {
        this.notificationsRead()
      }
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

.bell-button {
  width: 30px;
  text-align: center;
}

.circle {
  color: rgb(249, 48, 48);
  font-size: 18px;
}

.notifications-container {
  width:420px;
  position: absolute;
  margin-left: -212px;
  max-height: calc(100vh - 80px);
  overflow-y: auto;
}

</style>
