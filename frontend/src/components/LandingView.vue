<template lang="html">
  <div class="">
    <div class="welcome-header">
      <div v-if="$store.state.support.userEmailNotVerified" class="">
        <h4>
          <p>
            It seems that you have not verified your email yet. Please confirm it
            to get access to CollectiveOne.
          </p>
        </h4>
        <button class="w3-button app-button" @click="logout()">logout</button>
      </div>
      <div v-else class="">
        <h1>Welcome to CollectiveOne</h1>
        <button class="w3-button app-button" @click="login()">login / signup</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  methods: {
    login () {
      this.$store.state.user.lock.show()
    },
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
