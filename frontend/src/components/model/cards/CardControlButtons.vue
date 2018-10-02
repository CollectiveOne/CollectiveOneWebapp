<template lang="html">

  <div class="">
    <div class="modals">
      <div class="slider-container">
        <transition name="slideDownUp">
          <app-model-card-modal
            v-if="showNewCardModal"
            :isNew="true"
            :inSectionId="inSection.id"
            :inSectionTitle="inSection.title"
            :atCardWrapper="cardWrapper"
            :newCardLocation="newCardLocation"
            @close="showNewCardModal = false"
            @update="$emit('update')"
            @updateCards="$emit('updateCards')">
          </app-model-card-modal>
        </transition>

        <transition name="slideDownUp">
          <app-model-card-modal
            v-if="showEditCardModal"
            :isNew="false"
            :cardWrapperId="cardWrapper.id"
            :inSectionId="inSection.id"
            :inSectionTitle="inSection.title"
            :editingInit="true"
            @close="showEditCardModal = false"
            @update="$emit('update')">
          </app-model-card-modal>
        </transition>
      </div>
    </div>

    <popper :append-to-body="true" trigger="click" :options="popperOptions" :toggleShow="togglePopperShow" class="">
      <div class="">
        <app-drop-down-menu
          class="drop-menu"
          @edit="edit()"
          @startConsent="startConsent()"
          @stopConsent="stopConsent()"
          @addCardBefore="addCardLocation('before')"
          @addCardAfter="addCardLocation('after')"
          @remove="remove()"
          :items="menuItems">
        </app-drop-down-menu>

        <div class="w3-card w3-white drop-menu">

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
export default {

  props: {
    cardWrapper: {
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
      showNewCardModal: false,
      newCardLocation: 'end',
      showEditCardModal: false,
      removeIntent: false,
      togglePopperShow: false
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
    edit () {
      //  this.showEditCardModal = true
      this.$emit('edit')
      this.togglePopperShow = !this.togglePopperShow
    },
    remove () {
      this.removeIntent = true
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
  width: 180px;
  text-align: left;
  font-size: 15px;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
