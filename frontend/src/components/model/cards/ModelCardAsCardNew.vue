<template lang="html">
  <div class="w3-display-container w3-card-4 w3-topbar w3-round-large" :class="cClass"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-row card-container cursor-pointer" :class="{'limit-height': !showFull}"
      ref="cardContent">

<!--       <div class="w3-row" @click="cardClicked();"> -->
      <div class="w3-row">

        <div class="card-container-padded">
          <div class="w3-row card-title">
            <input type="text" class="w3-input w3-hover-light-grey" placeholder="Enter card title"  v-model="editedCard.title">
            <app-error-panel
            :show="titleTooLongShow"
            message="name too long">
            </app-error-panel>
          </div>

        <div ref="cardText" class="w3-row card-text">

          <div class="w3">
            <app-markdown-editor placeholder="Enter text here" v-model="editedCard.text"></app-markdown-editor>
          </app-error-panel>
          </div>

      </div>

        </div>
    </div>

    </div>

    <div class="w3-row bottom-row light-grey">

      <div v-if="!inCardSelector" class="w3-left scope-controls">

        <div  class="w3-right cursor-pointer indicator-comp"
          @click="scopeSelected('common')">
          <i class="fa fa-circle select-common highlight" aria-hidden="true"></i>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="scopeSelected('shared')">
          <i class="fa fa-circle select-shared highlight" aria-hidden="true"></i>
        </div>

        <div class="w3-right cursor-pointer indicator-comp"
          @click="scopeSelected('private')">
          <i class="fa fa-circle select-private highlight" aria-hidden="true"></i>
        </div>
      </div>

      <div v-if="!inCardSelector" class="w3-right control-buttons">
        <div class="w3-right cursor-pointer indicator-comp">
          <i class="fa fa-upload highlight" aria-hidden="true"></i>
        </div>
        <div class="w3-right cursor-pointer indicator-comp" @click="cardClicked()">
          <i class="fa fa-expand highlight" aria-hidden="true"></i>
        </div>
      </div>

    </div>
    <div class="w3-row button-row light-grey">
      <div class="send-button-container">
        <button v-show="!sendingData" class="w3-button app-button" name="button" @click="createCard()">Create new card</button>
        <div v-show="sendingData" class="sending-accept light-grey">
          <img class="" src="../../../assets/loading.gif" alt="">
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { cardMixin } from '@/components/model/cards/cardMixin.js'
import CardControlButtons from '@/components/model/cards/CardControlButtons.vue'
import InModelSectionsTags from '@/components/model/cards/InModelSectionsTags.vue'

export default {

  name: 'model-card-as-card',

  mixins: [ cardMixin ],

  components: {
    'app-card-control-buttons': CardControlButtons,
    'app-in-model-sections-tags': InModelSectionsTags
  },

  props: {
    isNew: {
      type: Boolean,
      default: true
    },
    inSectionIdNew: {
      type: String,
      default: ''
    },
    cardWrapperId: {
      type: String,
      default: ''
    },
    inSectionTitleNew: {
      type: String,
      default: ''
    },
    newCardLocation: {
      type: String,
      default: ''
    },
    atCardWrapper: {
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
      cClass: {
        'border-yellow': true
      },
      hovering: false,
      showFull: false,
      addExisting: false,
      sendingData: false,
      loading: false
    }
  },

  computed: {
    hasImage () {
      return this.card.imageFile !== null
    },
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
    cardComponent () {
      console.log('thisbanda', this.type)
      switch (this.type) {
        case 'summary':
          return 'app-model-card-summary'

        case 'card':
          if (this.cardWrapper.type === 'newCard') {
            return 'app-model-card-as-card-new'
          } else {
            return 'app-model-card-as-card'
          }

        case 'doc':
          return 'app-model-card-as-par'

        case 'new':
          return 'app-model-card-as-card-new'

        default:
          return 'app-model-card-as-card'
      }
    }
  },

  methods: {
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

      this.axios.post('/1/upload/cardImage/' + this.cardWrapper.id, data).then((response) => {
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
    scopeSelected (scope) {
      console.log(scope)
      this.cClass = {}
      if (scope === 'private') {
        this.editedCard.newScope = 'PRIVATE'
        this.cClass['border-red'] = true
      } else if (scope === 'shared') {
        this.editedCard.newScope = 'SHARED'
        this.cClass['border-yellow'] = true
      } else if (scope === 'common') {
        this.editedCard.newScope = 'COMMON'
        this.cClass['border-blue'] = true
      }
      console.log(this.cClass)
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
    showMoreClick () {
      this.showFull = !this.showFull
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
        var cardDto = JSON.parse(JSON.stringify(this.editedCard))
        console.log('cardDto', JSON.stringify(cardDto))
        if (this.isNew) {
          if (!this.addExisting) {
            /* create new card */
            this.sendingData = true
            this.axios.post('/1/model/section/' + this.inSectionIdNew + '/cardWrapper', cardDto, { params: {
              onCardWrapperId: null,
              isBefore: this.newCardLocation === 'before'
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
    }
  }
}
</script>

<style scoped>

.card-title {
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

.send-button-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
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

/*.card-container-padded {
  padding: 2px 2px 0px 2px !important;
}*/

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

.bottom-row .button-row{
  position: relative;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  padding: 3px 6px;
  min-height: 30px;
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

</style>
