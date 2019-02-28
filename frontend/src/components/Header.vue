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
      <div class="w3-col s12 m4 initiatives-breadcrumb-container">

        <div v-if="withInitiativeNav" class="w3-left nav-menu-btn w3-xlarge fa-button"
          @click="$store.commit('toggleExpandNav')">
          <i class="fa fa-chevron-circle-right"></i>
        </div>

        <div v-if="inInitiative && !accessDenied" @scroll="scrolling()" class="initiatives-breadcrumb small-scroll noselect" ref="breadcrumb">

          <div v-for="(parent, ix) in reversedParents" :key="parent.id" class="initiative-section">
            <router-link :to="{name: 'Initiative', params: {'initiativeId': parent.id }}"
              tag="div" class="w3-left cursor-pointer" :class="{ 'parent-2': ix > 0 }">
              {{ parent.meta.name }}
            </router-link>
            <div class="separator">
              <i class="fa fa-chevron-right" aria-hidden="true"></i>
            </div>
          </div>

          <div class="initiative-section">
            <div class="">
              {{ initiative.meta.name }}
            </div>

            <div class="notification-div">
              <app-notifications-list
                :element="initiative"
                contextType="INITIATIVE">
              </app-notifications-list>
            </div>

            <app-initiative-control-buttons
              class=""
              :initiative="this.initiative">
            </app-initiative-control-buttons>

          </div>

          <div v-if="initiative.status !== 'ENABLED'" class="w3-tag w3-round w3-margin-left error-panel">
            {{ initiative.status }}
          </div>

        </div>

        <div v-if="crumbTooLong && !windowIsSmall" class="scroll-btns">
          <div @click="scrollLeft()" class="left-scroll-btn">
            <i class="fa fa-chevron-left"></i>
          </div>
          <div @click="scrollRight()" class="right-scroll-btn">
            <i class="fa fa-chevron-right"></i>
          </div>
        </div>

      </div>

      <div class="w3-col s12 m8">
        <div class="w3-col s6">
          <div v-if="inInitiative && !accessDenied" class="tab-btns-container w3-xlarge">
            <popper
              :key="$store.state.support.isTouchScreen + Math.random()*100000000"
              :trigger="$store.state.support.isTouchScreen ? 'long-press' : 'hover'"
              :options="popperOptions" class="btn-div" :delay-on-mouse-in="1200" :delay-on-mouse-out="800">
              <app-help-popper
                :title="$t('help.CONTENT-TAB-TT')"
                :details="$t('help.CONTENT-TAB-DET')">
              </app-help-popper>

              <router-link slot="reference" :to="{ name: 'InitiativeModel', params: { initiativeId: initiative.id } }"
                class="tab-btn-space">
                <div tooltip="Content" class="fa-button noselect" :class="{'fa-button-selected': isModel}">
                  <span class=""><i class="fa fa-home" aria-hidden="true"></i></span>
                </div>
              </router-link>
            </popper>

            <popper
              :key="$store.state.support.isTouchScreen + Math.random()*100000000"
              :trigger="$store.state.support.isTouchScreen ? 'long-press' : 'hover'"
              :options="popperOptions" class="btn-div" :delay-on-mouse-in="1200" :delay-on-mouse-out="800">
              <app-help-popper
                :title="$t('help.MEMBERS-TAB-TT')"
                :details="$t('help.MEMBERS-TAB-DET')">
              </app-help-popper>

              <router-link slot="reference" :to="{ name: 'InitiativePeople', params: { initiativeId: initiative.id } }"
                class="tab-btn-space">
                <div tooltip="Members" class="fa-button noselect" :class="{'fa-button-selected': isPeople}">
                  <span class=""><i class="fa fa-users" aria-hidden="true"></i></span>
                </div>
              </router-link>
            </popper>

            <popper
              :key="$store.state.support.isTouchScreen + Math.random()*100000000"
              :trigger="$store.state.support.isTouchScreen ? 'long-press' : 'hover'"
              :options="popperOptions" class="btn-div" :delay-on-mouse-in="1200" :delay-on-mouse-out="800">
              <app-help-popper
                :title="$t('help.TRANSFERS-TAB-TT')"
                :details="$t('help.TRANSFERS-TAB-DET')">
              </app-help-popper>

              <router-link slot="reference" :to="{ name: 'InitiativeAssignations', params: { initiativeId: initiative.id } }"
                class="tab-btn-space">
                <div tooltip="Transfers" class="fa-button noselect" :class="{'fa-button-selected': isAssignations}">
                  <span class=""><i class="fa fa-exchange" aria-hidden="true"></i></span>
                </div>
              </router-link>
            </popper>

          </div>
          <div v-else class="logo-container">
            <router-link v-if="!windowIsSmall" slot="reference" :to="{name: 'InitiativesHome'}" class="cursor-pointer">
              <img class="logo" src="../assets/logo-color.png" alt="">
            </router-link>
          </div>

        </div>

        <div class="w3-col s6">

          <div v-if="$store.state.user.authenticated"
            class="">

            <popper trigger="click" :options="popperOptions" class="user-container">
              <app-drop-down-menu
                class="user-drop-menu"
                @profile="goMyProfile()"
                @home="goHome()"
                @notifications="showEditNotificationsModal = true"
                @logout="logoutUser()"
                :items="userMenuItems">
              </app-drop-down-menu>

              <div slot="reference" class="w3-right">
                <div v-if="$store.state.user.profile" class="logged-user-div fa-button w3-right">
                  <div class="avatar-img-container w3-left">
                    <img :src="$store.state.user.profile.pictureUrl" class="logged-avatar w3-circle noselect">
                  </div>
                  <div class="logged-nickname noselect w3-left w3-hide-medium w3-hide-small">
                    {{ $store.state.user.profile.nickname }}
                  </div>
                </div>
              </div>
            </popper>
          </div>
          <div v-else class="login-button-container w3-right">
            <button @click="login()"
              class="w3-button app-button" name="button">
              {{ $t('general.LOGIN_SIGNUP') }}
            </button>
          </div>

          <popper
            :key="$store.state.support.isTouchScreen + Math.random()*100000000"
            :trigger="$store.state.support.isTouchScreen ? 'long-press' : 'hover'"
            :options="popperOptions"  :delay-on-mouse-in="1200" :delay-on-mouse-out="800" class="btn-div">
            <app-help-popper
              :title="$t('help.LANDING-BUTTON-TT')"
              :details="$t('help.LANDING-BUTTON-DET')">
            </app-help-popper>

            <router-link slot="reference" :to="{ name: 'Landing', query: { demos: true }}" class="fa-button info-button w3-right"><i class="w3-xlarge fa fa-question-circle"></i></router-link>
          </popper>

          <popper
            :key="$store.state.support.isTouchScreen + Math.random()*100000000"
            :trigger="$store.state.support.isTouchScreen ? 'long-press' : 'hover'"
            :options="popperOptions"  :delay-on-mouse-in="1200" :delay-on-mouse-out="800" class="btn-div">
            <app-help-popper
              :title="$t('help.HOME-BUTTON-TT')"
              :details="$t('help.HOME-BUTTON-DET')">
            </app-help-popper>

            <router-link slot="reference" :to="{name: 'InitiativesHome'}" class="w3-right logo-container noselect cursor-pointer">
              <img class="icon" src="../assets/imago-red.png" alt="">
            </router-link>
          </popper>

        </div>
      </div>

    </div>
  </div>

