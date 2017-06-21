<template lang="html">
  <div class="w3-row">
    <div v-show="expandNav" class="this-nav" :class="navContainerClass">
      <keep-alive>
        <app-initiatives-nav
          :userInitiatives="userInitiatives"
          @selected="initiativeSelected($event)"
          @initiative-created="initiativeCreated($event)">
        </app-initiatives-nav>
      </keep-alive>
    </div>

    <div v-show="showContent" class="this-content" :class="contentContainerClass">
      <router-view @new-initiative="newInitiative($event)"></router-view>
    </div>
  </div>
</template>

<script>
import { mapMutations } from 'vuex'
import InitiativesNav from './initiative/InitiativesNav.vue'

export default {
  components: {
    AppInitiativesNav: InitiativesNav
  },

  props: {
    expandNav: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showNewInitiativeModal: false,
      showContent: true,
      userInitiatives: null
    }
  },

  computed: {
    windowIsSmall () {
      return window.innerWidth < 601
    },
    navContainerClass () {
      if (this.windowIsSmall) {
        return {
          'w3-sidebar': true,
          'nav-small': true
        }
      } else {
        return {
          'w3-col': true,
          's3': true
        }
      }
    },
    contentContainerClass () {
      if (this.windowIsSmall) {
        return {
          'w3-col': true,
          's12': true
        }
      } else {
        if (this.expandNav) {
          return {
            'w3-col': true,
            's9': true
          }
        } else {
          return {
            'w3-col': true,
            's12': true
          }
        }
      }
    }
  },

  methods: {
    ...mapMutations(['setSelectedInitiativeLevel', 'setSelectedInitiativeLevelOld']),

    initiativeSelected (initiative) {
      this.setSelectedInitiativeLevelOld(this.$store.state.selectedInitiativeLevel)
      this.setSelectedInitiativeLevel(initiative.level)
      if (this.windowIsSmall) {
        this.$emit('hide-nav')
      }
    },
    newInitiative (parentId) {
      this.parentInitiativeIdForModal = parentId
      this.asSubinitiativeForModal = (parentId !== '')
      this.showNewInitiativeModal = true
    },
    initiativeCreated (initiativeId) {
      this.updatedMyInitiatives()
      this.$router.push('/inits/' + initiativeId + '/overview')
    },
    updatedMyInitiatives () {
      this.axios.get('/1/secured/initiatives/mines').then((response) => {
        this.userInitiatives = response.data.data
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  watch: {
    '$route' (to, from) {
      console.log(to)
      console.log(from)
      console.log(this.$store.state.support.selectedInitiativeLevel)

      let newLevel = this.$store.state.support.selectedInitiativeLevel
      let oldLevel = this.$store.state.support.selectedInitiativeLevelOld

      
    }
  },

  mounted () {
    this.updatedMyInitiatives()
  }
}
</script>

<style scoped>

.expand-left-menu-btn {
  position: fixed;
}

.nav-small {
  width: 90%;
}

.this-nav {
  height: 100%;
}

.this-content {
}

</style>
