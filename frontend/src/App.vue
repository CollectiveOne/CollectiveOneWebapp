<template>
  <div class="container-fluid">
    <router-view></router-view>
  </div>
</template>

<script>
import Auth0Lock from 'auth0-lock'
import NewInitiativeModal from '@/components/modal/NewInitiativeModal.vue'

export default {
  name: 'app',

  methods: {
  },

  components: {
    AppNewInitiativeModal: NewInitiativeModal
  },

  created () {
    var options = {
      auth: {
        state: this.$route.path,
        responseType: 'token',
        params: {
          connectionScopes: {
            connectionName: [ 'openid', 'user_metadata', 'app_metadata', 'picture' ]
          }
        }
      },
      theme: {
        logo: 'https://image.ibb.co/bTQ9Ev/logo_color_auth0.png',
        primaryColor: '#bc1c34'
      },
      languageDictionary: {
        title: 'welcome'
      }
    }

    var lock = new Auth0Lock(process.env.AUTH0_CLIENT_ID, process.env.AUTH0_DOMAIN, options)

    lock.on('authenticated', (authResult) => {
      localStorage.setItem('access_token', authResult.accessToken)
      localStorage.setItem('id_token', authResult.idToken)
      this.$store.commit('authenticate', !!localStorage.getItem('id_token'))
      this.$store.commit('setAuth0state', authResult.state)
      this.$store.dispatch('updateProfile')
      this.$router.push({ name: 'Root' })
    })

    lock.on('authorization_error', (error) => {
      console.log('error authenticating')
      console.log(error)
    })

    /* check if user is authenticated */
    this.$store.commit('setLock', lock)
    this.$store.commit('authenticate', !!localStorage.getItem('id_token'))
    this.$store.dispatch('updateProfile')
  }
}
</script>

<style>

@import '~simplemde/dist/simplemde.min.css';

.CodeMirror, .CodeMirror-scroll {
	min-height: 30px;
  resize: vertical;
}

.CodeMirror a {
  text-decoration: underline !important;
}

.marked-text a {
  text-decoration: underline !important;
}

.marked-text p {
  margin-top: 3px;
  margin-bottom: 3px;
}

#app html,body,h1,h2,h3,h4,h5 {
  font-family: 'Raleway', sans-serif;
}
tbody tr {
  height: 80px !important;
}

tfoot tr {
  height: 60px !important;
  font-weight: bold;
}

td {
  vertical-align: middle !important;
}

textarea {
  resize: vertical;
}

a {
  text-decoration: none;
}

select {
  padding-left: 10px !important;
}

.w3-modal {
  padding-top: 30px;
  display: block;
  z-index: 100;
}

.w3-modal-content {
  margin-top: 50px;
  margin-bottom: 50px;
  border-radius: 20px;
  min-height: 200px;
}

.w3-modal-content .w3-card-4 {
  border-radius: 20px;
  min-height: 200px;
}

