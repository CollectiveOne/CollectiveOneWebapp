<template lang="html">
  <div>
    <div v-if="$store.state.support.initiativeLoaded" class="w3-row">
      <div class="w3-white w3-col ">
        <div class="header-container">
          <transition name="fadeenter" mode="out-in" appear>
            <div class="w3-row white-bg w3-border-bottom" :key="initiative.meta.name">
              <div class="w3-col m5">
                <div class="w3-left w3-xlarge flex-vert bars-div"
                  @click="$store.commit('toggleExpandNav')">
                  <i v-if="!expandNav" class="fa fa-chevron-circle-right"></i>
                  <i v-if="expandNav" class="fa fa-chevron-circle-left"></i>
                </div>

                <h3 class="w3-left initiative-path noselect">
                  <b><app-initiative-path :initiative="initiative"></app-initiative-path></b>
                </h3>
                <div class="w3-right edit-container">
                  <div v-if="isLoggedAMember"
                    @click="showEditMenu = !showEditMenu"
                    class="edit-btn-div w3-button w3-large"
                    style="height:100%">
                    <i class="fa fa-cog" aria-hidden="true"></i>
                  </div>
                  <div v-if="showEditMenu" class="edit-menu w3-dropdown-content w3-bar-block w3-card">
                    <div v-if="isLoggedAnAdmin"
                      @click="$store.commit('showEditInitiativeModal', true); showEditMenu = false"
                      class="w3-bar-item w3-button">
                      <i class="fa fa-pencil" aria-hidden="true"></i>edit initiative
                    </div>
                    <div v-if="isLoggedAMember || isLoggedAnAdmin"
                      @click="$store.commit('showEditNotificationsModal', true); showEditMenu = false"
                      class="w3-bar-item w3-button">
                      <i class="fa fa-bell" aria-hidden="true"></i>notifications
                    </div>
                  </div>
                </div>
              </div>

              <div class="w3-col m5 section-tabs w3-center">
                <div class="w3-bar header-div w3-border-bottom w3-white">
                  <router-link :to="{name: 'InitiativesHome'}" class="logo-container w3-bar-item noselect cursor-pointer">
                    <img class="icon" src="../../assets/imago-red.png" alt="">
                  </router-link>
                </div>

                <router-link :to="{ name: 'InitiativeOverview', params: { initiativeId: initiative.id } }"
                  class="tab-btn-space">
                  <div class="tab-btn noselect" :class="{'bold-text': isOverview, 'button-blue': isOverview}">
                    <span class="w3-hide-small w3-hide-medium tab-btn-text">Overview</span>
                    <span class="w3-hide-large"><i class="fa fa-home" aria-hidden="true"></i></span>
                  </div>
                </router-link>
                <router-link :to="{ name: 'InitiativeModel', params: { initiativeId: initiative.id } }"
                  class="tab-btn-space">
                  <div class="tab-btn noselect" :class="{'bold-text': isModel, 'button-blue': isModel}">
                    <span class="w3-hide-small w3-hide-medium tab-btn-text">Vision</span>
                    <span class="w3-hide-large"><i class="fa fa-lightbulb-o" aria-hidden="true"></i></span>
                  </div>
                </router-link>
                <router-link :to="{ name: 'InitiativePeople', params: { initiativeId: initiative.id } }"
                  class="tab-btn-space">
                  <div class="tab-btn noselect" :class="{'bold-text': isPeople, 'button-blue': isPeople}">
                    <span class="w3-hide-small w3-hide-medium tab-btn-text">People</span>
                    <span class="w3-hide-large"><i class="fa fa-users" aria-hidden="true"></i></span>
                  </div>
                </router-link>
                <router-link :to="{ name: 'InitiativeAssignations', params: { initiativeId: initiative.id } }"
                  class="tab-btn-space">
                  <div class="tab-btn noselect" :class="{'bold-text': isAssignations, 'button-blue': isAssignations}">
                    <span class="w3-hide-small w3-hide-medium tab-btn-text">Transfers</span>
                    <span class="w3-hide-large"><i class="fa fa-exchange" aria-hidden="true"></i></span>
                  </div>
                </router-link>
              </div>

              <div class="w3-col m2">
                <div v-if="$store.state.user.authenticated"
                  @click="userOptionsClicked()" class="w3-bar-item w3-right w3-button"
                  v-click-outside="clickOutsideUser">

                  <div v-if="$store.state.user.profile" class="logged-user">
                    <div class="avatar-img-container w3-left">
                      <img :src="$store.state.user.profile.pictureUrl" class="logged-avatar w3-circle noselect">
                    </div>
                    <div class="logged-nickname noselect w3-left  w3-hide-medium w3-hide-small">
                      {{ $store.state.user.profile.nickname }}
                    </div>
                  </div>
                  <div v-if="showUserOptions"
                    class="avatar-dropdown-content w3-card-2 w3-bar-block w3-white w3-large"
                    :class="{'left-align-1': windowIsSmall, 'left-align-2': !windowIsSmall}">

                    <div @click="goMyProfile()" class="w3-bar-item w3-button"><i class="fa fa-user" aria-hidden="true"></i>profile</div>
                    <div @click="goHome()" class="w3-bar-item w3-button"><i class="fa fa-home" aria-hidden="true"></i>home</div>
                    <div @click="logoutUser()" class="w3-bar-item w3-button"><i class="fa fa-power-off" aria-hidden="true"></i>logout</div>
                  </div>
                </div>
                <div v-else class="login-button-container w3-bar-item w3-right">
                  <button @click="login()"
                    class="w3-button app-button" name="button">
                    login
                  </button>
                </div>

                <div v-if="$store.state.user.authenticated" class="nots-div w3-right">
                  <app-notifications-list></app-notifications-list>
                </div>

                <div class="nots-div w3-right w3-button w3-xlarge flex-vert bars-div dark-gray-color">
                  <router-link :to="{ name: 'Landing'}"><i class="fa fa-info-circle"></i></router-link>
                </div>
              </div>

            </div>
          </transition>
        </div>

        <div class="w3-row content-container">
          <transition :name="animationType" mode="out-in" appear>
            <router-view :key="initiative.id">
            </router-view>
          </transition>
        </div>
      </div>
    </div>
    <div v-else class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../../assets/loading.gif" alt="">
    </div>
  </div>
