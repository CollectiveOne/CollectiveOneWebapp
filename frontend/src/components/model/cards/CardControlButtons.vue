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
            @updateCards="$emit('updateCards')">
          </app-model-card-modal>
        </transition>
      </div>

      <div class="slider-container">
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

    <div class="modal-buttons-container">

      <div class="expand-btn cursor-pointer"
        @click="expanded =! expanded"
        v-click-outside="clickOutsideMenu">
        <i class="fa fa-bars" aria-hidden="true"></i>
      </div>

      <app-drop-down-menu
        v-show="expanded"
        class="drop-menu"
        @edit="edit()"
        @makeShared="makeShared()"
        @makeCommon="makeCommon()"
        @addCardBefore="addCardBefore()"
        @addCardAfter="addCardAfter()"
        @remove="remove()"
        @delete="deleteCard()"
        :items="menuItems">
      </app-drop-down-menu>

      <div class="w3-card w3-white drop-menu">

        <div v-if="makeSharedIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to make this card visible? It will be seen by all initiative members.
            </p>
          </div>
          <button
            class="w3-button light-grey"
            @click="makeSharedIntent = false">cancel
          </button>
          <button
            class="w3-button button-blue"
            @click="makeSharedConfirmed()">confirm
          </button>
        </div>

        <div v-if="makeCommonIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to make this card common? It will start being controlled be the initiative members and not only by you. It cannot be undonde.
            </p>
          </div>
          <button
            class="w3-button light-grey"
            @click="makeCommonIntent = false">cancel
          </button>
          <button
            class="w3-button button-blue"
            @click="makeCommonConfirmed()">confirm
          </button>
        </div>

        <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to remove this card from "{{ inSection.title }}"?
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
              <b>Warning:</b> Are you sure you want to completely delete this card? This will delete it from all the sections in which it is currenlty present.
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
      expanded: false,
      showNewCardModal: false,
      newCardLocation: 'end',
      showEditCardModal: false,
      makeSharedlIntent: false,
      makeCommonIntent: false,
      deleteIntent: false,
      removeIntent: false
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
    menuItems () {
      let menuItems = []

      switch (this.cardWrapper.scope) {
        case 'PRIVATE':
        case 'SHARED':
          if (this.isLoggedTheAuthor) menuItems.push({ text: 'edit', value: 'edit', faIcon: 'fa-pencil' })
          break

        case 'COMMON':
          if (this.isLoggedAnEditor) menuItems.push({ text: 'edit', value: 'edit', faIcon: 'fa-pencil' })
          break
      }

      if (this.isLoggedAnEditor && this.isPrivate && this.isLoggedTheAuthor) menuItems.push({ text: 'make shared', value: 'makeShared', faIcon: 'fa-share' })
      if (this.isLoggedAnEditor && this.isShared && this.isLoggedTheAuthor) menuItems.push({ text: 'make common', value: 'makeCommon', faIcon: 'fa-share' })
      if (this.isLoggedAnEditor) menuItems.push({ text: 'add card before', value: 'addCardBefore', faIcon: 'fa-plus' })
      if (this.isLoggedAnEditor) menuItems.push({ text: 'add card after', value: 'addCardAfter', faIcon: 'fa-plus' })
      if (this.isLoggedAnEditor) menuItems.push({ text: 'remove', value: 'remove', faIcon: 'fa-times' })
      if (this.isLoggedAnEditor) menuItems.push({ text: 'delete', value: 'delete', faIcon: 'fa-times' })
      return menuItems
   }
  },

  methods: {
    addCardBefore () {
      this.newCardLocation = 'before'
      this.showNewCardModal = true
    },
    addCardAfter () {
      this.newCardLocation = 'after'
      this.showNewCardModal = true
    },
    edit () {
      this.showEditCardModal = true
      this.expanded = false
    },
    remove () {
      this.removeIntent = true
    },
    deleteCard () {
      this.deleteIntent = true
    },
    makeShared () {
      this.makeSharedIntent = true
    },
    makeCommon () {
      this.makeCommonIntent = true
    },
    makeSharedConfirmed () {
      this.axios.put('/1/model/section/' + this.inSection.id + '/cardWrapper/' + this.cardWrapper.id + '/makeShared',
        {}).then((response) => {
          if (response.data.result === 'success') {
            this.makeSharedIntent = false
            this.expanded = false
            this.$emit('updateCards')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
        console.log(error)
      })
    },
    makeCommonConfirmed () {
      this.axios.put('/1/model/section/' + this.inSection.id + '/cardWrapper/' + this.cardWrapper.id + '/makeCommon',
        {}).then((response) => {
          console.log(response)
          if (response.data.result === 'success') {
            this.makeCommonIntent = false
            this.expanded = false
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
            this.expanded = false
            this.$emit('updateCards')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
        console.log(error)
      })
    },
    deleteConfirmed () {
      this.axios.delete('/1/model/cardWrapper/' + this.cardWrapper.id)
        .then((response) => {
          this.deleteIntent = false
          this.expanded = false
          this.$emit('updateCards')
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
  z-index: 2;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
