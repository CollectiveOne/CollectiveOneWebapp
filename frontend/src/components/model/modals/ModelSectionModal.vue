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
            deleteMessage="This will delete the section from all the views in which it is used. Its cards will not be deleted."
            @edit="startEditing()"
            @delete="deleteSection()">
          </app-model-modal-buttons>

          <div v-if="isNew" class="w3-row">
            <div v-if="!editedSection.isSubsection" class="">
              <label class=""><b>View:</b></label>
              <br>
              <h4>{{ this.editedSection.viewTitle }}</h4>
            </div>
            <div v-else class="">
              <label class=""><b>Parent Section:</b></label>
              <br>
              <h4>{{ this.editedSection.parentSectionTitle }}</h4>
            </div>
          </div>

          <div class="w3-row w3-margin-top">
            <label class=""><b>Title: <span v-if="editing" class="w3-small error-text">(required)</span></b></label>
            <div v-if="!editing" class="w3-padding light-grey">
              {{ section.title }}
            </div>
            <div v-else class="">
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
            <label class=""><b>Description: <span v-if="editing" class="w3-small error-text">(required)</span></b></label>
            <div v-if="!editing" class="w3-padding light-grey">
              {{ section.description }}
            </div>
            <div v-else class="">
              <textarea type="text" class="w3-input w3-border w3-round w3-hover-light-grey" v-model="editedSection.description"></textarea>
              <app-error-panel
                :show="descriptionErrorShow"
                message="please include a description of this section">
              </app-error-panel>
            </div>
          </div>

          <div v-if="editing" class="modal-bottom-btns-row w3-row-padding">
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

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons
  },

  props: {
    pars: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      section: {
        title: '',
        description: ''
      },
      initiativeId: '',
      isNew: false,
      editedSection: null,
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      descriptionEmptyError: false,
      test: 'tests'
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
    }
  },

  methods: {
    updateSectionData () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id).then((response) => {
        this.section = response.data.data
      })
    },
    startEditing () {
      this.editedSection = JSON.parse(JSON.stringify(this.section))
      this.editing = true
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
        var sectionDto = JSON.parse(JSON.stringify(this.editedSection))
        var baseurl = '/1/initiative/' + this.initiativeId + '/model/section'
        var returnF = (response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.commit('triggerUpdateModel')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }

        if (this.isNew) {
          this.axios.post(baseurl, sectionDto).then(returnF).catch((error) => {
            console.log(error)
          })
        } else {
          this.axios.put(baseurl + '/' + sectionDto.id, sectionDto).then(returnF).catch((error) => {
            console.log(error)
          })
        }
      }
    },
    deleteSection () {
      this.axios.delete('/1/initiative/' + this.initiativeId + '/model/section/' + this.section.id).then((response) => {
        this.closeThis()
        this.$store.commit('triggerUpdateModel')
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.initiativeId = this.pars.initiativeId

    if (this.pars.new) {
      this.isNew = true

      this.editedSection = {
        isSubsection: this.pars.isSubsection,
        title: '',
        description: ''
      }

      if (!this.pars.isSubsection) {
        this.editedSection.viewId = this.pars.viewId
        this.editedSection.viewTitle = this.pars.viewTitle
      } else {
        this.editedSection.parentSectionId = this.pars.parentSectionId
        this.editedSection.parentSectionTitle = this.pars.parentSectionTitle
      }

      this.editing = true
    } else {
      this.showEditButtons = true
      this.section.id = this.pars.sectionId
      this.updateSectionData()
    }
  }
}
</script>

<style scoped>

</style>
