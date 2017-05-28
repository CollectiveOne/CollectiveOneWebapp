<template lang="html">
  <div class="this-container">


    <!--  Output Messages -->
    <app-output-message></app-output-message>

    <!-- Navbar -->
    <div class="w3-top">
     <div class="w3-bar w3-theme-d2 w3-left-align w3-large">
      <a class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-padding-large w3-hover-white w3-large w3-theme-d2" href="javascript:void(0);" onclick="openNav()"><i class="fa fa-bars"></i></a>
      <div
        class="w3-bar-item w3-button w3-left w3-padding-large w3-large w3-theme-d2"
        @click="$emit('expand-nav')">
        <i class="fa fa-bars"></i>
      </div>
      <a href="#" class="w3-display-middle left w3-bar-item">
        <img class="logo" src="../assets/Logo-Light.png" alt="">
      </a>

      <div class="w3-dropdown-hover w3-hide-small w3-right">
        <div class="avatar-img-container">
          <img :src="loggedUserPicture" class="logged-avatar w3-circle">
        </div>
        <div class="avatar-dropdown-content w3-dropdown-content w3-card-4 w3-bar-block" style="width:200px">
          <div @click="logoutUser()" class="w3-bar-item w3-button">logout</div>
        </div>
      </div>

      <div class="w3-dropdown-hover w3-hide-small w3-right">
        <button class="w3-button w3-padding-large" title="Notifications"><i class="fa fa-bell"></i><span class="w3-badge w3-right w3-small w3-green">0</span></button>
        <div class="w3-dropdown-content w3-card-4 w3-bar-block" style="width:300px">
        </div>
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
import { mapGetters, mapActions } from 'vuex'

export default {
  components: {
    AppOutputMessage: OutputMessage
  },

  methods: {
    ...mapGetters(['loggedUserNickname']),
    ...mapActions(['logoutUser'])
  },

  computed: {
    loggedUserPicture () {
      if (this.$store.state.user.profile) {
        return this.$store.state.user.profile.pictureUrl
      } else {
        return ''
      }
    }
  }
}
</script>

<style scoped>

.logo {
  height: 20px;
}

.avatar-img-container {
  margin-right: 15px;
  padding: 6px;
}

.avatar-img-container img {
  width: 37px;
}

.avatar-dropdown-content {
  margin-left: -130px;
}

.logo {
}

</style>
