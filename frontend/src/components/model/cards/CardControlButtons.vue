<template lang="html">

  <div class="">
    <div class="modals">
      <div class="slider-container">
        <transition name="slideDownUp">
          <app-move-card-modal
            v-if="showMoveCardModal"
            :isMove="isMove"
            :cardWrapperToMove="cardWrapper"
            :fromSection="inSection"
            @close="showMoveCardModal = false"
            @update="$emit('updateCards')">
          </app-move-card-modal>
        </transition>
      </div>
    </div>

    <popper :append-to-body="true" trigger="click" :options="popperOptions" :toggleShow="togglePopperShow" class="">
      <div class="drop-menu-div">
        <app-drop-down-menu
          class="drop-menu"
          @edit="edit()"
          @detach="detachIntent = true"
          @startConsent="startConsent()"
          @stopConsent="stopConsent()"
          @addCardBefore="addCardLocation('before')"
          @addCardAfter="addCardLocation('after')"
          @addToSection="addToSection()"
          @moveToSection="moveToSection()"
          @remove="remove()"
          @enableDrag="enableDrag()"
          :items="menuItems">
        </app-drop-down-menu>

        <div class="w3-card w3-white drop-menu">

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
              <p v-html="$t('model.REMOVE_CARD_WARNING', { title: inSection.title })"></p>
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

        </div>
      </div>

      <div slot="reference" class="expand-btn cursor-pointer">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>
    </popper>

  </div>
</template>

<script>
import MoveCardModal from '@/components/model/modals/MoveCardModal.vue'

export default {
  components: {
    'app-move-card-modal': MoveCardModal
  },

  props: {
    cardWrapper: {
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
      showNewCardModal: false,
      newCardLocation: 'end',
      showEditCardModal: false,
      showMoveCardModal: false,
      detachIntent: false,
      removeIntent: false,
      togglePopperShow: false,
      isMove: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    isPrivate () {
      return this.cardWrapper.scope === 'PRIVATE'
    },
    isShared () {
      return this.cardWrapper.scope === 'SHARED'
    },
    isLoggedTheAuthor () {
      return this.cardWrapper.creator.c1Id === this.$store.state.user.profile.c1Id
    },
    isConsent () {
      return this.cardWrapper.governanceType === 'SIMPLE_CONSENT'
    },
    isConsentOpened () {
      return this.cardWrapper.simpleConsentState === 'OPENED'
    },
    menuItems () {
      let menuItems = []

      switch (this.cardWrapper.scope) {
        case 'PRIVATE':
        case 'SHARED':
          if (this.isLoggedTheAuthor) {
            menuItems.push({
              text: this.$t('general.EDIT'),
              value: 'edit',
              faIcon: 'fa-pencil' })
          }
          break

        case 'COMMON':
          if (this.isLoggedAnEditor) {
            menuItems.push({
              text: this.$t('general.EDIT'),
              value: 'edit',
              faIcon: 'fa-pencil' })

            if (this.showDetach) {
              menuItems.push({
                text: this.$t('model.DETACH_LC'),
                value: 'detach',
                faIcon: 'fa-chain-broken' })
            }
          }
          break
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.ADD_CARD_BEFORE'),
          value: 'addCardBefore',
          faIcon: 'fa-plus' })
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.ADD_CARD_AFTER'),
          value: 'addCardAfter',
          faIcon: 'fa-plus' })
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.ADD_TO_SECTION'),
          value: 'addToSection',
          faIcon: 'fa-clone' })
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('model.MOVE_TO_SECTION'),
          value: 'moveToSection',
          faIcon: 'fa-arrow-right' })
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$store.state.support.enableCardDraggingState ?
            this.$t('model.DISABLE_DRAG_AND_DROP') :
            this.$t('model.ENABLE_DRAG_AND_DROP'),
          value: 'enableDrag',
          faIcon: 'fa-arrows' })
      }

      if (this.isLoggedAnEditor && !this.isConsent) {
        menuItems.push({
          text: this.$t('model.START_CONSENT'),
          value: 'startConsent',
          faIcon: 'fa-users' })
      } else {
        if (this.isConsentOpened) {
          menuItems.push({
            text: this.$t('model.STOP_CONSENT'),
            value: 'stopConsent',
            faIcon: 'fa-users' })
        } else {
          menuItems.push({
            text: this.$t('model.REOPEN_CONSENT'),
            value: 'startConsent',
            faIcon: 'fa-users' })
        }
      }

      if (this.isLoggedAnEditor) {
        menuItems.push({
          text: this.$t('general.REMOVE'),
          value: 'remove',
          faIcon: 'fa-times' })
      }

      return menuItems
    },
    popperOptions () {
      return {
        placement: 'bottom-start',
        modifiers: {
          preventOverflow: {
            enabled: false
          }
        }
      }
    }
  },

  methods: {
    startConsent () {
      this.togglePopperShow = !this.togglePopperShow
      this.$emit('startConsent')
    },
    stopConsent () {
      this.togglePopperShow = !this.togglePopperShow
      this.$emit('stopConsent')
    },
    addCardLocation (location) {
      this.$store.commit('createNewCardLocation', location)
      this.$emit('createNew')
      this.togglePopperShow = !this.togglePopperShow
    },
    addToSection () {
      this.isMove = false
      this.showMoveCardModal = true
    },
    moveToSection () {
      this.isMove = true
      this.showMoveCardModal = true
    },
    edit () {
      this.$emit('edit')
      this.togglePopperShow = !this.togglePopperShow
    },
    remove () {
      this.removeIntent = true
    },
    enableDrag () {
      this.togglePopperShow = !this.togglePopperShow
      this.$store.commit('setCardDraggingState', true)
    },
    detachConfirmed () {
      this.axios.put('/1/model/section/' + this.inSection.id + '/detachCardWrapper/' + this.cardWrapper.id,
        {}).then((response) => {
        console.log(response)
        if (response.data.result === 'success') {
          this.detachIntent = false
          this.togglePopperShow = !this.togglePopperShow
          this.$emit('updateCards')
        } else {
          this.showOutputMessage(response.data.message)
        }
      }).catch((error) => {
        console.log(error)
      })
    },
    removeConfirmed () {
      this.axios.put('/1/model/section/' + this.inSection.id + '/removeCard/' + this.cardWrapper.id,
        {}).then((response) => {
        console.log(response)
        if (response.data.result === 'success') {
          this.removeIntent = false
          this.togglePopperShow = !this.togglePopperShow
          this.$emit('updateCards')
        } else {
          this.showOutputMessage(response.data.message)
        }
      }).catch((error) => {
        console.log(error)
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

.expand-btn {
}

.drop-menu {
  width: 220px;
  text-align: left;
  font-size: 15px;
}

.drop-menu-div {
  z-index: 3;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
