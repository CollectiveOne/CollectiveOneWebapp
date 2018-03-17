<template lang="html">
  <div class="modal-buttons-container">

    <button class="w3-button w3-xlarge expand-btn"
      @click="expanded =! expanded">
      <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
    </button>

    <div v-if="expanded" class="w3-card w3-white buttons-container">
      <div class="w3-button"
        @click="$emit('edit')">
        <i class="fa fa-pencil" aria-hidden="true"></i> edit <small>(ctr + shf + e)</small>
      </div>
      <div
        v-if="!hideRemove"
        class="w3-button"
        @click="removeIntent = !removeIntent; deleteIntent = false;">
        <i class="fa fa-times" aria-hidden="true"></i> remove <small></small>
      </div>
      <div class="w3-button danger-btn"
        @click="deleteIntent = !deleteIntent; removeIntent = false;">
        <i class="fa fa-trash" aria-hidden="true"></i> delete
      </div>
      <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
        <div class="w3-padding w3-round light-grey w3-margin-bottom">
          <p>
            <b>Warning:</b> {{ removeMessage }}
          </p>
        </div>
        <button
          class="w3-button light-grey"
          @click="removeIntent = false">cancel
        </button>
        <button
          class="w3-button button-blue"
          @click="$emit('remove')">confirm
        </button>
      </div>

      <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
        <div class="w3-padding w3-round light-grey w3-margin-bottom">
          <p>
            <b>Warning:</b> {{ deleteMessage }}
          </p>
        </div>
        <button
          class="w3-button light-grey"
          @click="deleteIntent = false">cancel
        </button>
        <button
          class="w3-button danger-btn"
          @click="$emit('delete')">confirm
        </button>
      </div>
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
    },
    hideRemove: {
      type: Boolean,
      default: false
    }
  },

  data () {
    return {
      expanded: false,
      deleteIntent: false,
      removeIntent: false
    }
  },

  methods: {
    atKeydown (e) {
      /* e */
      if (e.keyCode === 69 && e.ctrlKey && e.shiftKey) {
        this.$emit('edit')
      }
    }
  },

  mounted () {
    window.addEventListener('keydown', this.atKeydown)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }

}
</script>

<style scoped>

.expand-btn {
  width: 55px;
}

.buttons-container {
  position: absolute;
  width: 200px;
  margin-top: 10px;
  margin-left: -120px;
  text-align: left;
  z-index: 2;
}

.buttons-container .w3-button {
  width: 100%;
  text-align: left;
}

.buttons-container .button-row:hover {
  background-color: rgb(122, 94, 133);
}

</style>
