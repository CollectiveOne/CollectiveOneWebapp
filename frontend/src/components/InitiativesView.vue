<template lang="html">
<div class="">

  <!-- All Modals in one place -->
  <div class="modals">

    <transition name="slideDownUp">
      <app-new-initiative-modal v-if="showNewInitiativeModal">
      </app-new-initiative-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-subinitiative-modal v-if="showNewSubInitiativeModal">
      </app-new-subinitiative-modal>
    </transition>

    <transition name="slideDownUp">
      <app-edit-initiative-modal v-if="showEditInitiativeModal">
      </app-edit-initiative-modal>
    </transition>

    <transition name="slideDownUp">
      <app-edit-notifications-modal v-if="showEditNotificationsModal">
      </app-edit-notifications-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-tokenmint-modal v-if="showNewTokenMintModal">
      </app-new-tokenmint-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-assignation-modal v-if="showNewAssignationModal">
      </app-new-assignation-modal>
    </transition>

    <transition name="slideDownUp">
      <app-new-initiative-transfer-modal v-if="showNewInitiativeTransferModal">
      </app-new-initiative-transfer-modal>
    </transition>

  </div>

  <!-- Initiatives View -->

  <div class="w3-row">
    <transition name="slideRightLeft">
      <div v-show="expandNav" :class="navContainerClass">
        <app-initiatives-nav @initiative-selected="initiativeSelected()"></app-initiatives-nav>
      </div>
    </transition>

    <div v-show="showContent" class="this-content" :class="contentContainerClass">
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
import NewSubInitiativeModal from '@/components/modal/NewSubInitiativeModal.vue'
import EditInitiativeModal from '@/components/modal/EditInitiativeModal.vue'
import EditNotificationsModal from '@/components/modal/EditNotificationsModal.vue'
import NewTokenMintModal from '@/components/modal/NewTokenMintModal.vue'
import NewInitiativeTransferModal from '@/components/modal/NewInitiativeTransferModal.vue'
import NewAssignationModal from '@/components/modal/NewAssignationModal.vue'

export default {
  components: {
    'app-new-initiative-modal': NewInitiativeModal,
    'app-new-subinitiative-modal': NewSubInitiativeModal,
    'app-initiatives-nav': InitiativesNav,
    'app-edit-initiative-modal': EditInitiativeModal,
    'app-edit-notifications-modal': EditNotificationsModal,
    'app-new-tokenmint-modal': NewTokenMintModal,
    'app-new-initiative-transfer-modal': NewInitiativeTransferModal,
    'app-new-assignation-modal': NewAssignationModal
  },

  props: {
    expandNav: {
      type: Boolean,
      default: true
    }
  },

  data () {
    return {
      showContent: true
    }
  },

  computed: {
    showNewInitiativeModal () {
      return this.$store.state.modals.showNewInitiativeModal
    },
    showNewSubInitiativeModal () {
      return this.$store.state.modals.showNewSubInitiativeModal
    },
    showEditInitiativeModal () {
      return this.$store.state.modals.showEditInitiativeModal
    },
    showEditMenu () {
      return this.$store.state.modals.showEditMenu
    },
    showEditNotificationsModal () {
      return this.$store.state.modals.showEditNotificationsModal
    },
    showNewTokenMintModal () {
      return this.$store.state.modals.showNewTokenMintModal
    },
    showNewInitiativeTransferModal () {
      return this.$store.state.modals.showNewInitiativeTransferModal
    },
    showNewAssignationModal () {
      return this.$store.state.modals.showNewAssignationModal
    },
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
    },
    basePage () {
      return this.$route.name === 'Initiatives'
    }
  },

  methods: {
    initiativeSelected () {
      if (this.windowIsSmall) {
        this.$emit('hide-nav')
      }
    }
  },

  mounted () {
    this.$store.dispatch('updatedMyInitiatives')
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

.base-page-div {

}

</style>
