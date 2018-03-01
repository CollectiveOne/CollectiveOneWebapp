<template lang="html">
  <div :class="{'big-content-container': isTimeline}">
    <div v-if="$store.state.support.initiativeLoaded" class="w3-row">
      <div class="w3-white w3-col ">
        <div class="header-container">
          <transition name="fadeenter" mode="out-in" appear>
            <div class="w3-row white-bg w3-border-bottom" :key="initiative.meta.name">
              <div class="w3-col m6">
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

              <div class="w3-col m6 w3-right section-tabs w3-center">
                <router-link :to="{ name: 'InitiativeOverview', params: { initiativeId: initiative.id } }"
                  class="tab-btn-space">
                  <div class="tab-btn noselect" :class="{'bold-text': isOverview, 'button-blue': isOverview}">
                    <span class="w3-hide-small w3-hide-medium tab-btn-text">Overview</span>
                    <span class="w3-hide-large"><i class="fa fa-home" aria-hidden="true"></i></span>
                  </div>
                </router-link>
                <router-link v-if="this.initiative.meta.modelEnabled"
                  :to="{ name: 'InitiativeTimeline', params: { initiativeId: initiative.id } }"
                  class="tab-btn-space">
                  <div class="tab-btn noselect" :class="{'bold-text': isTimeline, 'button-blue': isTimeline}">
                    <span class="w3-hide-small w3-hide-medium tab-btn-text">Timeline</span>
                    <span class="w3-hide-large"><i class="fa fa-comments-o" aria-hidden="true"></i></span>
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

            </div>
          </transition>
        </div>

        <div class="w3-row content-container" :class="{'content-container-small': isTimeline}">
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
    isTimeline () {
      var res = false
      this.$route.matched.forEach((e) => {
        if (e.name === 'InitiativeTimeline') {
          res = true
        }
      })
      return res
    },
    animationType () {
      return this.$store.state.support.contentAnimationType
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
  overflow: hidden;
}

.content-container-small {
  height: calc(100vh - 135px);
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
  height: 65px;
  display: flex;
  flex-direction: row;
}

.tab-btn-space {
  display: block;
  padding: 19px 10px 0px 10px;
  width: 20%;
}

.tab-btn {
  padding: 3px 6px;
  border-radius: 6px;
}

.tab-btn-text {
  font-size: 14px;
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
