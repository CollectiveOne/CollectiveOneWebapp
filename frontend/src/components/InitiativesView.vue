<template lang="html">
  <div class="">
    <button class="expand-left-menu-btn w3-button w3-theme w3-xlarge" @click="showSideBar = !showSideBar">x</button>
    <div v-if="showSideBar" class="w3-sidebar w3-bar-block" style="width:25%">
      <app-initiatives-nav @close-navbar="showSideBar = false"></app-initiatives-nav>
    </div>
    <div :class="{'expanded-menu': showSideBar}">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import InitiativesNav from './InitiativesNav.vue'

export default {
  components: {
    AppInitiativesNav: InitiativesNav
  },

  data () {
    return {
      showSideBar: false
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
