<template lang="html">
  <div class="">
    <div v-if="$store.state.support.userEmailNotVerified" class="w3-row w3-center">
      <h4>
        <p>
          It seems that you have not verified your email yet. Please confirm it
          to get access to CollectiveOne.
        </p>
      </h4>
      <button class="w3-button app-button" @click="logout()">logout</button>
    </div>
    <div v-else class="w3-row">
      <app-landing-design></app-landing-design>
    </div>

  </div>
</template>

<script>
import LandingDesign from '@/components/landing/LandingDesign.vue'

export default {
  components: {
    'app-landing-design': LandingDesign
  },

  methods: {
    logout () {
      this.$store.dispatch('logoutUser')
      window.location.href = '/'
    }
  }
}
</script>

<style scoped>

.navbar {
}

.logo {
}

.welcome-header {
  background-color: rgb(247, 247, 247);
  padding-top: 195px;
  height: 500px;
  text-align: center;
}

.welcome-header button {
  width: 150px;
}

</style>
