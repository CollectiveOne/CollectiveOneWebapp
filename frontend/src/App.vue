<template>
  <div id="app" class="container-fluid">
    <component :is="view"></component>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

import LandingView from '@/components/LandingView.vue'
import LoggedView from '@/components/LoggedView.vue'
import NewInitiativeModal from '@/components/modal/NewInitiativeModal.vue'

export default {
  name: 'app',

  methods: {
    ...mapActions(['initUserAuthenticated'])
  },

  computed: {
    view () {
      if (this.$store.state.user.authenticated) {
        return 'app-logged-view'
      } else {
        return 'app-landing-view'
      }
    }
  },

  components: {
    AppLandingView: LandingView,
    AppLoggedView: LoggedView,
    AppNewInitiativeModal: NewInitiativeModal
  },

  mounted () {
    this.initUserAuthenticated()
  }
}
</script>

<style>

#app html,body,h1,h2,h3,h4,h5 {
  font-family: 'Roboto', sans-serif;
}

textarea {
  resize: vertical;
}

.w3-modal {
  padding-top: 30px;
  display: block;
}

.w3-modal-content {
  margin-top: 50px;
  margin-bottom: 50px;
}

.l4-color {
  color: #e6e9fc !important; /* w3-indigo l3*/
}

.l3-color {
  color: #b1b8e3 !important; /* w3-indigo l3*/
}

.l2-color {
  color: #8995d6 !important; /* w3-indigo l2*/
}

.l1-color {
  color: #8995d6 !important
}

.d2-color {
  color: #334191 !important;  /* w3-indigo d2*/
}

.success-panel {
  color: white;
  background-color:  rgb(64, 163, 109) !important;
}

.warning-panel {
  color: white;
  background-color:  rgb(194, 157, 0) !important;
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

.slider-container {
  overflow: hidden;
}

.error-row {
  margin-top: 10px;
  width: 100%;
}

.error-panel {
  color: white;
  background-color: #b50000;
  margin-bottom: 6px;
}

.error-input {
  outline: none;
  border-color: red !important;
  border-color: red;
  box-shadow: 0 0 10px red;
}

.cursor-pointer {
  cursor: pointer !important;
}

.covered-div {
  position: relative;
}

.covered-div:before {
  content: " ";
  z-index: 10;
  display: block;
  position: absolute;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
}


/* Animations */

.fadeenter-enter-active {
  transition: opacity .9s
}

.fadeenter-enter {
  opacity: 0
}

.fade-enter-active, .fade-leave-active {
  transition: opacity .9s
}
.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
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
