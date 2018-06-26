<template lang="html">

  <div class="">
    <div class="">
      <div class="slider-container">
        <transition name="slideDownUp">
          <app-model-card-modal
            v-if="showNewCardModal"
            :isNew="true"
            :inSectionId="section.id"
            :inSectionTitle="section.title"
            @close="showNewCardModal = false"
            @updateCards="$store.commit('triggerUpdateSectionCards')">
          </app-model-card-modal>
        </transition>
      </div>

      <div class="slider-container">
        <transition name="slideDownUp">
          <app-model-section-modal
            v-if="showNewSubsectionModal"
            :isNew="true"
            :inSection="section"
            @close="showNewSubsectionModal = false">
          </app-model-section-modal>
        </transition>
      </div>

      <div class="slider-container">
        <transition name="slideDownUp">
          <app-model-section-modal
            v-if="showSectionModal"
            :isNew="false"
            :sectionId="section.id"
            :inSection="inSection"
            @close="showSectionModal = false">
          </app-model-section-modal>
        </transition>
      </div>

      <div class="slider-container">
        <transition name="slideDownUp">
          <app-edit-notifications-modal
            v-if="showEditNotificationsModal"
            :section="section"
            @close="showEditNotificationsModal = false">
          </app-edit-notifications-modal>
        </transition>
      </div>

    </div>

    <div class="modal-buttons-container">

      <div tooltip="Seciton Menu" class="expand-btn cursor-pointer"
        @click="expanded =! expanded"
        v-click-outside="clickOutsideMenu">
        <i class="fa"
          :class="{'fa-bars': !onlyAdd, 'fa-plus': onlyAdd}" aria-hidden="true">
        </i>
      </div>

      <app-drop-down-menu
        v-show="expanded"
        class="drop-menu"
        @addCard="addCard()"
        @addSubsection="addSubsection()"
        @edit="edit()"
        @remove="remove()"
        @delete="deleteSection()"
        @configNotifications="configNotifications()"
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
import EditNotificationsModal from '@/components/modal/EditNotificationsModal.vue'

export default {
  components: {
    'app-edit-notifications-modal': EditNotificationsModal
  },

  props: {
    section: {
      type: Object,
      default: null
    },
    inSection: {
      type: Object,
      deafult: null
    },
    hideAdd: {
      type: Boolean,
      deafult: false
    },
    onlyAdd: {
      type: Boolean,
      deafult: false
    }
  },

  data () {
    return {
      expanded: false,
      showSectionModal: false,
      showNewSubsectionModal: false,
      showNewCardModal: false,
      showEditNotificationsModal: false,
      deleteIntent: false,
      removeIntent: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    menuItems () {
      let menuItems = []

      if (this.isLoggedAnEditor && !this.onlyAdd) menuItems.push({ text: 'edit', value: 'edit', faIcon: 'fa-pencil' })
      if (this.isLoggedAnEditor && !this.hideAdd) menuItems.push({ text: 'add card', value: 'addCard', faIcon: 'fa-plus' })
      if (this.isLoggedAnEditor && !this.hideAdd) menuItems.push({ text: 'add subsection', value: 'addSubsection', faIcon: 'fa-plus' })

      if (!this.onlyAdd) menuItems.push({ text: 'notifications', value: 'configNotifications', faIcon: 'fa-cog' })

      if (this.isLoggedAnEditor && this.inSection !== null && !this.onlyAdd) menuItems.push({ text: 'remove', value: 'remove', faIcon: 'fa-times' })
      if (this.isLoggedAnEditor && this.inSection !== null && !this.onlyAdd) menuItems.push({ text: 'delete', value: 'delete', faIcon: 'fa-times' })

      return menuItems
   }
  },

  methods: {
    addCard () {
      this.expanded = false
      this.$emit('addCard')
    },
    addSubsection () {
      this.expanded = false
      this.showNewSubsectionModal = true
    },
    edit () {
      this.expanded = false
      this.showSectionModal = true
    },
    configNotifications () {
      this.expanded = false
      this.showEditNotificationsModal = true
    },
    remove () {
      this.removeIntent = true
    },
    deleteSection () {
      this.deleteIntent = true
    },
    removeConfirmed () {
      this.expanded = false
      this.axios.put('/1/model/section/' + this.inSection.id + '/removeSubsection/' + this.section.id,
        {}).then((response) => {
          if (response.data.result === 'success') {
            this.removeIntent = false
            this.expanded = false
            this.$emit('section-removed')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
        console.log(error)
      })
    },
    deleteConfirmed () {
      this.expanded = false
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
  margin-left: -120px;
  text-align: left;
  font-size: 15px;
  z-index: 2;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
