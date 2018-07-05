<template lang="html">
  <div :class="cClass"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="">
      <div v-if="hasImage || uploadingImage" class="w3-row image-container w3-center w3-display-container">
        <div v-if="uploadingImage" class="loader-gif-container">
          <img class="loader-gif" src="../../../assets/loading.gif" alt="">
        </div>
        <div v-if="!uploadingImage" class="">
          <img v-if="hasImage" @click="showImageClick()" :src="editedCard.imageFile.url + '?lastUpdated='+ editedCard.imageFile.lastUpdated" alt="">
        </div>
        <div v-if="!uploadingImage" class="w3-display-middle">
          <input class="inputfile" @change="newFileSelected($event)"
          type="file" name="imageFile" id="imageFile" accept="image/*">
          <label for="imageFile" class="w3-button app-button w3-rounded">{{ editedCard.imageFile ? 'change' : 'upload image' }}</label>
          <button v-if="hasImage"
            @click="removeImage()"
            class="w3-button app-button-danger">
            remove
          </button>
        </div>
      </div>
      <app-error-panel
        :show="errorUploadingFile"
        :message="errorUploadingFileMsg">
      </app-error-panel>
    </div>

    <div>
      <div class="cursor-pointer" :class="inputClass">
        <div class="w3-row input-border">
          <input type="text" class="w3-input w3-hover-light-grey" placeholder="title" v-model="editedCard.title">
        </div>
        <div ref="cardText" class="w3-row card-text">
          <app-markdown-editor placeholder="content" v-model="editedCard.text" :keepBackup="false"></app-markdown-editor>
        </div>
      </div>

      <div :class="controlsClass">
        <div class="w3-left scope-controls">
          <div  class="w3-right cursor-pointer indicator-comp"
            @click="scope = 'COMMON'">
            <i class="fa fa-circle select-common highlight" aria-hidden="true"></i>
          </div>
          <div class="w3-right cursor-pointer indicator-comp"
            @click="scope = 'SHARED'">
            <i class="fa fa-circle select-shared highlight" aria-hidden="true"></i>
          </div>
          <div class="w3-right cursor-pointer indicator-comp"
            @click="scope = 'PRIVATE'">
            <i class="fa fa-circle select-private highlight" aria-hidden="true"></i>
          </div>
        </div>


        <div class="w3-right control-buttons">
          <div v-if="!hasImage" class="w3-right cursor-pointer indicator-comp">
            <input class="inputfile" @change="newFileSelected($event)"
            type="file" name="imageFile" id="imageFile" accept="image/*">
            <label for="imageFile" class="cursor-pointer">
              <i class="fa fa-picture-o highlight" aria-hidden="true"></i>
            </label>
          </div>

          <div v-if="!isNew" class="w3-right cursor-pointer w3-margin-right" @click="expandCard()">
            <i class="fa fa-expand highlight" aria-hidden="true"></i>
          </div>
        </div>

      </div>

      <div class="button-row send-button-container" :class="buttonClass">
        <div class="w3-row">
          <div class="w3-col m6">
            <button class="w3-button app-button-light" name="button" @click="$emit('edit', null)">
              cancel
            </button>
          </div>
          <div class="w3-col m6">
            <button v-show="!sendingData" class="w3-button app-button" name="button" @click="createCard()">
              {{ cardButtonText }}
            </button>
            <div v-show="sendingData" class="sending-accept light-grey">
              <img class="" src="../../../assets/loading.gif" alt="">
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import InModelSectionsTags from '@/components/model/InModelSectionsTags.vue'

