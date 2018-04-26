<template lang="html">
  <div class="">
    <transition name="slideDownUp">
      <app-edit-notifications-modal
        v-if="showEditNotificationsModal"
        type="COLLECTIVEONE"
        @close="showEditNotificationsModal = false">
      </app-edit-notifications-modal>
    </transition>

    <div class="w3-row header-row drop-shadow-br light-grey">
      <div class="w3-col m4">
       <div class="w3-left nav-menu-btn w3-xlarge fa-button"
         @click="$store.commit('toggleExpandNav')">
         <i v-if="!expandNav" class="fa fa-chevron-circle-right"></i>
         <i v-if="expandNav" class="fa fa-chevron-circle-left"></i>
       </div>

       <div v-if="inInitiative" class="">
         <div v-for="(parent, ix) in reversedParents" :key="parent.id" class="w3-left initiative-section">
           <router-link :to="{name: 'Initiative', params: {'initiativeId': parent.id }}"
             tag="div" class="w3-left cursor-pointer" :class="{ 'parent-2': ix > 0 }">
             {{ parent.meta.name }}
           </router-link>
           <div class="w3-left separator">
             <i class="fa fa-chevron-right" aria-hidden="true"></i>
           </div>
         </div>
         <div class="w3-left">
            <div class="w3-left initiative-section">
              {{ initiative.meta.name }}
            </div>

            <app-initiative-control-buttons
              class="w3-left w3-margin-left" :initiative="this.initiative">
            </app-initiative-control-buttons>
         </div>
         <div v-if="initiative.status !== 'ENABLED'" class="w3-left w3-tag w3-round w3-margin-left error-panel">
           {{ initiative.status }}
         </div>
       </div>

      </div>

      <div class="w3-col m4">
       <div v-if="inInitiative" class="tab-btns-container w3-xlarge">
         <router-link :to="{ name: 'InitiativeOverview', params: { initiativeId: initiative.id } }"
           class="tab-btn-space">
           <div class="fa-button noselect" :class="{'fa-button-selected': isOverview}">
             <span class=""><i class="fa fa-home" aria-hidden="true"></i></span>
           </div>
         </router-link>
         <router-link :to="{ name: 'InitiativeModel', params: { initiativeId: initiative.id } }"
           class="tab-btn-space">
           <div class="fa-button noselect" :class="{'fa-button-selected': isModel}">
             <span class=""><i class="fa fa-th-large" aria-hidden="true"></i></span>
           </div>
         </router-link>
         <router-link :to="{ name: 'InitiativePeople', params: { initiativeId: initiative.id } }"
           class="tab-btn-space">
           <div class="fa-button noselect" :class="{'fa-button-selected': isPeople}">
             <span class=""><i class="fa fa-users" aria-hidden="true"></i></span>
           </div>
         </router-link>
         <router-link :to="{ name: 'InitiativeAssignations', params: { initiativeId: initiative.id } }"
           class="tab-btn-space">
           <div class="fa-button noselect" :class="{'fa-button-selected': isAssignations}">
             <span class=""><i class="fa fa-exchange" aria-hidden="true"></i></span>
           </div>
         </router-link>
       </div>
       <div v-else class="logo-container">
         <img class="logo" src="../assets/logo-color.png" alt="">
       </div>

      </div>

      <div class="w3-col m4">

       <div v-if="$store.state.user.authenticated"
         @click="userOptionsClicked()" class="w3-right cursor-pointer"
         v-click-outside="clickOutsideUser">

         <div v-if="$store.state.user.profile" class="logged-user-div fa-button w3-right">
           <div class="avatar-img-container w3-left">
             <img :src="$store.state.user.profile.pictureUrl" class="logged-avatar w3-circle noselect">
           </div>
           <div class="logged-nickname noselect w3-left w3-hide-medium w3-hide-small">
             {{ $store.state.user.profile.nickname }}
           </div>
         </div>

         <app-drop-down-menu
           v-show="showUserOptions"
           class="user-drop-menu"
           @profile="goMyProfile()"
           @home="goHome()"
           @notifications="showEditNotificationsModal = true"
           @logout="logoutUser()"
           :items="userMenuItems">
         </app-drop-down-menu>

       </div>
       <div v-else class="login-button-container w3-bar-item w3-right">
         <button @click="login()"
           class="w3-button app-button" name="button">
           login
         </button>
       </div>

       <router-link :to="{ name: 'Landing'}" class="fa-button info-button w3-right"><i class="w3-xlarge fa fa-info-circle"></i></router-link>

       <router-link v-if="inInitiative" :to="{name: 'InitiativesHome'}" class="w3-right logo-container noselect cursor-pointer">
         <img class="icon" src="../assets/imago-red.png" alt="">
       </router-link>

      </div>
    </div>
  </div>

</template>

<script>
import InitiativeControlButtons from '@/components/initiative/InitiativeControlButtons.vue'
import EditNotificationsModal from '@/components/modal/EditNotificationsModal.vue'

export default {
  components: {
    'app-initiative-control-buttons': InitiativeControlButtons,
    'app-edit-notifications-modal': EditNotificationsModal
  },

  props: {
    inInitiative: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showActivityList: false,
      showUserOptions: false,
      showEditNotificationsModal: false
    }
  },

  computed: {
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
    },
    expandNav () {
      return this.$store.state.support.expandNav
    },
    initiative () {
      return this.$store.state.initiative.initiative
    },
    reversedParents () {
      var copy = JSON.parse(JSON.stringify(this.initiative.parents))
      return copy.reverse()
    },
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    isLoggedAMember () {
      return this.$store.getters.isLoggedAMember
    },
    isOverview () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeOverview') {
          res = true
        }
      })
      return res
    },
    isPeople () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativePeople') {
          res = true
        }
      })
      return res
    },
    isAssignations () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeAssignations') {
          res = true
        }
      })
      return res
    },
    isModel () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeModelBase') {
          res = true
        }
      })
      return res
    },
    userMenuItems () {
      return [
        { text: 'home', value: 'home', faIcon: 'fa-home' },
        { text: 'profile', value: 'profile', faIcon: 'fa-user' },
        { text: 'notifications', value: 'notifications', faIcon: 'fa-cog' },
        { text: 'logout', value: 'logout', faIcon: 'fa-power-off' }
      ]
    }
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
  }
}
</script>

<style scoped>

.header-row {
  z-index: 2;
}

.nav-menu-btn {
  width: 50px;
  height: 50px;
  padding: 8px 12px;
  text-align: center;
}

.initiative-section {
  padding: 10px 0px 0px 12px;
  font-size: 20px;
  text-align: left;
}

.initiative-section .fa {
  font-size: 16px;
}

.separator {
  padding-left: 6px;
}

.tab-btns-container {
  width: 280px;
  height: 50px;
  margin: 0 auto;
}

.tab-btn-space {
  width: 70px;
  height: 50px;
  float: left;
  padding: 6px 12px;
  text-align: center;
}

.logo-container  {
  padding: 9px 12px;
}

.icon {
  height: 32px;
}

.logo-container {
  text-align: center;
}

.logo {
  height: 32px;
}

.info-button {
  display: block;
  width: 50px;
  height: 50px;
  padding: 13px 12px;
  text-align: center;
}

.logged-user-div {
  height: 50px;
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

.user-drop-menu {
 position: absolute;
 margin-top: 50px;
 width: 150px;
 z-index: 2;
}

.login-button-container {
  padding: 13px;
}

</style>
