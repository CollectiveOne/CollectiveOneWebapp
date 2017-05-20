<template lang="html">
  <div class="">
    <app-new-initiative-modal v-if="showNewInitiativeModal" :parentInitId="parentInitiativeIdForModal" @close-this="showNewInitiativeModal = false"></app-new-initiative-modal>
    <button class="expand-left-menu-btn w3-button w3-theme w3-xlarge" @click="showSideBar = !showSideBar">></button>

    <div v-if="showSideBar" class="w3-sidebar w3-bar-block" style="width:25%">
      <keep-alive>
        <app-initiatives-nav
          @close-navbar="showSideBar = false"
          @new-initiative="newInitiative($event)">
        </app-initiatives-nav>
      </keep-alive>
    </div>

    <div :class="{'expanded-menu': showSideBar}">
      <router-view></router-view>
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
      showSideBar: true
    }
  },

  methods: {
    newInitiative (parentId) {
      this.parentInitiativeIdForModal = parentId
      this.showNewInitiativeModal = true
    }
  }
}
</script>

<style scoped>

.expand-left-menu-btn {
  position: fixed;
}

.expanded-menu {
  margin-left:25%;
}

</style>
