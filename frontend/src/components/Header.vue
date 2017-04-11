<template lang="html">
  <div class="">
    <app-navbar></app-navbar>
    <b-modal id="loginModal" :hide-header="true" :hide-footer="true">
      <app-login-control @userLogged="userLogged()"></app-login-control>
    </b-modal>
    <b-modal id="cbtionNewModal" :hide-header="true" :hide-footer="true">
      <app-new-cbtion @create="cbtionSubmitted($event)"></app-new-cbtion>
    </b-modal>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import LoginControl from '@/components/LoginControl.vue'
import CbtionNewForm from '@/components/CbtionNewForm.vue'

export default {
  methods: {
    userLogged () {
      this.$root.$emit('hide::modal', 'loginModal')
    },

    cbtionSubmitted (cbtion) {
    }
  },

  components: {
    appNavbar: Navbar,
    appLoginControl: LoginControl,
    appNewCbtion: CbtionNewForm
  }
}
</script>

<style scoped>

</style>
