<template lang="html">
  <div class="initiative-content">
    <div v-if="$store.state.initiative.initiativeLoaded" class="initiative-content-loaded">
      <div class="w3-row initiative-row">

        <transition name="slideDownUp">
          <div v-show="!hideTopBar" class="w3-row initiative-header-row">
            <app-header :inInitiative="true"></app-header>
          </div>
        </transition>

        <div v-if="windowIsSmall" class="w3-row hide-nail-container">
          <div @click="hideTopBar = !hideTopBar" class="hide-nail drop-shadow-br cursor-pointer">
            <i v-if="!hideTopBar" class="fa fa-chevron-up" aria-hidden="true"></i>
            <i v-else class="fa fa-chevron-down" aria-hidden="true"></i>
          </div>
        </div>

        <div v-if="!accessDenied" class="w3-row initiative-content-row">
          <transition :name="animationType" mode="out-in" appear>
            <router-view :key="initiative.id">
            </router-view>
          </transition>
        </div>
        <div v-else class="w3-center w3-container access-denied-panel">
          <div class="w3-card-2 w3-round-large w3-padding error-panel">
            <h4>{{ $t('initiatives.ACCESS_DENIED') }}</h4>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="w3-row w3-center loader-gif-container">
      <img class="loader-gif" src="../../assets/loading.gif" alt="">
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
export default {

  components: {
    'app-header': Header
  },

  data () {
    return {
      hideTopBar: false
    }
  },

  computed: {
    accessDenied () {
      return this.$store.state.initiative.accessDenied
    },
    initiative () {
      return this.$store.state.initiative.initiative
    },
    animationType () {
      return this.$store.state.support.contentAnimationType
    },
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
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
      this.$router.push({ name: 'InitiativesHome' })
    },
    logoutUser () {
      this.$store.dispatch('logoutUser')
    }
  },

  watch: {
    '$route.params.initiativeId' (to, from) {
      this.updateInitiative()
    }
  },

  created () {
    if (this.$route.matched.length === 3) {
      /* no subsection selected */
      this.$router.push({ name: 'InitiativeModel', params: { initiativeId: this.$route.params.initiativeId } })
    }
    this.updateInitiative()
  }
}
</script>

<style scoped>

.initiative-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  min-height: 0;
  /*position: relative;*/
}

.hide-nail-container {
  position: relative;
  z-index: 2;
}

.hide-nail {
  position: absolute;
  right: 20px;
  height: 25px;
  width: 35px;
  background-color: #313942;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  text-align: center;
  color: white;
}

.hide-nail:hover {
  background-color: #3e464e;
}

.initiative-content-loaded {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.initiative-row {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.initiative-header-row {
  min-height: 50px;
  flex: 0 0 auto;
}

.initiative-content-row {
  flex: 1 0 0;
  min-height: 0;
}

.temp-content {
  background-color: red;
  height: 1500px;
  width: 100%;
}

.access-denied-panel {
  max-width: 600px;
  margin: 0 auto;
  margin-top: 50px;
}

</style>
