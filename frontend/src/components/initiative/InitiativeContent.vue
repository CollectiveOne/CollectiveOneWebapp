<template lang="html">
  <div class="">
    <div v-if="initiative" class="w3-row">
      <div class="w3-white">

      <div class="header-container">
        <transition name="fadeenter" mode="out-in">
            <header class="w3-theme" :key="initiative.name">
              <div class="w3-row">
                <div v-if="isLoggedAnAdmin" class="w3-col w3-right" style="width:55px; height:100%">
                  <div @click="showEditMenu = !showEditMenu" class="edit-btn-div w3-button w3-large" style="width:100%; height:100%">
                    <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
                  </div>
                  <div v-if="showEditMenu" class="edit-menu w3-dropdown-content w3-bar-block w3-card d2-color">
                    <div @click="$store.commit('showEditInitiativeModal', true); showEditMenu = false" class="w3-bar-item w3-button">
                      <i class="fa fa-pencil" aria-hidden="true"></i>name and driver
                    </div>
                    <div @click="$store.commit('showEditNotificationsModal', true); showEditMenu = false" class="w3-bar-item w3-button">
                      <i class="fa fa-cog" aria-hidden="true"></i>notifications
                    </div>
                  </div>
                </div>
                <div class="w3-rest w3-container">
                  <h3 class="noselect">
                    <app-initiative-path :initiative="initiative"></app-initiative-path>
                  </h3>
                </div>
              </div>
            </header>
          </transition>
        </div>

        <div class="section-tabs w3-row w3-center">
          <router-link tag="div" :to="{ name: 'InitiativeOverview', params: { initiativeId: initiative.id } }" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey"
            :class="{'w3-border-blue': isOverview}"
            @click="">
            <h5 class="d2-color noselect" :class="{'bold-text': isOverview}">Overview</h5>
          </router-link>
          <router-link tag="div" :to="{ name: 'InitiativePeople', params: { initiativeId: initiative.id } }" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey"
            :class="{'w3-border-blue': isPeople}"
            @click="">
            <h5 class="d2-color noselect" :class="{'bold-text': isPeople}">People</h5>
          </router-link>
          <router-link tag="div" :to="{ name: 'InitiativeAssignations', params: { initiativeId: initiative.id } }" class="w3-col s4 tablink w3-bottombar w3-hover-light-grey"
            :class="{'w3-border-blue': isAssignations}"
            @click="">
            <h5 class="d2-color noselect" :class="{'bold-text': isAssignations}">Transfers</h5>
          </router-link>
        </div>

        <div class="w3-row content-container">
          <transition :name="animationType" mode="out-in">
            <router-view :key="initiative.id">
            </router-view>
          </transition>
        </div>
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
}

.title-arrow {
  font-size: 12px;
  margin-bottom: 10px;
}

.edit-menu {
  width: 220px;
  display: block;
  margin-left: -165px;
}

.edit-menu .fa {
  margin-right: 15px;
}

.tablink {
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
