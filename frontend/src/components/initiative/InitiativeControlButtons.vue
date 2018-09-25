<template lang="html">

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

    <popper :append-to-body="true" trigger="click":options="popperOptions" class="">
      <div class="">
        <app-drop-down-menu
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

      <div slot="reference" class="expand-btn w3-xlarge fa-button">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>
    </popper>

  </div>
</template>

<script>
import EditInitiativeModal from '@/components/modal/EditInitiativeModal.vue'
import EditNotificationsModal from '@/components/modal/EditNotificationsModal.vue'

export default {
  components: {
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
      deleteIntent: false,
      showEditInitiativeModal: false,
      showEditNotificationsModal: false
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    menuItems () {
      let items = []
      if (this.isLoggedAnAdmin) items.push({ text: 'edit', value: 'edit', faIcon: 'fa-pencil' })
      items.push({ text: 'notifications', value: 'notifications', faIcon: 'fa-cog' })
      if (this.isLoggedAnAdmin) items.push({ text: 'delete', value: 'delete', faIcon: 'fa-times' })
      return items
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: false
          },
          flip: {
            enabled: false
          }
        }
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
      this.axios.delete('/1/initiative/' + this.initiative.id).then((response) => {
        this.$store.dispatch('refreshInitiative')
        this.$store.dispatch('updateMyInitiatives')
        if (this.initiative.parents.length > 0) {
          var parentId = this.initiative.parents[this.initiative.parents.length - 1].id
          this.$router.replace({ name: 'Initiative', params: { initiativeId: parentId } })
          this.closeThis()
        } else {
          window.location.href = '/'
        }
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

.initiative-buttons-container {
}

.expand-btn {
  padding: 8px 12px;
  width: 50px;
  height: 50px;
  text-align: center;
}

.drop-menu {
  width: 180px;
  text-align: left;
  font-size: 15px;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
