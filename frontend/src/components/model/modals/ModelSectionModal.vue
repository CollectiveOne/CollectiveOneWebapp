<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ isNew ? 'New Section' : 'Model Section' }}</h2>
        </div>

        <div class="w3-container div-modal-content">

          <app-model-modal-buttons
            v-if="isLoggedAnEditor"
            :show="showEditButtons"
            @edit="startEditing()"
            @delete="deleteSection()"
            deleteMessage="This will delete the section from all the views in which it is used."
            @remove="removeSection()"
            :removeMessage="'This will remove this subsection from ' + inElementTitle + '.'">
          </app-model-modal-buttons>

          <div v-if="isNew" class="w3-row">
            <label v-if="inView" class=""><b>In View:</b></label>
            <label v-else class=""><b>In Section:</b></label>
            <br>
            <h4>{{ this.inElementTitleOk }}</h4>
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
              <div v-if="addExisting" class="">
                <app-model-section-selector
                  :initiativeId="initiativeId"
                  @select="sectionSelected($event)">
                </app-model-section-selector>
                <app-error-panel
                  :show="noSectionSelectedShow"
                  message="please select one section from above">
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
                <div class="w3-row w3-margin-top">
                  <div v-if="!editing" class="w3-padding light-grey">
                    <h4><b>{{ section.title }}</b></h4>
                  </div>
                  <div v-else class="">
                    <label class=""><b>Title: <span v-if="editing" class="w3-small error-text">(required)</span></b></label>
                    <input type="text" class="w3-input w3-hover-light-grey" v-model="editedSection.title">
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

                <div class="w3-row w3-margin-top">
                  <div v-if="!editing" class="w3-padding light-grey">
                    <vue-markdown class="marked-text" :source="section.description"></vue-markdown>
                  </div>
                  <div v-else class="">
                    <label class=""><b>Description: <span v-if="editing" class="w3-small error-text">(required)</span></b></label>
                    <app-markdown-editor v-model="editedSection.description"></app-markdown-editor>
                    <app-error-panel
                      :show="descriptionErrorShow"
                      message="please include a description of this section">
                    </app-error-panel>
                  </div>
                </div>
              </div>
            </transition>
          </div>

          <div v-if="editing || addExisting" class="modal-bottom-btns-row w3-row-padding">
            <hr>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="cancel()">Cancel</button>
            </div>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
            </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ModelModalButtons from '@/components/model/modals/ModelModalButtons.vue'
import ModelSectionSelector from '@/components/model/ModelSectionSelector.vue'

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons,
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
    sectionId: {
      type: String,
      default: ''
    },
    inView: {
      type: Boolean,
      default: true
    },
    inElementId: {
      type: String,
      default: ''
    },
    inElementTitle: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      section: {
        if: '',
        title: '',
        description: ''
      },
      inElementTitleOk: '',
      editedSection: null,
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      descriptionEmptyError: false,
      addExisting: false,
      existingSection: null,
      noSectionSelectedError: false,
      animatingTab: false
    }
  },

  computed: {
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    titleEmpty () {
      return this.editedSection.title === ''
    },
    titleTooLong () {
      return this.editedSection.title.length > 30
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
      return this.editedSection.description === ''
    },
    descriptionErrorShow () {
      return this.descriptionEmptyError && this.descriptionEmpty
    },
    noSectionSelected () {
      return this.existingSection === null
    },
    noSectionSelectedShow () {
      return this.noSectionSelectedError && this.noSectionSelected
    }
  },

  methods: {
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id)
      .then((response) => {
        this.section = response.data.data
      })
    },
    updateInElement () {
      if (this.inView) {
        this.axios.get('/1/initiative/' + this.initiativeId + '/model/view/' + this.inElementId)
        .then((response) => {
          this.inElementTitleOk = response.data.data.title
        })
      } else {
        this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.inElementId)
        .then((response) => {
          this.inElementTitleOk = response.data.data.title
        })
      }
    },
    startEditing () {
      this.editedSection = JSON.parse(JSON.stringify(this.section))
      this.editing = true
    },
    sectionSelected (section) {
      this.existingSection = section
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

      if (!this.addExisting) {
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
      } else {
        if (this.noSectionSelected) {
          ok = false
          this.noSectionSelectedError = true
        }
      }

      if (ok) {
        var sectionDto = JSON.parse(JSON.stringify(this.editedSection))
        var responseF = (response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.commit('triggerUpdateModel')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }

        if (this.isNew) {
          var place = ''
          if (this.inView) {
            place = 'view'
          } else {
            place = 'section'
          }

          if (!this.addExisting) {
            /* create new section */
            this.axios.post('/1/initiative/' + this.initiativeId + '/model/' + place + '/' + this.inElementId + '/subsection', sectionDto)
            .then(responseF).catch((error) => {
              console.log(error)
            })
          } else {
            /* add existing section */
            this.axios.put('/1/initiative/' + this.initiativeId + '/model/' + place + '/' + this.inElementId + '/subsection/' + this.existingSection.id, {})
            .then(responseF).catch((error) => {
              console.log(error)
            })
          }
        } else {
          /* edit existing section */
          this.axios.put('/1/initiative/' + this.initiativeId + '/model/section/' + sectionDto.id, sectionDto)
          .then(responseF).catch((error) => {
            console.log(error)
          })
        }
      }
    },
    removeSection () {
      var responseF = (response) => {
        if (response.data.result === 'success') {
          this.closeThis()
          this.$store.commit('triggerUpdateModel')
        } else {
          this.showOutputMessage(response.data.message)
        }
      }

      if (this.inView) {
        this.axios.put('/1/initiative/' + this.initiativeId + '/model/view/' + this.inElementId + '/removeSection/' + this.section.id,
          {}).then(responseF).catch((error) => {
            console.log(error)
          })
      } else {
        this.axios.put('/1/initiative/' + this.initiativeId + '/model/section/' + this.inElementId + '/removeSubsection/' + this.section.id,
          {}).then(responseF).catch((error) => {
            console.log(error)
          })
      }
    },
    deleteSection () {
      this.axios.delete('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id)
      .then((response) => {
        this.closeThis()
        this.$store.commit('triggerUpdateModel')
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.inElementTitleOk = this.inElementTitle

    if (this.isNew) {
      this.editedSection = {
        title: '',
        description: ''
      }
      this.editing = true
      this.updateInElement()
    } else {
      this.showEditButtons = true
      this.section.id = this.sectionId
      this.update()
    }
  }
}
</script>

<style scoped>

</style>
