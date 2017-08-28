<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Model Section</h2>
        </div>

        <div v-if="section" class="w3-container div-modal-content">

          <app-model-modal-buttons
            :show="true"
            @edit="startEditing()"
            @delete="deleteSection()">
          </app-model-modal-buttons>

          <div v-if="section.isSubsection" class="">
            <label class=""><b>Under:</b></label>
            {{ section.parentSectionTitle }}
          </div>

          <br>
          <label class=""><b>Title:</b></label>
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

          <br>
          <label class=""><b>Description:</b></label>
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
</template>

<script>
import ModelModalButtons from '@/components/model/modals/ModelModalButtons.vue'

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    sectionId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      section: null,
      editedSection: null,
      editing: false,
      titleEmptyError: false,
      descriptionEmptyError: false,
      test: 'tests'
    }
  },

  computed: {
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
      this.axios.get('/1/secured/initiative/' + this.initiativeId + '/model/section/' + this.sectionId).then((response) => {
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
        var sectionDto = {
          id: this.editedSection.id,
          viewId: this.editedSection.viewId,
          title: this.editedSection.title,
          description: this.editedSection.description,
          isSubsection: false
        }

        if (this.isSubsection) {
          sectionDto.isSubsection = true
          sectionDto.parentSectionId = this.editedSection.parentSection.id
        }

        this.axios.put('/1/secured/initiative/' + this.initiativeId + '/model/section', sectionDto).then((response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.dispatch('refreshModel')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }).catch((error) => {
          console.log(error)
        })
      }
    },
    deleteSection () {
      this.axios.delete('/1/secured/initiative/' + this.initiativeId + '/model/section/' + this.section.id).then((response) => {
        this.$store.dispatch('refreshModel')
        this.closeThis()
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.updateSectionData()
  }
}
</script>

<style scoped>

</style>
