<template lang="html">

  <div class="">
    <div class="">
      <div class="slider-container">
        <transition name="slideDownUp">
          <app-new-subinitiative-modal
            v-if="showNewSubInitiativeModal"
            :parentInitiative="initiative"
            @close="showNewSubInitiativeModal = false">
          </app-new-subinitiative-modal>
        </transition>

        <transition name="slideDownUp">
          <app-edit-initiative-modal
            v-if="showEditInitiativeModal"
            @close="showEditInitiativeModal = false">
          </app-edit-initiative-modal>
        </transition>

        <transition name="slideDownUp">
          <app-edit-notifications-modal
            v-if="showEditNotificationsModal"
            type="INITIATIVE"
            :initiative="initiative"
            @close="showEditNotificationsModal = false">
          </app-edit-notifications-modal>
        </transition>
      </div>
    </div>

    <div class="modal-buttons-container">

      <div class="expand-btn w3-xlarge fa-button"
        @click="expanded =! expanded"
        v-click-outside="clickOutsideMenu">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>

      <app-drop-down-menu
        v-show="expanded"
        class="drop-menu"
        @notifications="showNotifications()"
        @edit="showEditInitiative()"
        @newSubinitiative="showNewSubInitiative()"
        @delete="deleteInitiative()"
        :items="menuItems">
      </app-drop-down-menu>

      <div class="w3-card w3-white drop-menu">
        <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to completely delete the initiative "{{ initiative.meta.name }}"? This will delete all its contents.
            </p>
          </div>
          <button
            class="w3-button light-grey"
            @click="deleteIntent = false">cancel
          </button>
          <button
            class="w3-button danger-btn"
            @click="deleteConfirmed()">confirm
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NewSubInitiativeModal from '@/components/modal/NewSubInitiativeModal.vue'
import EditInitiativeModal from '@/components/modal/EditInitiativeModal.vue'
import EditNotificationsModal from '@/components/modal/EditNotificationsModal.vue'

export default {
  components: {
    'app-new-subinitiative-modal': NewSubInitiativeModal,
    'app-edit-initiative-modal': EditInitiativeModal,
    'app-edit-notifications-modal': EditNotificationsModal
  },
  props: {
    initiative: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      expanded: false,
      deleteIntent: false,
      showNewSubInitiativeModal: false,
      showEditInitiativeModal: false,
      showEditNotificationsModal: false
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    menuItems () {
      if (this.isLoggedAnAdmin !== null) {
        return [
          { text: 'edit', value: 'edit', faIcon: 'fa-pencil' },
          { text: 'new subinitiative', value: 'newSubinitiative', faIcon: 'fa-plus' },
          { text: 'notifications', value: 'notifications', faIcon: 'fa-cog' },
          { text: 'delete', value: 'delete', faIcon: 'fa-times' }
        ]
      } else {
        return [
          { text: 'notifications', value: 'notifications', faIcon: 'fa-engine' }
        ]
      }
    }
  },

  methods: {
    showNotifications () {
      this.expanded = false
      this.showEditNotificationsModal = true
    },
    showEditInitiative () {
      this.expanded = false
      this.showEditInitiativeModal = true
    },
    showNewSubInitiative () {
      this.expanded = false
      this.showNewSubInitiativeModal = true
    },
    deleteInitiative () {
      this.deleteIntent = true
    },
    deleteConfirmed () {
      this.axios.delete('/1/model/section/' + this.section.id)
        .then((response) => {
          this.deleteIntent = false
          this.expanded = false
        }).catch((error) => {
          console.log(error)
        })
    },
    clickOutsideMenu () {
      this.expanded = false
    }
  },

  mounted () {
  },

  destroyed () {
  }

}
</script>

<style scoped>

.expand-btn {
  padding: 8px 12px;
  width: 50px;
  height: 50px;
  text-align: center;
}

.drop-menu {
  position: absolute;
  width: 180px;
  margin-top: 0px;
  margin-left: -80px;
  text-align: left;
  font-size: 15px;
  z-index: 2;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