.w3-modal .close-div {
  margin-right: 25px;
  margin-top: 20px;
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.w3-modal .close-div:hover {
  color: #15a5cc;
}

.w3-modal .bottom-btns-row button {
  width: 100%;
}

.white-bg {
  background-color: white;
}

.dark-gray {
  background-color: #313942;
  color: white;
}

.dark-gray-selected {
  background-color: #3e464e;
  color: white;
}

.gray-1 {
  background-color: #637484;
  color: white;
}

.gray-1-color {
  color: #637484;
}

.gray-1-border {
  border-color: #637484 !important;
}

.gray-2 {
  background-color: #6f8294;
}

.gray-2-color {
  color: #6f8294;
}

.light-grey {
  background-color: #eff3f6;
  color: black;
}

.light-grey-d1 {
  background-color: #dbdee0;
  color: black;
}

.light-grey-color {
  color: #eff3f6;
}

.white-text {
  color: white;
}

.border-blue {
  border-color: #15a5cc !important;
}

.app-blue {
  background-color: #95bad6 !important;
  color: #313942;
}

.app-button {
  background-color: #15a5cc !important;
  color: white;
  border-radius: 12px;
}

.app-button-color {
  color: #15a5cc !important;
}

.app-button-light {
  background-color: #f1f1f1 !important;
  color: black;
  border-radius: 12px;
}

.app-button-danger {
  background-color: #bc1c34 !important;
  color: white;
  border-radius: 12px;
}

.black-text {
  color: black;
}

.success-panel {
  color: white;
  background-color:  #40a36d !important;
}

.warning-panel {
  color: white;
  background-color:  #c29d00 !important;
}

.error-row {
  margin-top: 10px;
  width: 100%;
}

.error-panel {
  color: black;
  background-color: #f1cdcd;
  margin-bottom: 6px;
}

.error-text {
  color: rgb(255, 64, 64);
}

.error-input {
  outline: none;
  border-color: #f1cdcd !important;
  box-shadow: 0 0 10px red;
}

.noselect {
  -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Safari */
     -khtml-user-select: none; /* Konqueror HTML */
       -moz-user-select: none; /* Firefox */
        -ms-user-select: none; /* Internet Explorer/Edge */
            user-select: none; /* Non-prefixed version, currently
                                  supported by Chrome and Opera */
}

.flex-vert {
  display: flex !important;
  justify-content: center !important;
  flex-direction: column !important;
}

.slider-container {
  overflow: hidden;
}

.cursor-pointer {
  cursor: pointer !important;
}

.open-color {
  background-color: #32a11b;
}

.done-color {
  background-color: #b8b8b8;
}

.revert-color {
  background-color: #ff4b4b;
}

.section-header {
  text-align: left;
  margin-left: 16px;
}

.model-action-button {
  background-color: rgba(239, 243, 246, 0.65) !important;
}

.div-close-modal {
  width: 70px;
  height: 70px;
  cursor: pointer;
  text-align: right;
}

.fa-close-modal {
  margin-right: 20px;
  margin-top: 20px;
}

.div-modal-content {
  padding-top: 20px;
  padding-bottom: 25px;
}

.modal-bottom-btns-row button {
  width: 100%;
}

.app-margin-left {
  margin-left: 10px;
}

.app-margin-right {
  margin-right: 10px;
}

.app-margin-bottom {
  margin-bottom: 10px;
}

.vdp-datepicker input {
  width: 100%;
  padding-top: 8px;
  padding-bottom: 8px;
  padding-left: 10px;
}

.loader-gif-container {
  padding-top: 100px;
}

.loader-gif {
  width: 100px;
}

.plus-button {
  margin-right: 25px;
  margin-top: 15px;
  z-index: 1;
}

.action-menu {
  padding: 6px 0px 0px 0px  !important;
  margin-left: -200px;
  margin-top: 80px;
}

.action-menu .fa {
  margin-left: 10px;
  padding-top: 3px;
}

.action-menu .w3-card {
  width: 250px;
}

.action-menu .w3-button {
  width: 100%;
}

.tags-containers {
  display: inline-block;
  margin-left: 5px;
  margin-bottom: 5px;
}

.sending-accept {
  padding: 4px 12px;
  border-radius: 12px;
}

.sending-accept img {
  width: 30px;
}



/* Animations */

.fadeenter-enter-active {
  transition: opacity .9s
}

.fadeenter-enter {
  opacity: 0
}

.slideToRight-enter-active {
  animation: slideToRight-in 0.5s ease forwards;
}

.slideToRight-leave-active {
  animation: slideToRight-out 0.5s ease forwards;
}

.slideToLeft-enter-active {
  animation: slideToLeft-in 0.5s ease forwards;
}

.slideToLeft-leave-active {
  animation: slideToLeft-out 0.5s ease forwards;
}

.slideToUp-enter-active {
  animation: slideToUp-in 0.5s ease forwards;
}

.slideToUp-leave-active {
  animation: slideToUp-out 0.5s ease forwards;
}

.slideToDown-enter-active {
  animation: slideToDown-in 0.5s ease forwards;
}

.slideToDown-leave-active {
  animation: slideToDown-out 0.5s ease forwards;
}

.slideDownUp-enter-active {
  animation: slideToDown-in 0.5s ease forwards;
}

.slideDownUp-leave-active {
  animation: slideToUp-out 0.5s ease forwards;
}

.slideRightLeft-enter-active {
  animation: slideToRight-in 0.5s ease forwards;
}

.slideRightLeft-leave-active {
  animation: slideToLeft-out 0.5s ease forwards;
}

.slideLeftRight-enter-active {
  animation: slideToLeft-in 0.5s ease forwards;
}

.slideLeftRight-leave-active {
  animation: slideToRight-out 0.5s ease forwards;
}

@keyframes slideToRight-in {
  from {
    transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes slideToRight-out {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(100%);
  }
}

@keyframes slideToLeft-in {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes slideToLeft-out {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100%);
  }
}

@keyframes slideToDown-in {
  from {
    transform: translateY(-100%);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideToDown-out {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(100%);
  }
}

@keyframes slideToUp-in {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

@keyframes slideToUp-out {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
}

</style>
