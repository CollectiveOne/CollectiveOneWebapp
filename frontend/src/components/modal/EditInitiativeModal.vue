<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="close-div w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times" aria-hidden="true"></i>
        </div>

        <div class="w3-container  w3-border-bottom">
          <h2>Edit Initiative</h2>
        </div>

        <div class="w3-container form-container">

          <div class="w3-row image-container w3-center w3-display-container w3-card-2">
            <div v-if="uploadingImage" class="loader-gif-container">
              <img class="loader-gif" src="../../assets/loading.gif" alt="">
            </div>
            <div v-if="!uploadingImage && (newInitiative.meta.imageFile)" class="">
              <img :src="newInitiative.meta.imageFile.url + '?lastUpdated='+ newInitiative.meta.imageFile.lastUpdated" alt="">
            </div>
            <div v-if="!uploadingImage" class="w3-display-middle">
              <input class="inputfile" @change="newFileSelected($event)"
                type="file" name="imageFile" id="imageFile" accept="image/*">
              <label for="imageFile" class="w3-button app-button">{{ newInitiative.meta.imageFile ? 'change' : 'upload image' }}</label>
              <button v-if="newInitiative.meta.imageFile"
                @click="removeImage()"
                class="w3-button app-button-danger">remove</button>
            </div>
          </div>
          <app-error-panel
            :show="errorUploadingFile"
            :message="errorUploadingFileMsg">
          </app-error-panel>

          <div class="w3-row w3-margin-top">
            <label class=""><b>Name</b></label>
            <input v-model="newInitiative.meta.name"
              class="w3-input w3-hover-light-grey"
              :class="{ 'error-input' : nameErrorShow }"
              type="text">
            <app-error-panel
              :show="nameEmptyShow"
              message="please set the new name of the initiative">
            </app-error-panel>
            <app-error-panel
              :show="nameTooLarge"
              message="name too long">
            </app-error-panel>
          </div>
          <br>

          <div class="w3-row">
            <label class=""><b>Purpose</b></label>
            <app-markdown-editor v-model="newInitiative.meta.driver"></app-markdown-editor>
            <app-error-panel
              :show="driverEmptyShow"
              message="please set a purpose for the initiative">
            </app-error-panel>
          </div>
          <br>

          <div class="w3-row">
            <label class=""><b>Color</b></label>
            <div class="colors-container w3-center">
              <div
                v-for="color in colors"
                class="color-picker w3-circle cursor-pointer"
                :class="{'color-picked': newInitiative.meta.color === color}"
                :style="{'background-color': color}"
                @click="newInitiative.meta.color = color">
              </div>
            </div>
          </div>

          <hr>
          <div class="w3-row">
            <label class=""><b>Modules</b></label>
            <div class="w3-col s12">
              <div class="w3-row w3-margin-top modules-row">
                  <div class="w3-col m4 module-tag-col">
                    MODEL:
                  </div>
                  <div class="w3-col m8">
                    <button class="w3-left w3-button"
                      :class="{ 'app-button': !newInitiative.meta.modelEnabled, 'app-button-light': newInitiative.meta.modelEnabled }"
                      @click="newInitiative.meta.modelEnabled = false">
                      disable
                    </button>
                    <button class="w3-left w3-button app-button"
                      :class="{ 'app-button': newInitiative.meta.modelEnabled, 'app-button-light': !newInitiative.meta.modelEnabled }"
                      @click="newInitiative.meta.modelEnabled = true">
                      enable
                    </button>
                  </div>
              </div>
            </div>
          </div>

          <hr>
          <div class="w3-row">
            <div class="w3-col s12">
              <div class="w3-row">
                <label class=""><b>Tags</b></label>
              </div>
              <div class="w3-row-padding w3-margin-top tags-row">
                  <div class="w3-col l6 module-tag-col w3-margin-bottom">
                    <app-initiative-tag-selector-manager
                      :initiativeId="initiative.id"
                      @add="addTag($event)">
                    </app-initiative-tag-selector-manager>
                  </div>
                  <div class="w3-col l6">
                    <app-initiative-tag
                      class="tags-containers"
                      v-for="tag in newInitiative.meta.tags"
                      :tag="tag"
                      :key="tag.id"
                      @remove="removeTag($event)"
                      :showRemove="true">
                    </app-initiative-tag>
                  </div>
              </div>
            </div>
          </div>

          <hr>
          <div class="w3-row">
            <label class=""><b>Visibility</b></label>
            <div class="w3-col s12">
              <div class="w3-row w3-margin-top modules-row">
                  <div class="w3-col s4">
                    <button class="w3-left w3-button"
                      :class="{ 'app-button': isPrivate, 'app-button-light': !isPrivate }"
                      @click="newInitiative.meta.visibility = 'PRIVATE'">
                      private
                    </button>
                  </div>
                  <div class="w3-col s4">
                    <button class="w3-left w3-button"
                      :class="{ 'app-button': isEcosystem, 'app-button-light': !isEcosystem }"
                      @click="newInitiative.meta.visibility = 'ECOSYSTEM'">
                      ecosystem
                    </button>
                  </div>
                  <div v-if="isSubinitiative" class="w3-col s4">
                    <button class="w3-left w3-button"
                      :class="{ 'app-button': isInherited, 'app-button-light': !isInherited }"
                      @click="newInitiative.meta.visibility = 'INHERITED'">
                      inherited
                    </button>
                  </div>
                  <div class="w3-col s4">
                    <button class="w3-left w3-button"
                      :class="{ 'app-button': isPublic, 'app-button-light': !isPublic }"
                      @click="newInitiative.meta.visibility = 'PUBLIC'">
                      public
                    </button>
                  </div>
              </div>
            </div>
          </div>

          <hr>
          <div class="bottom-btns-row w3-row-padding">
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
            </div>
          </div>

          <hr>
          <div class="w3-row w3-center w3-margin-bottom delete-row">
            <button type="button" class="w3-left w3-button app-button-danger" @click="deleteInitiativeSelected = true">Delete Initiative</button>
          </div>
          <div class="slider-container">
            <transition name="slideDownUp">
              <div v-if="deleteInitiativeSelected" class="w3-row tags-row w3-center">
                <div class="w3-padding w3-round light-grey w3-margin-bottom">
                  <p>
                    <b>Warning:</b> This will delete this initiative and all its
                    subinitiatives and, if there is a parent initiative, transfer all
                    its assets back to it. Please confirm.
                  </p>
                </div>

                <button
                  class="w3-button app-button-light button-pair"
                  @click="deleteInitiativeSelected = false">cancel
                </button>
                <button
                  class="w3-button app-button-danger button-pair"
                  @click="deleteInitiative()">confirm
                </button>
              </div>
            </transition>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
