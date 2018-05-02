<template lang="html">
<div class="">

  <transition name="slideDownUp">
    <app-new-initiative-modal
      v-if="showNewInitiativeModal"
      @close="showNewInitiativeModal = false">
    </app-new-initiative-modal>
  </transition>

  <!-- Initiatives View -->
  <div class="w3-cell-row">

    <transition name="slideRightLeft">
      <div v-show="expandNav" class="dark-gray nav-container-cell w3-sidebar">
        <app-initiatives-nav @initiative-selected="initiativeSelected()" @new-initiative="showNewInitiativeModal = true"></app-initiatives-nav>
      </div>
    </transition>

    <div v-show="showContent" class="w3-cell content-container-cell">
      <div class="slider-container">
        <transition name="slideDownUp" mode="out-in">
          <router-view></router-view>
        </transition>
      </div>
    </div>

  </div>

</div>
</template>

<script>
import InitiativesNav from '@/components/nav/InitiativesNav.vue'
import NewInitiativeModal from '@/components/modal/NewInitiativeModal.vue'

export default {
  components: {
    'app-new-initiative-modal': NewInitiativeModal,
    'app-initiatives-nav': InitiativesNav
  },

  data () {
    return {
      showContent: true,
      showNewInitiativeModal: false
    }
  },

  computed: {
    expandNav () {
      return this.$store.state.support.expandNav
    },
    userAuthenticated () {
      return this.$store.state.user.authenticated
    },
    windowIsSmall () {
      return this.$store.state.support.windowIsSmall
    },
    basePage () {
      return this.$route.name === 'Initiatives'
    }
  },

  methods: {
    initiativeSelected () {
      this.$store.commit('setExpandNav', false)
    }
  },

  mounted () {
    this.$store.dispatch('updateMyInitiatives')
  }
}
</script>

<style scoped>

.nav-container-cell {
  margin-top: 50px;
  width: 300px;
  vertical-align: top !important;
  height: calc(100vh - 51px);
}

</style>
