<template lang="html">
  <div class="">
    <app-navbar></app-navbar>
    <b-modal id="loginModal" :hide-header="true" :hide-footer="true">
      <app-login-control @userLogged="userLogged()"></app-login-control>
    </b-modal>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import LoginControl from '@/components/LoginControl.vue'

export default {
  methods: {
    userLogged () {
      this.$root.$emit('hide::modal', 'loginModal')
    }
  },

  components: {
    appNavbar: Navbar,
    appLoginControl: LoginControl
  }
}
</script>

<style scoped>

</style>
