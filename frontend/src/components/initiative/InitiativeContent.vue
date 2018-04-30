<template lang="html">
  <div>
    <div v-if="$store.state.initiative.initiativeLoaded">
      <div class="w3-row">

       <div class="w3-row">
         <app-header :inInitiative="true"></app-header>
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
import Header from '@/components/Header.vue'
export default {

  components: {
    'app-header': Header
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
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
    '$route.params.initiativeId' (to, from) {
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

</style>
