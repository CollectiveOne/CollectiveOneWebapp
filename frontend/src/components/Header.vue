<template lang="html">
  <div class="this-container">

    <!--  Output Messages -->
    <app-output-message></app-output-message>

    <!-- Navbar -->
    <div class="w3-top header-div">
     <div class="w3-bar w3-left-align w3-large">
      <div
        class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-padding-large w3-hover-white w3-large">
        <i class="fa fa-bars"></i>
      </div>
      <div
        class="w3-bar-item w3-button w3-left w3-padding-large w3-large"
        @click="$emit('expand-nav')">
        <i class="fa fa-bars"></i>
      </div>
      <a href="#" class="w3-display-middle left w3-bar-item noselect">
        <img class="logo" src="../assets/Logo-Dark.png" alt="">
      </a>

      <div v-if="$store.state.user.profile" class="w3-dropdown-hover w3-hide-small w3-right">
        <div @click="userOptionsClicked()"  class="w3-right">
          <div class="avatar-img-container w3-left">
            <img :src="$store.state.user.profile.pictureUrl" class="logged-avatar w3-circle noselect">
          </div>
          <div class="logged-nickname w3-left noselect">
              {{ $store.state.user.profile.nickname }}
          </div>
        </div>
        <div v-if="showUserOptions" class="avatar-dropdown-content w3-card-4 w3-bar-block w3-white" style="width:200px">
          <div @click="goHome()" class="w3-bar-item w3-button">home</div>
          <div @click="logoutUser()" class="w3-bar-item w3-button">logout</div>
        </div>
      </div>

      <div class="w3-hide-small w3-right">
        <app-notifications-list
          :show="showActivityList"
          @icon-clicked="activityClicked()">
        </app-notifications-list>
      </div>

     </div>
    </div>

    <!-- Navbar on small screens -->
    <div id="navDemo" class="w3-bar-block w3-theme-d2 w3-hide w3-hide-large w3-hide-medium w3-large">
      <a href="#" class="w3-bar-item w3-button w3-padding-large">Link 1</a>
      <a href="#" class="w3-bar-item w3-button w3-padding-large">Link 2</a>
      <a href="#" class="w3-bar-item w3-button w3-padding-large">Link 3</a>
      <a href="#" class="w3-bar-item w3-button w3-padding-large">My Profile</a>
    </div>
  </div>
</template>

<script>
import OutputMessage from '@/components/OutputMessage.vue'
import NotificationsList from '@/components/notifications/NotificationsList.vue'

import { mapActions } from 'vuex'

export default {
  components: {
    'app-output-message': OutputMessage,
    'app-notifications-list': NotificationsList
  },

  methods: {
    ...mapActions(['logoutUser', 'updateNotifications']),

    userOptionsClicked () {
      this.showActivityList = false
      this.showUserOptions = !this.showUserOptions
    },
    activityClicked () {
      this.showUserOptions = false
      if (this.showActivityList) {
        this.updateNotifications()
      }
      this.showActivityList = !this.showActivityList
    },
    goHome () {
      this.showUserOptions = false
      this.$router.push('/inits')
    }
  },

  data () {
    return {
      showActivityList: false,
      showUserOptions: false
    }
  },

  computed: {
  }
}
</script>

<style scoped>

.header-div {
  height: 65px;
}

.w3-bar {
}

.logo {
  height: 25px;
}

.logged-nickname {
  padding-top: 10px;
  margin-right: 20px;
}

.avatar-img-container {
  margin-right: 15px;
  padding: 6px;
}

.avatar-img-container img {
  width: 37px;
}

.avatar-dropdown-content {
  position: absolute;
  margin-left: -20px;
  margin-top: 51px;
}

.logo {
}

</style>