</template>

<script>
import InitiativePath from '@/components/initiative/InitiativePath.vue'
export default {

  components: {
    'app-initiative-path': InitiativePath
  },

  data () {
    return {
      showEditMenu: false,
      showActivityList: false,
      showUserOptions: false
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
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
    animationType () {
      return this.$store.state.support.contentAnimationType
    },
    expandNav () {
      return this.$store.state.support.expandNav
    }
  },

  methods: {
    updateInitiative () {
      this.$store.dispatch('updateInitiative', this.$route.params.initiativeId)
    },
    newMember () {
      this.showNewMemberModal = true
    },
    clickOutsideShowMenu () {
      this.showEditMenu = false
    },
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
  },

  watch: {
    '$route' (to, from) {
      this.updateInitiative()
    }
  },

  created () {
    if (this.$route.matched.length === 3) {
      /* no subsection selected */
      this.$router.push({ name: 'InitiativeOverview', params: { initiativeId: this.$route.params.initiativeId } })
    }
    this.updateInitiative()
  }
}
</script>

<style scoped>

.bars-div {
  width: 70px;
  padding: 20px 20px;
  cursor: pointer;
}

.big-content-container {
  height: calc(100vh - 65px);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.content-container {
  overflow: hidden;
}

.header-container {
  min-height: 65px;
}

.initiative-path {
  padding-left: 16px;
  padding-top: 5px;
}

.title-arrow {
  font-size: 12px;
  margin-bottom: 10px;
}

.edit-container {
  min-height: 65px;
}

.edit-btn-div {
  min-height: 65px;
  padding-top: 20px;
  width: 65px;
}

.edit-menu {
  width: 220px;
  display: block;
  margin-left: 0px;
  margin-top: 0px;
}

.edit-menu .fa {
  margin-right: 15px;
}

.section-tabs {
  display: flex;
  flex-direction: row;
}

.tab-btn-space {
  display: block;
  padding: 19px 10px 0px 10px;
  width: 25%;
}

.tab-btn {
  padding: 3px 6px;
  border-radius: 6px;
}

.tab-btn-text {
  font-size: 14px;
}

.bold-text {
  font-weight: bold;
}

.icon {
  margin-top: 18px;
  height: 32px;
}

.nots-div {
  height: 100% !important;
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

.avatar-dropdown-content {
  position: absolute;
  width: 150px;
  margin-top: 58px;
}

</style>
