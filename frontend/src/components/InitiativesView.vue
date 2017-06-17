<template lang="html">
  <div class="w3-row">
    <app-new-initiative-modal
      v-if="showNewInitiativeModal" :parentInitId="parentInitiativeIdForModal"
      @close-this="showNewInitiativeModal = false"
      @initiative-created="initiativeCreated($event)">
    </app-new-initiative-modal>

    <div v-if="expandNav" class="" style="width:300px">
      <keep-alive>
        <app-initiatives-nav
          :userInitiatives="userInitiatives"
          @new-initiative="newInitiative($event)">
        </app-initiatives-nav>
      </keep-alive>
    </div>

    <div v-if="showContent" :class="{'expanded-menu': expandNav}">
      <router-view @new-initiative="newInitiative($event)"></router-view>
    </div>
  </div>
</template>

<script>
import NewInitiativeModal from './modal/NewInitiativeModal.vue'
import InitiativesNav from './initiative/InitiativesNav.vue'

export default {
  components: {
    AppInitiativesNav: InitiativesNav,
    AppNewInitiativeModal: NewInitiativeModal
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
      userInitiatives: null
    }
  },

  computed: {
    showContent () {
      if (document.body.clientWidth < 601) {
        if (this.expandNav) {
          return false
        }
      }
      return true
    }
  },

  methods: {
    newInitiative (parentId) {
      this.parentInitiativeIdForModal = parentId
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

  mounted () {
    this.updatedMyInitiatives()
  }
}
</script>

<style scoped>

.expand-left-menu-btn {
  position: fixed;
}

.expanded-menu {
  margin-left: 300px;
}

</style>
