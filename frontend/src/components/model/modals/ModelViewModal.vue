<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div v-if="view" class="w3-container">

          <div class="w3-row w3-border-bottom">
            <h2>Model View</h2>
          </div>

          <div class="w3-row div-modal-content">

            <app-model-modal-buttons
              :show="true"
              @edit="startEditing()"
              @delete="deleteView()">
            </app-model-modal-buttons>

            <label class=""><b>Title:</b></label>
            <div v-if="!editing" class="w3-padding light-grey">
              {{ view.title }}
            </div>
            <div v-else class="">
              <input type="text" class="w3-input w3-hover-light-grey" v-model="editedView.title">
              <app-error-panel
                :show="titleEmptyShow"
                message="please add a title">
              </app-error-panel>
              <app-error-panel
                :show="titleTooLongShow"
                message="name too long">
              </app-error-panel>
            </div>

            <br>
            <label class=""><b>Description:</b></label>
            <div class="">
              <div v-if="!editing" class="w3-padding light-grey">
                {{ view.description }}
              </div>
              <div v-else class="">
                <textarea type="text" class="w3-input w3-border w3-round w3-hover-light-grey" v-model="editedView.description"></textarea>
                <app-error-panel
                  :show="descriptionErrorShow"
                  message="please include a description of this section">
                </app-error-panel>
              </div>
            </div>

            <div v-if="editing" class="modal-bottom-btns-row w3-row-padding">
              <hr>
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button-light" @click="editing = false">Cancel</button>
              </div>
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
              </div>
            </div>

          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ModelModalButtons from '@/components/model/modals/ModelModalButtons.vue'

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons
  },

  props: {
    viewId: {
      type: String,
      default: ''
    },
    initiativeId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      view: null,
      editedView: null,
      editing: false,
      titleEmptyError: false,
      descriptionEmptyError: false
    }
  },

  computed: {
    titleEmpty () {
      return this.editedView.title === ''
    },
    titleTooLong () {
      return this.editedView.title.length > 30
    },
    titleErrorShow () {
      return this.titleEmptyShow || this.titleTooLongShow
    },
    titleEmptyShow () {
      return this.titleEmptyError && this.titleEmpty
    },
    titleTooLongShow () {
      return this.titleTooLong
    },
    descriptionEmpty () {
      return this.editedView.description === ''
    },
    descriptionErrorShow () {
      return this.descriptionEmptyError && this.descriptionEmpty
    }
  },

  methods: {
    updateViewData () {
      this.axios.get('/1/secured/initiative/' + this.initiativeId + '/model/view/' + this.viewId).then((response) => {
        this.view = response.data.data
      })
    },
    startEditing () {
      this.editedView = JSON.parse(JSON.stringify(this.view))
      this.editing = true
    },
    closeThis () {
      this.$emit('close')
    },
    accept () {
      var ok = true

      if (this.titleEmpty) {
        ok = false
        this.titleEmptyError = true
      } else {
        if (this.titleTooLong) {
          ok = false
        }
      }

      if (this.descriptionEmpty) {
        ok = false
        this.descriptionEmptyError = true
      }

      if (ok) {
        let viewDto = {
          id: this.view.id,
          title: this.editedView.title,
          description: this.editedView.description
        }

        this.axios.put('/1/secured/initiative/' + this.initiativeId + '/model/view', viewDto).then((response) => {
          if (response.data.result === 'success') {
            this.updateViewData()
            this.editing = false
            this.$store.dispatch('refreshModel')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
          console.log(error)
        })
      }
    },
    deleteView () {
      this.axios.delete('/1/secured/initiative/' + this.initiativeId + '/model/view/' + this.view.id).then((response) => {
        this.$store.dispatch('refreshModel')
        this.closeThis()
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.updateViewData()
  }
}
</script>

<style scoped>

.buttons-row button, .delete-intent-div button {
  width: 90px;
}

</style>
