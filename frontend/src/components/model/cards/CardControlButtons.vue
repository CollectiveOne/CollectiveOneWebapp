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
        @addCardBefore="addCardBefore()"
        @addCardAfter="addCardAfter()"
        @edit="edit()"
        @remove="remove()"
        @delete="deleteCard()"
        :items="menuItems">
      </app-drop-down-menu>

      <div v-if="inSection !== null" class="w3-card w3-white drop-menu">
        <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> Are you sure you want to remove the section "{{ inSection.title }}" as a subsection of "{{ inSection.title }}"?
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
              <b>Warning:</b> Are you sure you want to completely delete the section "{{ inSection.title }}"? This will delete it from all the sections in which it is a subsection.
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
      deleteIntent: false,
      removeIntent: false
    }
  },

  computed: {
    menuItems () {
      return [
        { text: 'edit', value: 'edit', faIcon: 'fa-pencil' },
        { text: 'add card before', value: 'addCardBefore', faIcon: 'fa-plus' },
        { text: 'add card after', value: 'addCardAfter', faIcon: 'fa-plus' },
        { text: 'remove', value: 'remove', faIcon: 'fa-times' },
        { text: 'delete', value: 'delete', faIcon: 'fa-times' }
      ]
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
    },
    remove () {
      this.removeIntent = true
    },
    deleteCard () {
      this.deleteIntent = true
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
      this.axios.delete('/1/model/cardWrapper/' + this.card.id)
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
  z-index: 1;
}

.delete-intent-div {
  padding: 6px 6px;
}

</style>
