<template lang="html">
  <div class="">
    <div v-if="initiative" class="w3-row">
      <div class="w3-white">

      <div class="header-container w3-bar">
        <transition name="fadeenter" mode="out-in" appear>
            <header class="white-bg" :key="initiative.meta.name">
              <div class="w3-bar-item w3-left">
                <h3 class="noselect">
                  <b><app-initiative-path :initiative="initiative"></app-initiative-path></b>
                </h3>
              </div>
              <div class="w3-bar-item w3-right">
                <div class="" style="width:55px; height:100%">
                  <div
                    v-if="isLoggedAMember || isLoggedAnAdmin"
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
            </header>
          </transition>
        </div>

        <div class="section-tabs w3-row w3-center light-grey">
          <router-link tag="div" :to="{ name: 'InitiativeOverview', params: { initiativeId: initiative.id } }"
            class="w3-col s4 tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isOverview}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isOverview}">Overview</h5>
          </router-link>
          <router-link tag="div" :to="{ name: 'InitiativePeople', params: { initiativeId: initiative.id } }"
            class="w3-col s4 tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isPeople}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isPeople}">People</h5>
          </router-link>
          <router-link tag="div" :to="{ name: 'InitiativeAssignations', params: { initiativeId: initiative.id } }"
            class="w3-col s4 tablink w3-bottombar w3-hover-light-grey"
            :class="{'border-blue': isAssignations}"
            @click="">
            <h5 class="noselect" :class="{'bold-text': isAssignations}">Transfers</h5>
          </router-link>
        </div>

        <!-- TODO: selecting the role on a select element with the mouse triggered the swipe  -->
        <!-- <v-touch v-on:swipeleft="onSwipeLeft()" v-on:swiperight="onSwipeRight()"> -->
        <div class="w3-row content-container">
          <transition :name="animationType" mode="out-in" appear>
            <router-view :key="initiative.id">
            </router-view>
          </transition>
        </div>
        <!-- </v-touch> -->
      </div>
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
      return this.initiative.loggedMember.role === 'ADMIN'
    },
    isLoggedAMember () {
      return this.initiative.loggedMember.role === 'MEMBER'
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
    animationType () {
      return this.$store.state.support.contentAnimationType
    }
  },

  methods: {
    newMember () {
      this.showNewMemberModal = true
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
      this.$store.dispatch('updateInitiative', this.$route.params.initiativeId)
    }
  },

  mounted () {
    this.$store.dispatch('updateInitiative', this.$route.params.initiativeId)
  }
}
</script>

<style scoped>

.header-container {
  height: 65px;
}

.title-arrow {
  font-size: 12px;
  margin-bottom: 10px;
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

.edit-btn-div {
  padding-top: 15px;
}

.content-container {
  overflow: hidden;
}


</style>