import InitiativeTag from '@/components/initiative/InitiativeTag.vue'
import InitiativeTagSelectorManager from '@/components/initiative/InitiativeTagSelectorManager.vue'

const getIndexOfTag = function (list, id) {
  for (var ix in list) {
    if (list[ix].id === id) {
      return ix
    }
  }
  return -1
}

export default {

  components: {
    'app-initiative-tag': InitiativeTag,
    'app-initiative-tag-selector-manager': InitiativeTagSelectorManager
  },

  data () {
    return {
      newInitiative: null,
      colors: [
        '#009ee3',
        '#e5007d',
        '#e8d80c',
        '#68b628',
        '#ff9100',
        '#e40000',
        '#00a8a8'
      ],
      nameEmptyError: false,
      driverEmptyError: false,
      deleteInitiativeSelected: false,
      newTag: '',
      uploadingImage: false,
      errorUploadingFile: false,
      errorUploadingFileMsg: ''
    }
  },

  computed: {
    initiative () {
      return this.$store.state.initiative.initiative
    },
    isSubinitiative () {
      return this.newInitiative.parents.length !== 0
    },
    nameErrorShow () {
      return this.nameEmptyShow || this.nameTooLarge
    },
    nameEmptyShow () {
      return this.nameEmptyError && this.newInitiative.meta.name === ''
    },
    nameTooLarge () {
      return this.newInitiative.meta.name.length > 30
    },
    driverEmptyShow () {
      return this.driverEmptyError && this.newInitiative.meta.driver === ''
    },
    isPrivate () {
      if (!this.isSubinitiative) {
        return this.newInitiative.meta.visibility === 'PRIVATE' || this.newInitiative.meta.visibility === 'INHERITED' || this.newInitiative.meta.visibility === null
      } else {
        return this.newInitiative.meta.visibility === 'PRIVATE'
      }
    },
    isInherited () {
      if (!this.isSubinitiative) {
        return false
      } else {
        return this.newInitiative.meta.visibility === 'INHERITED' || this.newInitiative.meta.visibility === null
      }
    },
    isPublic () {
      return this.newInitiative.meta.visibility === 'PUBLIC'
    },
    isEcosystem () {
      return this.newInitiative.meta.visibility === 'ECOSYSTEM'
    }
  },

  methods: {
    newFileSelected (event) {
      /* upload image */
      var data = new FormData()
      data.append('file', event.target.files[0])

      this.uploadingImage = true
      this.errorUploadingFile = false

      this.axios.post('/1/upload/initiativeImage/' + this.initiative.id, data).then((response) => {
        this.uploadingImage = false

        if (response.data.result === 'success') {
          this.newInitiative.meta.newImageFileId = response.data.elementId
          return this.axios.get('/1/files/' + this.newInitiative.meta.newImageFileId)
        } else {
          this.errorUploadingFile = true
          this.errorUploadingFileMsg = response.data.message
        }
      }).then((response) => {
        this.newInitiative.meta.imageFile = response.data.data
      })
    },
    addTag (tag) {
      if (getIndexOfTag(this.newInitiative.meta.tags, tag.id) === -1) {
        this.newInitiative.meta.tags.push(tag)
      }
    },
    removeTag (tag) {
      var ix = getIndexOfTag(this.newInitiative.meta.tags, tag.id)
      if (ix !== -1) {
        this.newInitiative.meta.tags.splice(ix, 1)
      }
    },
    closeThis () {
      this.$store.commit('showEditInitiativeModal', false)
    },
    accept () {
      var ok = true

      if (this.newInitiative.meta.name === '') {
        this.nameEmptyError = true
        ok = false
      }

      if (this.nameTooLarge) {
        ok = false
      }

      if (this.newInitiative.meta.driver === '') {
        this.driverEmptyError = true
        ok = false
      }

      if (ok) {
        this.axios.put('/1/initiative/' + this.initiative.id, this.newInitiative.meta).then((response) => {
          this.closeThis()
          this.$store.dispatch('refreshInitiative')
          this.$store.dispatch('updateMyInitiatives')
        })
      }
    },
    deleteInitiative () {
      this.axios.delete('/1/initiative/' + this.initiative.id).then((response) => {
        this.$store.dispatch('refreshInitiative')
        this.$store.dispatch('updateMyInitiatives')
        window.location.href = '/'
      })
    }
  },

  created () {
    this.newInitiative = JSON.parse(JSON.stringify(this.initiative))
  }
}
</script>

<style scoped>

.form-container {
  padding-top: 20px;
  padding-bottom: 35px;
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

.image-container {
  min-height: 80px;
  max-height: 250px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.colors-container {
  margin-top: 15px;
}

.color-picker {
  width: 55px;
  height: 55px;
  display: inline-block;
  margin-left: 15px;
}

.color-picker:hover {
  border-style: solid;
  border-width: 5px;
  border-color: black;
}

.color-picked {
  border-style: solid;
  border-width: 5px;
  border-color: black;
}

.modules-row {
  max-width: 400px;
  margin: 0 auto;
}

.modules-row button {
  padding-top: 3px !important;
  padding-bottom: 3px !important;
  width: 100px;
  margin-right: 10px;
}

.module-tag-col {
  padding-top: 3px;
}

.new-tag-input-container {
  padding-left: 0px;
  padding-right: 10px;
}

.delete-row {
  margin-top: 50px;
}

</style>
