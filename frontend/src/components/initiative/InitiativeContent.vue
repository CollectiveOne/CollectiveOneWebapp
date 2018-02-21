<template lang="html">
  <div class="big-content-container">
    <div v-if="$store.state.support.initiativeLoaded" class="w3-row">
      <div class="w3-white w3-col ">
        <div class="header-container">
          <transition name="fadeenter" mode="out-in" appear>
            <header class="white-bg w3-row" :key="initiative.meta.name">
              <div class="w3-col w3-right" style="width:50px">
                <div class="edit-container">
                  <div
                    v-if="isLoggedAMember"
                    @click="showEditMenu = !showEditMenu"
                    class="edit-btn-div w3-button w3-large"
                    style="width:100%; height:100%">
                    <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
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
                      <i class="fa fa-cog" aria-hidden="true"></i>notifications
                    </div>
                  </div>
                </div>
              </div>
              <div class="w3-rest">
                <h3 class="initiative-path noselect">
                  <b><app-initiative-path :initiative="initiative"></app-initiative-path></b>
                </h3>
              </div>
            </header>
          </transition>
        </div>

        <div class="section-tabs w3-row w3-center light-grey">
          <router-link tag="div" :to="{ name: 'InitiativeOverview', params: { initiativeId: initiative.id } }"
            class="w3-col tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isOverview, 's3': fourTabs || fiveTabs, 's4': threeTabs}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isOverview}">Overview</h5>
          </router-link>
          <router-link
            tag="div" :to="{ name: 'InitiativeModel', params: { initiativeId: initiative.id } }"
            class="w3-col tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isModel, 's3': fourTabs || fiveTabs, 's4': threeTabs}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isModel}">Vision</h5>
          </router-link>
          <router-link v-if="this.initiative.meta.modelEnabled"
            tag="div" :to="{ name: 'InitiativeMessages', params: { initiativeId: initiative.id } }"
            class="w3-col tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isMessages, 's2': fiveTabs, 's3': fourTabs, 's4': threeTabs}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isModel}">
              <span class="w3-hide-small">Messages</span>
              <span class="w3-hide-medium w3-hide-large"><i class="fa fa-comments-o" aria-hidden="true"></i></span>
            </h5>
          </router-link>
          <router-link tag="div" :to="{ name: 'InitiativePeople', params: { initiativeId: initiative.id } }"
            class="w3-col tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isPeople, 's2': fiveTabs, 's3': fourTabs, 's4': threeTabs}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isPeople}">
              <span class="w3-hide-small">People</span>
              <span class="w3-hide-medium w3-hide-large"><i class="fa fa-users" aria-hidden="true"></i></span>
            </h5>
          </router-link>
          <router-link tag="div" :to="{ name: 'InitiativeAssignations', params: { initiativeId: initiative.id } }"
            class="w3-col tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isAssignations, 's2': fiveTabs, 's3': fourTabs, 's4': threeTabs}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isAssignations}">
              <span class="w3-hide-small">Transfers</span>
              <span class="w3-hide-medium w3-hide-large"><i class="fa fa-exchange" aria-hidden="true"></i></span>
            </h5>
          </router-link>
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
      showEditMenu: false
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
        if (e.name === 'InitiativeModel') {
          res = true
        }
      })
      return res
    },
    isMessages () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeMessages') {
          res = true
        }
      })
      return res
    },
    animationType () {
      return this.$store.state.support.contentAnimationType
    },
    threeTabs () {
      return false
    },
    fourTabs () {
      return !this.initiative.meta.modelEnabled
    },
    fiveTabs () {
      return this.initiative.meta.modelEnabled
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
    onSwipeLeft () {
      console.log('swiped left')
      if (this.isOverview) {
        this.$router.push({name: 'InitiativePeople'})
        return
      }
      if (this.isPeople) {
        this.$router.push({name: 'InitiativeAssignations'})
        return
      }
    },
    onSwipeRight () {
      console.log('swiped right')
      if (this.isAssignations) {
        this.$router.push({name: 'InitiativePeople'})
        return
      }
      if (this.isPeople) {
        this.$router.push({name: 'InitiativeOverview'})
        return
      }
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

.big-content-container {
  height: calc(100vh - 65px);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.content-container {
  height: calc(100vh - 190px);
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
}

.edit-menu {
  width: 220px;
  display: block;
  margin-left: -165px;
  margin-top: 6px;
}

.edit-menu .fa {
  margin-right: 15px;
}

.section-tabs {
  height: 65px;
}

.tablink {
  height: 100%;
  padding-top: 8px;
  cursor: pointer;
}

.bold-text {
  font-weight: bold;
}

</style>
