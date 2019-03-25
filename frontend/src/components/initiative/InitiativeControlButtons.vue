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

    </div>

    <popper :append-to-body="true" trigger="click" :options="popperOptions" class="">
      <div class="append-to-body-popper">
        <app-drop-down-menu
          class="drop-menu"
          @notifications="showNotifications()"
          @edit="showEditInitiative()"
          @newSubinitiative="showNewSubInitiative()"
          @delete="deleteInitiative()"
          @addFavorite="toggleFavorite()"
          :items="menuItems">
        </app-drop-down-menu>

        <div class="w3-card w3-white drop-menu">
          <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
            <div class="w3-row w3-padding w3-round light-grey w3-margin-bottom">
              <p v-html="$t('initiatives.DELETE_WARNING', { name:  initiative.meta.name })">
              </p>
            </div>
            <div class="w3-row btns-row">
              <button
                class="w3-button light-grey"
                @click="deleteIntent = false">{{ $t('general.CANCEL') }}
              </button>
              <button
                class="w3-button danger-btn"
                @click="deleteConfirmed()">{{ $t('general.CONFIRM') }}
              </button>
            </div>
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
import NewSubInitiativeModal from '@/components/modal/NewSubInitiativeModal.vue'
import EditInitiativeModal from '@/components/modal/EditInitiativeModal.vue'

export default {
  components: {
    'app-new-subinitiative-modal': NewSubInitiativeModal,
    'app-edit-initiative-modal': EditInitiativeModal
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
      showNewSubInitiativeModal: false,
      showEditInitiativeModal: false
    }
  },

  computed: {
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },

    isStarred () {
      return this.initiative.isStarred
    },

    menuItems () {
      let items = []

      if (this.isLoggedAnAdmin) {
        items.push({
          text: this.$t('general.EDIT'),
          value: 'edit',
          faIcon: 'fa-pencil'
        })
      }

      if (this.isLoggedAnAdmin) {
        items.push({
          text: this.$t('initiatives.NEW_SUBINITIATIVE'),
          value: 'newSubinitiative',
          faIcon: 'fa-plus'
        })
      }

      if (this.isLoggedAnAdmin) {
        items.push({
          text: this.$t('general.DELETE'),
          value: 'delete',
          faIcon: 'fa-times'
        })
      }

      items.push({
        text: this.isStarred ? this.$t('initiatives.REMOVE_FROM_FAVORITES') : this.$t('initiatives.ADD_TO_FAVORITES'),
        value: 'addFavorite',
        faIcon: 'fa-star'
      })

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
    },
    toggleFavorite () {
      this.axios.put('/1/initiative/' + this.initiative.id + (this.isStarred ? '/unstar' : '/star'))
     .then((response) => {
       this.$store.dispatch('refreshInitiative')
       this.$store.dispatch('updateMyInitiatives')
     })
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

.btns-row .w3-button {
  width: calc(50% - 6px);
}

</style>
