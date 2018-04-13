<template lang="html">
  <div class="this-container">

    <!--  Output Messages -->
    <app-output-message></app-output-message>

    <!-- Navbar -->
    <div class="w3-top">
     <div class="w3-bar header-div w3-border-bottom w3-white">
       <router-link :to="{name: 'InitiativesHome'}" class="logo-container w3-bar-item noselect cursor-pointer">
         <img class="logo w3-hide-small" src="../assets/logo-color.png" alt="">
         <img class="icon w3-hide-medium w3-hide-large" src="../assets/imago-red.png" alt="">
       </router-link>
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
    login () {
      this.$store.state.user.lock.show()
    },
    userOptionsClicked () {
      this.showUserOptions = !this.showUserOptions
    },
    clickOutsideUser () {
      this.showUserOptions = false
    },
    goMyProfile () {
      this.showUserOptions = false
      this.$router.push({ name: 'UserProfilePage', params: { userId: this.$store.state.user.profile.c1Id } })
    },
    goHome () {
      this.showUserOptions = false
      this.$router.push({name: 'InitiativesHome'})
    },
    logoutUser () {
      this.$store.dispatch('logoutUser')
    }
  },

  data () {
    return {
      showActivityList: false,
      showUserOptions: false
    }
  },

  computed: {
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
    },
    expandNav () {
      return this.$store.state.support.expandNav
    }
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
  height: 100% !important;
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
  margin-top: 58px;
}

.left-align-1 {
  margin-left: -80px;
}

.left-align-2 {
  margin-left: 0px;
}

.avatar-dropdown-content .fa {
  margin-right: 10px;
}

.logo {
}

.login-button-container {
  padding-top: 13px;
}

</style>