export default {

  name: 'model-card-as-card',

  mixins: [ cardMixin ],

  components: {
    'app-in-model-sections-tags': InModelSectionsTags
  },

  props: {
    isNew: {
      type: Boolean,
      default: false
    },
    inSection: {
      type: Object,
      default: null
    },
    cardWrapper: {
      type: Object,
      default: null
    },
    type: {
      type: String,
      default: 'card'
    }
  },

  data () {
    return {
      editing: true,
      editedCard: {
        title: '',
        text: '',
        newScope: 'SHARED'
      },
      hovering: false,
      showFull: false,
      addExisting: false,
      sendingData: false,
      loading: false,
      scope: 'SHARED',
      uploadingImage: false,
      errorUploadingFile: false,
      errorUploadingFileMsg: ''
    }
  },

  computed: {
    titleEmpty () {
      if (this.editedCard) {
        return this.editedCard.title === ''
      }
      return false
    },
    titleTooLong () {
      if (this.editedCard) {
        return this.editedCard.title.length > 42
      }
      return false
    },
    textEmpty () {
      if (this.editedCard) {
        return this.editedCard.text === ''
      }
      return false
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
    titleErrorShow () {
      return this.titleEmptyShow || this.titleTooLongShow
    },
    titleEmptyShow () {
      return this.titleEmptyError && this.titleEmpty
    },
    titleTooLongShow () {
      return this.titleTooLong
    },
    atCardWrapper () {
      return this.cardWrapper === null ? null : this.cardWrapper.cardWrapper
    },
    cardWrapperIdForImage () {
      return this.isNew ? '' : this.atCardWrapper.id
    },
    newCardLocation () {
      return this.$store.state.support.createNewCardLocation === 'before'
    },
    cardButtonText () {
      return this.isNew ? 'Create' : 'Save'
    },
    hasImage () {
      return this.editedCard.imageFile != null
    },
    cClass () {
      let cClass = {}

      cClass['border-red'] = this.scope === 'PRIVATE'
      cClass['border-yellow'] = this.scope === 'SHARED'
      cClass['border-blue'] = this.scope === 'COMMON'

      cClass['w3-card-4 w3-topbar w3-round-large'] = this.type === 'card'
      cClass['card-summary-container w3-leftbar w3-card w3-row'] = this.type === 'summary'
      cClass['card-editor-doc'] = this.type === 'doc'

      return cClass
    },
    inputClass () {
      let inputClass = {}

      inputClass['w3-row card-container cursor-pointer limit-height'] = this.type === 'card'
      inputClass['w3-col m8 left-div'] = this.type === 'summary'
      inputClass['w3-row w3-col s12'] = this.type === 'doc'

      return inputClass
    },
    controlsClass () {
      let controlsClass = {}

      controlsClass['w3-row bottom-row light-grey'] = this.type === 'card'
      controlsClass['w3-col m4 right-div-top controls w3-display-bottom'] = this.type === 'summary'

      return controlsClass
    },
    buttonClass () {
      let buttonClass = {}

      buttonClass['w3-row light-grey'] = this.type === 'card'
      buttonClass['w3-col m4 right-div-bottom controls send-button-container-summary w3-display-container'] = this.type === 'summary'
      buttonClass['send-button-container-doc'] = this.type === 'doc'

      return buttonClass
    },
    card () {
      return this.atCardWrapper.card
    }
  },

  methods: {
    expandCard () {
      if (!this.inCardSelector) {
        this.$router.push({name: this.cardRouteName, params: { cardId: this.cardWrapperIdForImage }})
      }
    },
    removeImage () {
      this.editedCard.imageFile = null
      this.editedCard.newImageFileId = 'REMOVE'
    },
    newFileSelected (event) {
      /* upload image */
      var fileData = event.target.files[0]
      if (fileData.size > 1048576) {
        this.errorUploadingFile = true
        this.errorUploadingFileMsg = 'Image file too big. Must be below 1 MB'
        return
      }

      var data = new FormData()
      data.append('file', fileData)

      this.uploadingImage = true
      this.errorUploadingFile = false

      this.axios.post('/1/upload/cardImage/' + this.cardWrapperIdForImage, data).then((response) => {
        console.log(response)
        this.uploadingImage = false

        if (response.data.result === 'success') {
          this.editedCard.newImageFileId = response.data.elementId
          return this.axios.get('/1/files/' + this.editedCard.newImageFileId)
        } else {
          this.errorUploadingFile = true
          this.errorUploadingFileMsg = response.data.message
        }
      }).then((response) => {
        /* to force reactivity */
        var newEditedCard = JSON.parse(JSON.stringify(this.editedCard))
        newEditedCard.imageFile = response.data.data
        this.editedCard = newEditedCard
      })
    },
    showImageClick () {
      if (!this.editing) {
        this.showImage = true
      }
    },
    startEditing () {
      this.editedCard = JSON.parse(JSON.stringify(this.atCardWrapper.card))
      this.scope = this.atCardWrapper.scope
    },
    textTooLong () {
      if (!this.$refs.cardText) {
        return false
      }
      if (this.$refs.cardText.scrollHeight > 250) {
        return true
      } else {
        return this.$refs.cardText.clientHeight < this.$refs.cardText.scrollHeight
      }
    },
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    },
    createCard () {
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
        this.editedCard.newScope = this.scope
        var cardDto = JSON.parse(JSON.stringify(this.editedCard))
        if (this.isNew) {
          if (!this.addExisting) {
            /* create new card */
            this.sendingData = true
            this.axios.post('/1/model/section/' + this.inSection.id + '/cardWrapper', cardDto, { params: {
              onCardWrapperId: this.atCardWrapper ? this.atCardWrapper.id : null,
              isBefore: this.newCardLocation
            }}).then((response) => {
              this.sendingData = false
              if (response.data.result === 'success') {
                this.$emit('updateCards')
              } else {
                console.log(response.data.message)
              }
            }).catch((error) => {
              console.log(error)
            })
          }
        } else {
          /* editing */
          console.log('editing', this.inSection.id, this.type, this.atCardWrapper.id, this.scope, cardDto)
          this.sendingData = true
          this.axios.put('/1/model/cardWrapper/' + this.atCardWrapper.id, cardDto, {
            params: {
              inSectionId: this.inSection.id
            }
          })
            .then((response) => {
              console.log(response)
              this.sendingData = false
              if (response.data.result === 'success') {
                this.$emit('updateCards')
              }
            }).catch((error) => {
              console.log(error)
            })
        }
      }
    },
    atKeydown (e) {
      if (this.editing) {
        /* esc */
        if (e.keyCode === 27) {
          this.$emit('edit')
        }

        /* ctr + enter */
        if (e.keyCode === 13 && e.ctrlKey) {
          e.preventDefault()
          this.createCard()
        }
      }
    }
  },

  mounted () {
    if (this.isNew) {
      this.editedCard = {
        title: '',
        text: '',
        newScope: 'SHARED'
      }
      this.editing = true
    } else {
      this.startEditing()
    }
    window.addEventListener('keydown', this.atKeydown)
  },

  destroyed () {
    window.removeEventListener('keydown', this.atKeydown)
  }
}
</script>

