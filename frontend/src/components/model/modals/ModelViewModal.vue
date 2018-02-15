<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card w3-display-container"
        v-click-outside="clickOutside">

        <div class="w3-display-topright">
          <div class="w3-right w3-button w3-xlarge" @click="closeThisConfirm()">
            <i class="fa fa-times" aria-hidden="true"></i>
          </div>
          <div class="w3-right">
            <app-model-modal-buttons
              v-if="isLoggedAnEditor && !editing"
              :show="showEditButtons"
              :hideRemove="true"
              deleteMessage="This will delete the view. Its sections and cards will not be deleted."
              @edit="startEditing()"
              @delete="deleteView()">
            </app-model-modal-buttons>
          </div>
        </div>

        <div v-if="!loading" class="w3-container">

          <div class="w3-row w3-border-bottom">
            <h3>{{ isNew ? 'New View' : 'Model View' }}</h3>
          </div>

          <div class="w3-row div-modal-content">

            <div v-if="!editing" class="w3-row">
              <h3><b>{{ view.title }}</b></h3>
            </div>
            <div v-else class="w3-row">
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

            <div class="w3-row">
              <div v-if="!editing" class="">
                <vue-markdown class="marked-text" :source="view.description"></vue-markdown>
              </div>
              <div v-else class="w3-margin-top">
                <app-markdown-editor v-model="editedView.description"></app-markdown-editor>
                <app-error-panel
                  :show="descriptionErrorShow"
                  message="please include a description of this section">
                </app-error-panel>
              </div>
            </div>

            <br>
            <div v-if="!isNew && !editing" class="">
              <app-message-thread
                contextType="MODEL_VIEW"
                :contextElementId="viewId"
                :onlyMessagesInit="onlyMessages"
                :url="'/1/activity/model/view/' + viewId">
              </app-message-thread>
            </div>

            <div v-if="editing" class="modal-bottom-btns-row w3-row-padding">
              <hr>
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button-light" @click="cancel()">Cancel <span><small>(Esc)</small></span></button>
              </div>
              <div class="w3-col m6 w3-center">
                <button v-show="!sendingData" type="button" class="w3-button app-button" @click="accept()">Accept <span><small>(Ctr + Enter)</small></span></button>
                <div v-show="sendingData" class="sending-accept light-grey">
                  <img class="" src="../../../assets/loading.gif" alt="">
                </div>
              </div>
            </div>

          </div>
        </div>
        <div v-else class="w3-row w3-center loader-gif-container">
          <img class="loader-gif" src="../../../assets/loading.gif" alt="">
        </div>

        <div v-if="closeIntent" class="w3-display-middle w3-card w3-white w3-padding w3-round-large w3-center">
          You are currently editing this view. Are you sure you want to close it? Any changes would get lost.
          <div class="w3-row w3-margin-top">
            <button class="w3-button app-button-light" name="button"
              @click="closeIntent = false">
              Cancel
            </button>
            <button class="w3-button app-button" name="button"
              @click="closeThis()">
              Confirm
            </button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ModelModalButtons from '@/components/model/modals/ModelModalButtons.vue'
import MessageThread from '@/components/notifications/MessageThread.vue'

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons,
    'app-message-thread': MessageThread
  },

  props: {
    isNew: {
      type: Boolean,
      defaul: false
    },
    viewId: {
      type: String,
      default: ''
    },
    initiativeId: {
      type: String,
      default: ''
    },
    onlyMessages: {
      type: Boolean,
      defaul: false
    }
  },

  data () {
    return {
      view: {
        title: '',
        description: ''
      },
      editedView: null,
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      descriptionEmptyError: false,
      sendingData: false,
      loading: false,
      closeIntent: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
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
    update () {
      this.loading = true
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/view/' + this.view.id).then((response) => {
        this.loading = false
        this.view = response.data.data
      })
    },
    startEditing () {
      this.editedView = JSON.parse(JSON.stringify(this.view))
      this.editing = true
    },
    clickOutside () {
      if (this.enableClickOutside) {
        this.closeThisConfirm()
      }
    },
    closeThisConfirm () {
      if (this.editing && !this.closeIntent) {
        this.closeIntent = true
      } else {
        this.closeThis()
      }
    },
    closeThis () {
      this.$emit('close')
    },
    cancel () {
      if (this.isNew) {
        this.closeThis()
      } else {
        this.editing = false
      }
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
        var viewDto = JSON.parse(JSON.stringify(this.editedView))
        var returnF = (response) => {
          this.sendingData = false
          if (response.data.result === 'success') {
            if (this.isNew) {
              this.$router.push({ name: 'ModelView', params: { viewId: response.data.elementId } })
            }
            this.$store.dispatch('refreshModelViews', { router: null, redirect: false })
            this.closeThis()
            this.editing = false
          } else {
            this.showOutputMessage(response.data.message)
          }
        }

        if (this.isNew) {
          this.sendingData = true
          this.axios.post('/1/initiative/' + this.initiativeId + '/model/view', viewDto).then(returnF).catch((error) => {
            console.log(error)
          })
        } else {
          this.sendingData = true
          this.axios.put('/1/initiative/' + this.initiativeId + '/model/view/' + this.viewId, viewDto).then(returnF).catch((error) => {
            console.log(error)
          })
        }
      }
    },
    deleteView () {
      this.axios.delete('/1/initiative/' + this.initiativeId + '/model/view/' + this.view.id).then((response) => {
        this.closeThis()
        this.$store.dispatch('refreshModelViews', { router: this.$router, redirect: true })
      }).catch((error) => {
        console.log(error)
      })
    },
    atKeydown (e) {
      if (!this.editing) {
        /* esc */
        if (e.keyCode === 27) {
          this.closeThis()
        }
      }

      if (this.editing) {
        /* esc */
        if (e.keyCode === 27) {
          this.cancel()
        }

        /* ctr + enter */
        if (e.keyCode === 13 && e.ctrlKey) {
          e.preventDefault()
          this.accept()
        }
      }
    }
  },

  mounted () {
    if (this.isNew) {
      this.editedView = {
        title: '',
        description: ''
      }
      this.editing = true
    } else {
      this.showEditButtons = true
      this.view.id = this.viewId
      this.update()
    }

    setTimeout(() => {
      this.enableClickOutside = true
    }, 1000)

    window.addEventListener('keydown', this.atKeydown)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }
}
</script>

<style scoped>

.buttons-row button, .delete-intent-div button {
  width: 90px;
}

</style>
