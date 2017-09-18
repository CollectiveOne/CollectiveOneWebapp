<template lang="html">
  <div class="modal-buttons-container">
    <div class="slider-container">
      <transition name="slideDownUp">
        <div v-if="show" class="w3-row buttons-row w3-margin-bottom">
          <button class="w3-right w3-button app-button app-margin-left"
            @click="$emit('edit')">
            <i class="fa fa-pencil" aria-hidden="true"></i> edit
          </button>
          <button class="w3-right w3-button app-button app-margin-left"
            @click="removeIntent = true">
            <i class="fa fa-times" aria-hidden="true"></i> remove
          </button>
          <button class="w3-right w3-button app-button app-button-danger"
            @click="deleteIntent = true">
            <i class="fa fa-trash" aria-hidden="true"></i> delete
          </button>
        </div>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> {{ removeMessage }}
            </p>
          </div>
          <button
            class="w3-button app-button-light"
            @click="removeIntent = false">cancel
          </button>
          <button
            class="w3-button app-button-danger app-margin-left"
            @click="$emit('remove')">confirm
          </button>
        </div>
      </transition>
    </div>

    <div class="slider-container">
      <transition name="slideDownUp">
        <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p>
              <b>Warning:</b> {{ deleteMessage }}
            </p>
          </div>
          <button
            class="w3-button app-button-light"
            @click="deleteIntent = false">cancel
          </button>
          <button
            class="w3-button app-button-danger app-margin-left"
            @click="$emit('delete')">confirm
          </button>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
export default {

  props: {
    show: {
      type: Boolean,
      default: true
    },
    deleteMessage: {
      type: String,
      default: ''
    },
    removeMessage: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      deleteIntent: false,
      removeIntent: false
    }
  }

}
</script>

<style scoped>
</style>
