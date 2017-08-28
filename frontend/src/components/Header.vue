<template lang="html">
  <div class="this-container">

    <!--  Output Messages -->
    <app-output-message></app-output-message>

    <!-- Navbar -->
    <div class="w3-top">
     <div class="w3-bar header-div w3-border-bottom w3-white">
      <div class="w3-bar-item w3-button w3-xlarge flex-vert bars-div"
        @click="$emit('expand-nav')">
        <i class="fa fa-bars"></i>
      </div>
      <router-link :to="{name: 'InitiativesHome'}" class="logo-container w3-bar-item noselect cursor-pointer">
        <img class="logo w3-hide-small" src="../assets/logo-color.png" alt="">
        <img class="icon w3-hide-medium w3-hide-large" src="../assets/imago-red.png" alt="">
      </router-link>

      <div v-if="$store.state.user.profile" class="w3-right">
        <div @click="userOptionsClicked()"  class="w3-bar-item w3-right w3-button">
          <div class="avatar-img-container w3-left">
            <img :src="$store.state.user.profile.pictureUrl" class="logged-avatar w3-circle noselect">
          </div>
          <div class="logged-nickname noselect w3-left  w3-hide-medium w3-hide-small">
            {{ $store.state.user.profile.nickname }}
          </div>
        </div>
        <div v-if="showUserOptions" class="avatar-dropdown-content w3-card-2 w3-bar-block w3-white w3-large">
          <div @click="goHome()" class="w3-bar-item w3-button"><i class="fa fa-home" aria-hidden="true"></i>home</div>
          <div @click="logoutUser()" class="w3-bar-item w3-button"><i class="fa fa-power-off" aria-hidden="true"></i>logout</div>
        </div>
      </div>

      <div class="nots-div w3-bar-item w3-right w3-button w3-xlarge">
        <app-notifications-list
          :show="showActivityList"
          @icon-clicked="activityClicked()">
        </app-notifications-list>
      </div>

     </div>
    </div>

  </div>
</template>

<script>
import OutputMessage from '@/components/OutputMessage.vue'
import NotificationsList from '@/components/notifications/NotificationsList.vue'

export default {
  components: {
    'app-output-message': OutputMessage,
    'app-notifications-list': NotificationsList
  },

  methods: {
    userOptionsClicked () {
      this.showActivityList = false
      this.showUserOptions = !this.showUserOptions
    },
    activityClicked () {
      this.showUserOptions = false
      if (this.showActivityList) {
        this.$store.dispatch('updateNotifications')
      }
      this.showActivityList = !this.showActivityList
    },
    goHome () {
      this.showUserOptions = false
      this.$router.push({name: 'InitiativesHome'})
    },
    logoutUser () {
      this.$store.dispatch('logoutUser')
      this.$router.push({ name: 'Landing' })
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

.w3-bar .w3-bar-item {
  height: 100%;
}

.bars-div {
  width: 70px;
}

.logo-container {
  padding-top: 0px;
}

.logo {
  margin-top: 16px;
  height: 28px;
}

.icon {
  margin-top: 18px;
  height: 32px;
}

.nots-div {
  padding-top: 3px !important;
  padding-bottom: 0px !important;
}

.logged-nickname {
  height: 100%;
  padding-top: 10px;
  font-size: 18px;
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
  width: 150px;
  margin-left: 0px;
  margin-top: 65px;
}

.avatar-dropdown-content .fa {
  margin-right: 10px;
}

.logo {
}

</style>