</template>

<script>
import InitiativeControlButtons from '@/components/initiative/InitiativeControlButtons.vue'
import EditNotificationsModal from '@/components/modal/EditNotificationsModal.vue'
import NotificationsList from '@/components/notifications/NotificationsList.vue'

export default {
  components: {
    'app-initiative-control-buttons': InitiativeControlButtons,
    'app-edit-notifications-modal': EditNotificationsModal,
    'app-notifications-list': NotificationsList
  },

  props: {
    inInitiative: {
      type: Boolean,
      default: false
    },
    withInitiativeNav: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showEditNotificationsModal: false,
      draggingBC: false,
      crumbTooLong: false,
      scrollOnLeft: false,
      scrollOnRight: false
    }
  },

  computed: {
    accessDenied () {
      return this.$store.state.initiative.accessDenied
    },
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
      if (!this.accessDenied) {
        var copy = JSON.parse(JSON.stringify(this.initiative.parents))
        return copy.reverse()
      }
      return []
    },
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    isLoggedAMember () {
      return this.$store.getters.isLoggedAMember
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
        if (e.name === 'ModelSectionContent' || e.name === 'InitiativeModel') {
          res = true
        }
      })
      return res
    },
    userMenuItems () {
      return [
        {
          text: this.$t('general.HOME'),
          value: 'home',
          faIcon: 'fa-home'
        },
        {
          text: this.$t('general.PROFILE'),
          value: 'profile',
          faIcon: 'fa-user'
        },
        {
          text: this.$t('general.NOTIFICATIONS'),
          value: 'notifications',
          faIcon: 'fa-cog'
        },
        {
          text: this.$t('general.LOGOUT'),
          value: 'logout',
          faIcon: 'fa-power-off'
        }
      ]
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: false
          },
          flip: {
            enabled: false
          }
        }
      }
    }
  },

  methods: {
    login () {
      this.$store.state.user.lock.show()
    },
    goMyProfile () {
      this.$router.push({ name: 'UserProfilePage', params: { userId: this.$store.state.user.profile.c1Id } })
    },
    goHome () {
      this.$router.push({ name: 'InitiativesHome' })
    },
    logoutUser () {
      this.$store.dispatch('logoutUser')
    },
    scrolling () {
      this.checkCrumbScroll()
    },
    checkCrumbScroll () {
      if (!this.$refs.breadcrumb) {
        this.crumbTooLong = false
        return
      }
      this.crumbTooLong = this.$refs.breadcrumb.clientWidth < this.$refs.breadcrumb.scrollWidth

      this.checkScrollOnLeft()
      this.checkScrollOnRight()
    },
    checkScrollOnLeft () {
      if (this.$refs.breadcrumb.scrollLeft === 0) {
        this.scrollOnLeft = true
      } else {
        this.scrollOnLeft = false
      }
    },
    checkScrollOnRight () {
      if (this.$refs.breadcrumb.scrollLeft === this.$refs.breadcrumb.scrollWidth) {
        this.scrollOnRight = true
      } else {
        this.scrollOnRight = false
      }
    },
    scrollLeft () {
      this.$refs.breadcrumb.scrollLeft -= 50
    },
    scrollRight () {
      this.$refs.breadcrumb.scrollLeft += 50
    }
  },

  mounted () {
    this.$nextTick(() => {
      this.checkCrumbScroll()
    })
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

.initiatives-breadcrumb-container {
  position: relative;
  height: 50px;
}

.scroll-btns {
  position: absolute;
  top: 13px;
  width: 100%;
  height: 0px;
  font-size: 18px;
  color: white;
}

.scroll-btns > div {
  position: absolute;
  width: 30px;
  opacity: 0.3;
  background-color: rgb(184, 184, 184);
  text-align: center;
  cursor: pointer;
  border-radius: 6px;
}

.scroll-btns > div:hover {
  background-color: #15a5cc;
}

.scroll-btns .left-scroll-btn {
  left: 50px;
}

.scroll-btns .right-scroll-btn {
  right: -10px;
}

.notification-div {
  display: inline-block;
  vertical-align: top;
}

.initiatives-breadcrumb {
  white-space: nowrap;
  overflow-x: auto;
  overflow-y:  visible;
}

.initiative-section {
  padding: 0px 12px;
  font-size: 20px;
  text-align: left;
  display: inline-block;
}

.initiative-section > div {
  display: inline-block;
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

.user-container {
  position: relative;
}

.user-drop-menu {
  position: absolute;
  top: 50px;
  right: 0px;
  width: 150px;
  z-index: 3;
}

.login-button-container {
  padding-top: 6px;
  padding-right: 6px;
}

</style>
