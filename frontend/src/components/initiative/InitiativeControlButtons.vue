<template lang="html">

  <div class="">
    <div class="">
      <div class="slider-container">
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
      </div>
    </div>

    <div class="modal-buttons-container">

      <div class="expand-btn cursor-pointer"
        @click="expanded =! expanded"
        v-click-outside="clickOutsideMenu">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>

      <app-drop-down-menu
        v-show="expanded"
        class="drop-menu"
        @addCard="addCard()"
        @addSubsection="addSubsection()"
        @edit="edit()"
        @remove="remove()"
        @delete="deleteSection()"
        :items="menuItems">
      </app-drop-down-menu>

      <div v-if="inSection !== null" class="w3-card w3-white drop-menu">
        <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to remove the section "{{ section.title }}" as a subsection of "{{ inSection.title }}"?
            </p>
          </div>
          <button
            class="w3-button light-grey"
            @click="removeIntent = false">cancel
          </button>
          <button
            class="w3-button button-blue"
            @click="removeConfirmed()">confirm
          </button>
        </div>

        <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to completely delete the section "{{ section.title }}"? This will delete it from all the sections in which it is a subsection.
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
export default {

  props: {
    section: {
      type: Object,
      default: null
    },
    inSection: {
      type: Object,
      deafult: null
    }
  },

  data () {
    return {
      expanded: false,
      showNewInitiativeModal: false,
      showNewSubInitiativeModal: false,
      showEditInitiativeModal: false,
      showEditNotificationsModal: false
    }
  },

  computed: {
    menuItems () {
      if (this.inSection !== null) {
        return [
          { text: 'add card', value: 'addCard', faIcon: 'fa-plus' },
          { text: 'add subsection', value: 'addSubsection', faIcon: 'fa-plus' },
          { text: 'edit', value: 'edit', faIcon: 'fa-pencil' },
          { text: 'remove', value: 'remove', faIcon: 'fa-times' },
          { text: 'delete', value: 'delete', faIcon: 'fa-times' }
        ]
      } else {
        return [
          { text: 'add card', value: 'addCard', faIcon: 'fa-plus' },
          { text: 'add subsection', value: 'addSubsection', faIcon: 'fa-plus' },
          { text: 'edit', value: 'edit', faIcon: 'fa-pencil' }
        ]
      }
   }
  },

  methods: {
    addCard () {
      this.showNewCardModal = true
    },
    addSubsection () {
      this.showNewSubsectionModal = true
    },
    edit () {
      this.showSectionModal = true
    },
    remove () {
      this.removeIntent = true
    },
    deleteSection () {
      this.deleteIntent = true
    },
    removeConfirmed () {
      this.axios.put('/1/model/section/' + this.inSection.id + '/removeSubsection/' + this.section.id,
        {}).then((response) => {
          console.log(response)
          if (response.data.result === 'success') {
            this.removeIntent = false
            this.expanded = false
            this.$store.commit('triggerUpdateSectionsTree')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
        console.log(error)
      })
    },
    deleteConfirmed () {
      this.axios.delete('/1/model/section/' + this.section.id)
        .then((response) => {
          this.deleteIntent = false
          this.expanded = false
          this.$store.commit('triggerUpdateSectionsTree')
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
}

.drop-menu {
  position: absolute;
  width: 180px;
  margin-top: 3px;
  margin-left: -150px;
  text-align: left;
  font-size: 15px;
  z-index: 1;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