<style scoped>

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

.input-border {
  border: solid #ccc !important;
  border-width: 1px 0px 0px 0px !important;
}

.card-summary-container {
  overflow: hidden;
  position: relative;
  width: 100%;
}

.controls {
  height: 50%;
  position: absolute;
  right: 0;
}

.right-div-top {
  top: 0;
}

.right-div-bottom {
  bottom: 0;
}

.highlight:hover {
   text-shadow: 0px 0px 5px #000000;
}

.select-clicked {
   text-shadow: 0px 0px 5px #000000;
}

.app-button {
  border-radius: 0px;
}

.app-button-light {
  border-radius: 0px;
}

.send-button-container button {
  width: 100%;
}

.send-button-container-summary {
  position: absolute;
  bottom:   0;
}

.send-button-container-doc {
  display: initial;
}
.scope-controls, .control-buttons {
  padding: 5px 0px 5px 0px;
}

.select-private {
  color: #980458;
}
.select-shared {
  color: #c6bb21;
}
.select-common {
  color: #0c708b;
}

.card-container {
  max-height: 750px;
  transition: max-height 1000ms ease;
  overflow-y: auto;
}

.limit-height {
  max-height: 250px;
  overflow: hidden;
}

.card-container-slim {
  padding: 0px 0px 0px 0px !important;
}

.top-row {
  padding: 6px 3px 6px 3px;
}

.details-row {
  margin-top: 3px;
}

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  max-height: 150px;
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

.image-container-doc {
  height: 350px;
  margin-bottom: 16px;
}

.image-container-doc img {
  height: 100%;
}

.card-text {
  overflow-y: hidden;
}

.card-text-ascard-no-image {
  max-height: 250px;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

.bottom-row {
  padding: 6px 12px;
  font-size: 18px;
}

.insection-tag-container {
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 5px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

.control-div {
  padding: 3px 6px;
}

.expand-height-button {
  position: absolute;
  left: calc(100% - 38px);
  top: -38px;
  background-color: rgba(218, 218, 218, 0.5);
}

.card-editor-doc {
  border-left-style: solid;
  padding-left: 6px;
}

</style>
