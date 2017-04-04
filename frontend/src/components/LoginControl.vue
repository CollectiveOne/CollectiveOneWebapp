<template lang="html">
    <form @submit.stop.prevent="loginSubmit">
      <h1>Login</h1>
      <br>
      <b-form-input type="text" placeholder="username" v-model="username"></b-form-input>
      <b-form-input type="password" placeholder="password" v-model="password"></b-form-input>
      <br>
      <b-button>login</b-button>
      <br>
      <br>
      <b-alert :show="wrongUser" variant="danger">wrong username or password</b-alert>
    </form>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  data () {
    return {
      username: '',
      password: '',
      wrongUser: false
    }
  },

  methods: {
    ...mapActions(['setUserLogged']),

    loginSubmit () {
      this.axios.post('/1/user/login',
        {
          username: this.username,
          password: this.password
        }
      ).then((response) => {
        if (response.data !== '') {
          this.wrongUser = false
          this.$emit('userLogged')
          this.setUserLogged(response.data)
        } else {
          this.wrongUser = true
        }
      })
    }
  }
}
</script>

<style lang="css">
</style>
