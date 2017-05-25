<template lang="html">
  <div class="">
    <app-new-initiative-modal
      v-if="showNewInitiativeModal" :parentInitId="parentInitiativeIdForModal"
      @close-this="showNewInitiativeModal = false"
      @initiative-created="initiativeCreated($event)">
    </app-new-initiative-modal>
    <button class="expand-left-menu-btn w3-button w3-theme w3-xlarge" @click="showSideBar = !showSideBar">></button>

    <div v-if="showSideBar" class="" style="width:300px">
      <keep-alive>
        <app-initiatives-nav
          :userInitiatives="userInitiatives"
          @close-navbar="showSideBar = false"
          @new-initiative="newInitiative($event)">
        </app-initiatives-nav>
      </keep-alive>
    </div>

    <div :class="{'expanded-menu': showSideBar}">
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

  data () {
    return {
      showNewInitiativeModal: false,
      showSideBar: true,
      userInitiatives: null
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
