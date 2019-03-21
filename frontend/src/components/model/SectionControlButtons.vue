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

        <transition name="slideDownUp">
          <app-model-section-modal
            v-if="showNewSubsectionModal"
            :isNew="true"
            :inSection="section"
            @close="showNewSubsectionModal = false">
          </app-model-section-modal>
        </transition>

        <transition name="slideDownUp">
          <app-model-section-modal
            v-if="showNewSectionModal"
            :isNew="true"
            :inSection="inSection"
            :onSection="section"
            :isBefore="isNewSectionBefore"
            @close="showNewSectionModal = false">
          </app-model-section-modal>
        </transition>

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

    </div>

    <popper :append-to-body="true" trigger="click" :options="popperOptions" class="" :toggleShow="toggleMenu">
      <div class="append-to-body-popper">
        <app-drop-down-menu
          class="drop-menu"
          @addCard="addCard()"
          @addSubsection="addSubsection()"
          @addSectionBefore="addSection(true)"
          @addSectionAfter="addSection(false)"
          @edit="edit()"
          @detach="detachIntent = true"
          @remove="remove()"
          @delete="deleteSection()"
          @configNotifications="configNotifications()"
          @resetSubsectionsOrder="resetSubsectionsOrder()"
          @resetCardsOrder="resetCardsOrder()"
          :items="menuItems">
        </app-drop-down-menu>

        <div v-if="inSection !== null" class="w3-card w3-white drop-menu">

          <div v-if="detachIntent" class="w3-row w3-center delete-intent-div">
            <div class="w3-padding w3-round light-grey w3-margin-bottom">
              <p v-html="$t('model.DETACH_CARD_WARNING')"></p>
            </div>
            <button
              class="w3-button light-grey"
              @click="detachIntent = false">{{ $t('general.CANCEL') }}
            </button>
            <button
              class="w3-button button-blue"
              @click="detachConfirmed()">{{ $t('general.CONFIRM') }}
            </button>
          </div>

          <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
            <div class="w3-padding w3-round light-grey w3-margin-bottom">
              <p
                v-html="$t('model.REMOVE_SECTION_WARNING', { sectionTitle: section.title, inSectionTitle: inSection.title  })">
              </p>
            </div>
            <button
              class="w3-button light-grey"
              @click="removeIntent = false">{{ $t('general.CANCEL') }}
            </button>
            <button
              class="w3-button button-blue"
              @click="removeConfirmed()">{{ $t('general.CONFIRM') }}
            </button>
          </div>

          <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
            <div class="w3-padding w3-round light-grey w3-margin-bottom">
              <p
                v-html="$t('model.DELETE_SECTION_WARNING', { sectionTitle: section.title })">
              </p>
            </div>
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

      <div slot="reference" class="expand-btn cursor-pointer">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>
    </popper>

  </div>
</template>

<script>

export default {
  components: {
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
    showDetach: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      showSectionModal: false,
      showNewSubsectionModal: false,
      showNewSectionModal: false,
      isNewSectionBefore: false,
      showNewCardModal: false,
      showEditNotificationsModal: false,
      detachIntent: false,
      deleteIntent: false,
      removeIntent: false,
      toggleMenu: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    menuItems () {
      let menuItems = []

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('general.EDIT'),
          value: 'edit',
          faIcon: 'fa-pencil'
        })
        if (this.showDetach) {
          menuItems.push({
            text: this.$t('model.DETACH_LC'),
            value: 'detach',
            faIcon: 'fa-chain-broken' })
        }
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.ADD_SUBSECTION'),
          value: 'addSubsection',
          faIcon: 'fa-plus' })

        if (this.inSection !== null) {
          menuItems.push({
            text: this.$t('model.ADD_SECTION_BEFORE'),
            value: 'addSectionBefore',
            faIcon: 'fa-plus' })

          menuItems.push({
            text: this.$t('model.ADD_SECTION_AFTER'),
            value: 'addSectionAfter',
            faIcon: 'fa-plus' })
        }
      }

      if (this.isLoggedAnEditor && this.inSection !== null) {
        menuItems.push({
          text: this.$t('general.REMOVE'),
          value: 'remove',
          faIcon: 'fa-times' })
      }

      if (this.isLoggedAnEditor && this.inSection !== null) {
        menuItems.push({
          text: this.$t('general.DELETE'),
          value: 'delete',
          faIcon: 'fa-times' })
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.RESET_SUBSECTIONS_ORDER'),
          value: 'resetSubsectionsOrder',
          faIcon: 'fa-exclamation-triangle' })
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.RESET_CARDS_ORDER'),
          value: 'resetCardsOrder',
          faIcon: 'fa-exclamation-triangle' })
      }

      return menuItems
    },
    popperOptions () {
      return {
        placement: 'bottom',
        modifiers: {
          preventOverflow: {
            enabled: false
          }
        }
      }
    }
  },

  methods: {
    addCard () {
      this.toggleMenu = !this.toggleMenu
      this.$emit('addCard')
    },
    addSubsection () {
      this.toggleMenu = !this.toggleMenu
      this.showNewSubsectionModal = true
    },
    addSection (isBefore) {
      this.toggleMenu = !this.toggleMenu
      this.isNewSectionBefore = isBefore
      this.showNewSectionModal = true
    },
    edit () {
      this.toggleMenu = !this.toggleMenu
      this.showSectionModal = true
    },
    configNotifications () {
      this.toggleMenu = !this.toggleMenu
      this.showEditNotificationsModal = true
    },
    remove () {
      this.removeIntent = true
    },
    deleteSection () {
      this.deleteIntent = true
    },
    detachConfirmed () {
      this.axios.put('/1/model/section/' + this.inSection.id + '/detachSubsection/' + this.section.id,
        {}).then((response) => {
        console.log(response)
        if (response.data.result === 'success') {
          this.detachIntent = false
          this.$emit('section-removed')
        } else {
          this.showOutputMessage(response.data.message)
        }
      }).catch((error) => {
        console.log(error)
      })
    },
    removeConfirmed () {
      this.expanded = false
      this.axios.put('/1/model/section/' + this.inSection.id + '/removeSubsection/' + this.section.id,
        {}).then((response) => {
        if (response.data.result === 'success') {
          this.removeIntent = false
          this.toggleMenu = !this.toggleMenu
          this.$emit('section-detached', response.data.data)
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
          this.toggleMenu = !this.toggleMenu
          this.$store.commit('triggerUpdateSectionsTree')
        }).catch((error) => {
          console.log(error)
        })
    },
    resetSubsectionsOrder () {
      this.axios.delete('/1/model/section/' + this.section.id + '/resetSubsectionsOrder')
        .then((response) => {
          this.toggleMenu = !this.toggleMenu
          this.$store.commit('triggerUpdateExpands')
        }).catch((error) => {
          console.log(error)
        })
    },
    resetCardsOrder () {
      this.axios.delete('/1/model/section/' + this.section.id + '/resetCardWrappersOrder')
        .then((response) => {
          this.toggleMenu = !this.toggleMenu
          this.$store.commit('triggerUpdateSectionCards')
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
  width: 250px;
  text-align: left;
  font-size: 15px;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
