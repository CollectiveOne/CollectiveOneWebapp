<template lang="html">
  <div class="">
    <h1>User Page</h1>
    <p>{{ userData.id }}</p>
    <p>{{ userData.username }}</p>
    <p>{{ userData.profile }}</p>
    <button @click="getSuggestions()">get suggestions</button>
    <br>
    <br>
    <p v-for="sug in userSuggestions">{{ sug }}</p>
  </div>
</template>

<script>
export default {
  data () {
    return {
      userData: {
        id: 0,
        username: 'username',
        profile: 'profile'
      },
      userSuggestions: []
    }
  },

  methods: {
    update () {
      this.axios.get('/1/user/' + this.$route.params.username,
        {
          headers: {'Authorization': 'Basic ' + btoa('user:password1')}
        },
      ).then((response) => {
        this.userData = response.data
      })
    },

    getSuggestions () {
      this.axios.get('/1/users/suggestions?query=u',
        {
          headers: {'Authorization': 'Basic ' + btoa('user:password1')}
        },
      ).then((response) => {
        this.userSuggestions = response.data.suggestions
      })
    }
  },

  watch: {
    '$route' (to, from) {
      this.update()
    }
  },

  created () {
    this.update()
  }
}
</script>

<style lang="css">
</style>
