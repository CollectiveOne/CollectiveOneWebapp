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
              @edit="startEditing()"
              @delete="deleteCard()"
              deleteMessage="This will delete the card from all the sections in which it is used."
              @remove="removeCard()"
              :removeMessage="'This will remove this card from the ' + inSectionTitle + ' section.'">
            </app-model-modal-buttons>
          </div>
          <div v-if="!editing" class="w3-right w3-button w3-xlarge" @click="copyUrl()">
            <i class="fa fa-share-alt" aria-hidden="true"></i>
          </div>
          <div class="slider-container url-show">
            <transition name="slideDownUp">
              <div v-if="showUrl" class="">
                <input id="copy-url" class="w3-input w3-border" type="text" name="" :value="cardUrl">
                <div class="w3-center"><small><i>please copy/paste the link above</i></small></div>
              </div>
            </transition>
          </div>
        </div>

        <div class="w3-container w3-border-bottom">
          <h3>{{ isNew ? 'New Card' : 'Model Card'}}</h3>
        </div>

        <div v-if="!loading" class="div-modal-content w3-container">

          <div v-if="isNew" class="w3-row">
            <label class=""><b>In Section:</b></label>
            <br>
            <h4>{{ this.inSectionTitleOk }}</h4>
          </div>

          <div v-if="isNew" class="section-tabs w3-row w3-center light-grey">
            <div class="w3-col s6 w3-bottombar w3-hover-light-grey cursor-pointer"
              :class="{'border-blue': !addExisting}"
              @click="addExisting = false">
              <h5 class="noselect" :class="{'bold-text': !addExisting}">Create New</h5>
            </div>
            <div class="w3-col s6 w3-bottombar w3-hover-light-grey cursor-pointer"
              :class="{'border-blue': addExisting}"
              @click="addExisting = true">
              <h5 class="noselect" :class="{'bold-text': addExisting}">Add Existing</h5>
            </div>
          </div>

          <div class="slider-container">
            <transition name="slideLeftRight">
              <div v-if="addExisting && editing" class="">
                <app-model-card-selector
                  :initiativeId="initiativeId"
                  @select="cardSelected($event)">
                </app-model-card-selector>
                <app-error-panel
                  :show="noCardSelectedShow"
                  message="please select one card from above">
                </app-error-panel>
              </div>
            </transition>
          </div>

          <div :class="{'slider-container': animatingTab}">
            <transition name="slideRightLeft"
              @before-enter="animatingTab = true"
              @after-enter="animatingTab = false"
              @before-leave="animatingTab = true"
              @after-leave="animatingTab = false">

              <div v-if="!addExisting">

                <div v-if="!isNew">
                  <div class="w3-row">
                    <div class="w3-left w3-margin-right">
                      <i>in:</i>
                    </div>
                    <div v-for="inSection in cardWrapper.inSections" class="insection-tag-container w3-left">
                      <router-link :to="{ name: 'ModelSection', params: { sectionId: inSection.id } }"
                        class="gray-1 w3-tag w3-round w3-small">
                        {{ inSection.title }}
                      </router-link>
                    </div>
                    <div v-if="editing" v-for="toSection in addToSections" class="insection-tag-container w3-left w3-display-container w3-margin-left">
                      <div class="success-background w3-tag w3-round w3-small">
                        {{ toSection.title }}
                      </div>
                      <div @click="removeToSection(toSection)"
                        class="remove-tag-icon success-color w3-display-right cursor-pointer">
                        <i class="fa fa-times-circle"></i>
                      </div>
                    </div>
                    <div v-if="editing" class="w3-left w3-margin-left">
                      <div v-if="!addToSection"
                        @click="addToSection = !addToSection"
                        class="w3-tag button-blue w3-small w3-round cursor-pointer">
                        add to another section
                      </div>
                    </div>
                  </div>

                  <div class="w3-row">
                    <div class="slider-container">
                      <transition name="slideDownUp">
                        <app-model-section-selector
                          v-if="addToSection"
                          :initiativeId="cardWrapper.initiativeId"
                          @select="sectionSelected($event)">
                        </app-model-section-selector>
                      </transition>
                    </div>
                  </div>

                  <div v-if="addToSection" class="w3-row w3-margin-top w3-center">
                    <button @click="addToSection = false"
                      class="w3-button app-button" type="button" name="button">
                      Cancel add to section
                    </button>
                  </div>

                </div>

                <div v-if="!editing" class="">
                  <div v-if="card.imageFile" class="w3-row w3-margin-top image-container w3-center w3-display-container w3-card-2 cursor-pointer">
                    <img @click="showImageClick()" :src="card.imageFile.url + '?lastUpdated='+ card.imageFile.lastUpdated" alt="">
                  </div>
                </div>
                <div v-if="editing" class="">
                  <div class="w3-row w3-margin-top image-container w3-center w3-display-container w3-card-2">
                    <div v-if="uploadingImage" class="loader-gif-container">
                      <img class="loader-gif" src="../../../assets/loading.gif" alt="">
                    </div>
                    <div v-if="!uploadingImage" class="">
                      <img v-if="editedCard.imageFile" @click="showImageClick()" :src="editedCard.imageFile.url + '?lastUpdated='+ editedCard.imageFile.lastUpdated" alt="">
                    </div>
                    <div v-if="!uploadingImage" class="w3-display-middle">
                      <input class="inputfile" @change="newFileSelected($event)"
                        type="file" name="imageFile" id="imageFile" accept="image/*">
                      <label for="imageFile" class="w3-button app-button">{{ editedCard.imageFile ? 'change' : 'upload image' }}</label>
                      <button v-if="editedCard.imageFile"
                        @click="removeImage()"
                        class="w3-button app-button-danger">remove</button>
                    </div>
                  </div>
                  <app-error-panel
                    :show="errorUploadingFile"
                    :message="errorUploadingFileMsg">
                  </app-error-panel>
                </div>

                <div class="slider-container">
                  <transition name="slideDownUp">
                    <div v-if="showImage" class="image-display-modal">
                      <div class="image-display w3-display-container">
                        <img v-if="card.imageFile"
                          :src="card.imageFile.url + '?lastUpdated='+ card.imageFile.lastUpdated" alt="">
                        <div class="w3-display-topright w3-xlarge cursor-pointer close-image-div" @click="showImage = false">
                          <i class="fa fa-times" aria-hidden="true"></i>
                        </div>
                      </div>
                    </div>
                  </transition>
                </div>

                <div class="w3-row w3-margin-top">
                  <div v-if="!editing" class="">
                    <div v-if="card.title !== ''" class="">
                      <h3><b>{{ card.title }}</b></h3>
                    </div>
                    <div v-else class="">
                      <i>(empty)</i>
                    </div>
                  </div>
                  <div v-else class="">
                    <label class=""><b>Title:</b></label>
                    <input type="text" class="w3-input w3-hover-light-grey" v-model="editedCard.title">
                    <app-error-panel
                      :show="titleEmptyShow"
                      message="please add a title">
                    </app-error-panel>
                    <app-error-panel
                      :show="titleTooLongShow"
                      message="name too long">
                    </app-error-panel>
                  </div>
                </div>

                <div class="w3-row">
                  <div v-if="!editing" class="">
                    <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
                  </div>
                  <div v-else class="w3-margin-top">
                    <label class=""><b>Text: <span v-if="editing" class="w3-small error-text">(required)</span></b></label>
                    <app-markdown-editor v-model="editedCard.text"></app-markdown-editor>
                    <app-error-panel
                      :show="textErrorShow"
                      message="please include the text of this card">
                    </app-error-panel>
                  </div>
                </div>

                <hr>

                <div class="">
                  <div v-if="!editing" class="">
                    <div v-if="cardWrapper.stateControl" class="w3-row-padding">
                      <div class="w3-col m6 w3-margin-bottom">
                        <label class=""><b>State:</b></label><br>
                        <div class="w3-tag gray-1 w3-padding w3-round state-tag">
                          {{ cardWrapper.state }}
                        </div>
                      </div>
                      <div v-if="targetDateStr !== ''" class="w3-col m6">
                        <label class=""><b>Target date:</b></label>
                        <input class="w3-input" :value="dateString(this.cardWrapper.targetDate)" disabled>
                      </div>
                    </div>
                  </div>
                  <div v-else class="">
                    <button
                      @click="editedCard.stateControl = !editedCard.stateControl"
                      class="w3-button app-button state-enable-button">
                      {{ editedCard.stateControl ? 'Remove state and deadline' : 'Set state and deadline' }}
                    </button>
                    <transition name="fadeenter">
                      <div v-if="editedCard.stateControl" class="w3-row-padding w3-margin-top">
                        <div class="w3-col m6 w3-margin-bottom">
                          <label class=""><b>State:</b></label>
                          <select class="w3-select" v-model="editedCard.state">
                            <option>PLAN</option>
                            <option>REALITY</option>
                          </select>
                        </div>
                        <div class="w3-col m6">
                          <label class=""><b>Target date:</b></label>
                          <datepicker
                            :value="targetDateStr"
                            @selected="targetDateSelected($event)">
                          </datepicker>
                        </div>
                      </div>
                    </transition>
                  </div>
                </div>
              </div>
            </transition>
          </div>

          <br>
          <div v-if="!isNew && !editing" class="">
            <app-message-thread
              contextType="MODEL_CARD"
              :contextElementId="cardWrapperId"
              :onlyMessagesInit="onlyMessages"
              :url="'/1/activity/model/card/' + cardWrapperId">
            </app-message-thread>
          </div>

          <div v-if="editing || addExisting" class="modal-bottom-btns-row w3-row-padding">
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
import { dateString } from '@/lib/common.js'
import Datepicker from 'vuejs-datepicker'
import ModelModalButtons from '@/components/model/modals/ModelModalButtons.vue'
import ModelCardSelector from '@/components/model/ModelCardSelector.vue'
import ModelSectionSelector from '@/components/model/ModelSectionSelector.vue'
import MessageThread from '@/components/notifications/MessageThread.vue'

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons,
    'datepicker': Datepicker,
    'app-model-card-selector': ModelCardSelector,
    'app-message-thread': MessageThread,
    'app-model-section-selector': ModelSectionSelector
  },

  props: {
    isNew: {
      type: Boolean,
      default: false
    },
    initiativeId: {
      type: String,
      default: ''
    },
    cardWrapperId: {
      type: String,
      default: ''
    },
    inSectionId: {
      type: String,
      default: ''
    },
    inSectionTitle: {
      type: String,
      default: ''
    },
    ixInSection: {
      type: Number,
      default: -1
    },
    onlyMessages: {
      type: Boolean,
      defaul: false
    }
  },

  data () {
    return {
      cardWrapper: {
        id: '',
        stateControl: false,
        card: {
          title: '',
          text: ''
        }
      },
      editedCard: null,
      inSectionTitleOk: '',
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      textEmptyError: false,
      targetDateStr: '',
      addExisting: false,
      existingCard: null,
      noCardSelectedError: false,
      animatingTab: false,
      showUrl: false,
      showImage: false,
      imageUpdated: false,
      newImageFileId: '',
      uploadingImage: false,
      errorUploadingFile: false,
      errorUploadingFileMsg: '',
      enableClickOutside: false,
      sendingData: false,
      loading: false,
      addToSection: false,
      addToSections: [],
      closeIntent: false
    }
  },

  computed: {
    showSelectFile () {
      return this.editing && this.changeImage
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    card () {
      return this.cardWrapper.card
    },
    showStateControl () {
      if (this.editing) {
        return this.editedCard.stateControl
      } else {
        return this.cardWrapper.stateControl
      }
    },
    titleEmpty () {
      return this.editedCard.title === ''
    },
    titleTooLong () {
      return this.editedCard.title.length > 30
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
    textEmpty () {
      return this.editedCard.text === ''
    },
    textErrorShow () {
      return this.textEmptyError && this.textEmpty
    },
    noCardSelected () {
      return this.existingCard === null
    },
    noCardSelectedShow () {
      return this.noCardSelectedError && this.noCardSelected
    },
    cardUrl () {
      return window.location.host + '/#/app/inits/' + this.initiativeId +
      '/model/section/' + this.inSectionId +
      '/card/' + this.cardWrapper.id
    }
  },

  methods: {
    copyUrl () {
      if (!this.showUrl) {
        this.showUrl = true
        this.$nextTick(() => {
          var copyTextarea = document.querySelector('#copy-url')
          copyTextarea.select()
          document.execCommand('copy')
          console.log('copied')
        })
      } else {
        this.showUrl = false
      }
    },
    newFileSelected (event) {
      /* upload image */
      var data = new FormData()
      data.append('file', event.target.files[0])

      this.uploadingImage = true
      this.errorUploadingFile = false

      this.axios.post('/1/upload/cardImage/' + this.cardWrapper.id, data).then((response) => {
        this.uploadingImage = false

        if (response.data.result === 'success') {
          this.editedCard.newImageFileId = response.data.elementId
          return this.axios.get('/1/files/' + this.editedCard.newImageFileId)
        } else {
          this.errorUploadingFile = true
          this.errorUploadingFileMsg = response.data.message
        }
      }).then((response) => {
        this.editedCard.imageFile = response.data.data
      })
    },
    dateString (v) {
      return dateString(v)
    },
    update () {
      this.loading = true
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.cardWrapper = response.data.data
        this.loading = false
      })
    },
    updateInSection () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSectionId)
      .then((response) => {
        this.inSectionTitleOk = response.data.data.title
      })
    },
    cardSelected (cardWrapper) {
      this.existingCard = cardWrapper
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
    startEditing () {
      this.addToSections = []
      this.editedCard = JSON.parse(JSON.stringify(this.card))

      if (this.cardWrapper.targetDate) {
        if (this.cardWrapper.targetDate > 0) {
          this.targetDateStr = new Date(this.cardWrapper.targetDate)
        }
      }

      this.editedCard.stateControl = this.cardWrapper.stateControl
      this.editedCard.state = this.cardWrapper.state
      this.editedCard.targetDate = this.cardWrapper.targetDate

      this.editing = true
    },
    targetDateSelected (dateStr) {
      this.targetDateStr = dateStr
      var date = new Date(dateStr)
      this.editedCard.targetDate = date.getTime()
    },
    accept () {
      // console.log('clicked accept')
      var ok = true

      if (!this.addExisting) {
        if (this.titleTooLong) {
          ok = false
        }

        if (this.textEmpty) {
          ok = false
          this.textEmptyError = true
        }
      } else {
        if (this.noCardSelected) {
          ok = false
          this.noCardSelectedError = true
        }
      }

      if (ok) {
        var cardDto = JSON.parse(JSON.stringify(this.editedCard))

        var responseF = (response) => {
          this.sendingData = false
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.commit('triggerUpdateModel')
          }
        }

        if (this.isNew) {
          if (!this.addExisting) {
            /* create new card */
            cardDto.ixInSection = this.ixInSection

            this.sendingData = true
            this.axios.post('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSectionId + '/cardWrapper', cardDto).then(responseF).catch((error) => {
              console.log(error)
            })
          } else {
            this.sendingData = true
            this.axios.put('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSectionId + '/cardWrapper/' + this.existingCard.id,
              {}).then(responseF).catch((error) => {
                console.log(error)
              })
          }
        } else {
          /* editing */

          /* add the new sections */
          if (this.addToSections.length > 0) {
            cardDto.inSections = this.addToSections
          }

          this.sendingData = true
          this.axios.put('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id, cardDto)
          .then(responseF).catch((error) => {
            console.log(error)
          })
        }
      }
    },
    removeCard () {
      var responseF = (response) => {
        if (response.data.result === 'success') {
          this.closeThis()
          this.$store.commit('triggerUpdateModel')
        } else {
          this.showOutputMessage(response.data.message)
        }
      }
      this.axios.put('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSectionId + '/removeCard/' + this.cardWrapper.id,
        {}).then(responseF).catch((error) => {
          console.log(error)
        })
    },
    deleteCard () {
      this.axios.delete('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.$store.commit('triggerUpdateModel')
        this.closeThis()
      }).catch((error) => {
        console.log(error)
      })
    },
    showImageClick () {
      if (!this.editing) {
        this.showImage = true
      }
    },
    removeImage () {
      this.editedCard.imageFile = null
      this.editedCard.newImageFileId = 'REMOVE'
    },
    sectionSelected (section) {
      this.addToSection = false

      /* prevent adding in an existing section */
      for (var ix1 in this.cardWrapper.inSections) {
        if (this.cardWrapper.inSections[ix1].id === section.id) {
          return
        }
      }

      /* prevent adding twice the same section */
      for (var ix2 in this.addToSections) {
        if (this.addToSections[ix2].id === section.id) {
          return
        }
      }

      this.addToSections.push(section)
    },
    removeToSection (section) {
      var ix = -1
      for (var ixS in this.addToSections) {
        if (this.addToSections[ixS].id === section.id) {
          ix = ixS
        }
      }

      if (ix !== -1) {
        this.addToSections.splice(ix, 1)
      }
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
    this.inSectionTitleOk = this.inSectionTitle

    if (this.isNew) {
      this.editedCard = {
        stateControl: false,
        title: '',
        text: ''
      }
      this.editing = true
      this.updateInSection()
    } else {
      this.showEditButtons = true
      this.cardWrapper.id = this.cardWrapperId
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

.url-show {
  position: absolute;
  margin-top: 60px;
  margin-left: -100px;
  width: 250px;
}

.not-add-existing-container {
  overflow: visible;
}

.inputfile {
	width: 0.1px;
	height: 0.1px;
	opacity: 0;
	overflow: hidden;
	position: absolute;
	z-index: -1;
}

.loader-gif-container {
  padding-top: 30px;
  padding-bottom: 30px;
}

.insection-tag-container {
  margin-left: 6px;
}

.image-container {
  min-height: 80px;
  max-height: 250px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.image-display-modal {
  position: fixed;
  top: 5%;
  left: 5%;
  width: 90%;
  height: 90%;
  text-align: center;
  background-color: white;
  border-radius: 20px;
  z-index: 10;
}

.image-display {
  width: 100%;
  height: 100%;
  padding: 10px;
}

.image-display img {
  zoom: 2;  //increase if you have very small images
  display: block;
  margin: auto;
  height: auto;
  max-height: 100%;
  width: auto;
  max-width: 100%;
  border-radius: 5px;
}

.close-image-div {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 20px;
  width: 50px;
  margin-top: -10px;
  margin-right: -10px;
  text-align: center;
}

.state-enable-button {
  width: 250px;
}

.state-tag {
  width: 100%;
}

.remove-tag-icon {
  margin-top: -5px;
  margin-right: -10px;
  z-index: 10;
}

</style>
