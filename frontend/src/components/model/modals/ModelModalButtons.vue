<template lang="html">
  <div class="modal-buttons-container">

    <popper :append-to-body="true" trigger="click" :options="popperOptions" class="">
      <div class="drop-down-menu w3-white w3-row">
        <app-drop-down-menu
          class="drop-menu"
          @edit="$emit('edit')"
          @remove="removeIntent = !removeIntent; deleteIntent = false;"
          @delete="deleteIntent = !deleteIntent; removeIntent = false;"
          :items="editMenuItems">
        </app-drop-down-menu>

        <div v-if="removeIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p v-html="$t('model.REMOVE_CARD_WARNING', { title: inSectionTitle })">
            </p>
          </div>
          <button
            class="w3-button light-grey"
            @click="removeIntent = false">{{ $t('general.CANCEL') }}
          </button>
          <button
            class="w3-button button-blue"
            @click="$emit('remove')">{{ $t('general.CONFIRM') }}
          </button>
        </div>

        <div v-if="deleteIntent" class="w3-row w3-center delete-intent-div">
          <div class="w3-padding w3-round light-grey w3-margin-bottom">
            <p v-html="$t('model.DELETE_CARD_WARNING')">
            </p>
          </div>
          <button
            class="w3-button light-grey"
            @click="deleteIntent = false">{{ $t('general.CANCEL') }}
          </button>
          <button
            class="w3-button danger-btn"
            @click="$emit('delete')">{{ $t('general.CONFIRM') }}
          </button>
        </div>
      </div>

      <button slot="reference" class="w3-button w3-xlarge expand-btn">
        <i class="fa fa-ellipsis-v" aria-hidden="true"></i>
      </button>

    </popper>
  </div>
</template>

<script>
export default {

  props: {
    show: {
      type: Boolean,
      default: true
    },
    inSectionTitle: {
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

  computed: {
    editMenuItems () {
      let items = []

      items.push({
        text: this.$t('general.EDIT'),
        value: 'edit',
        faIcon: 'fa-plus'
      })

      items.push({
        text: this.$t('general.REMOVE'),
        value: 'remove',
        faIcon: 'fa-times'
      })

      items.push({
        text: this.$t('general.DELETE'),
        value: 'delete',
        faIcon: 'fa-trash'
      })

      return items
    },
    popperOptions () {
      return {
        positionFixed: true,
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

.drop-down-menu {
  width: 190px;
  z-index: 11;
}

.delete-intent-div {
  padding-bottom: 6px;
}

.drop-down-menu .w3-button {
  width: calc(50% - 6px);
}

</style>
