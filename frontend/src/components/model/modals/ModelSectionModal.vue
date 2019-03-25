<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4 app-modal-card w3-display-container"
        v-click-outside="clickOutside">

        <div class="w3-display-topright">
          <div class="w3-right w3-button w3-xlarge" @click="closeThisConfirm()">
            <i class="fa fa-times" aria-hidden="true"></i>
          </div>
        </div>

        <div class="w3-container w3-border-bottom">
          <h3>{{ isNew ? $t('model.NEW_SECTION') : $t('model.MODEL_SECTION') }}</h3>
        </div>

        <div v-if="!loading" class="w3-container div-modal-content">

          <div v-if="isNew" class="w3-row">
            <label v-if="inInitiative" class="">
              <b>{{ $t('model.AS_TOP_LEVEL_IN') }}:</b>
            </label>
            <label v-else class=""><b>{{ $t('model.IN_SECTION') }}:</b></label>
            <br>
            <h4>{{ this.inSectionTitleOk }}
              <span v-if="onSection != null">
                ({{ isBefore ? $t('model.BEFORE_SECTION') : $t('model.AFTER_SECTION') }} {{ onSection.title }})
              </span>
            </h4>
          </div>

          <div v-if="isNew" class="section-tabs w3-row w3-center light-grey">
            <div class="w3-col s6 w3-bottombar w3-hover-light-grey cursor-pointer"
              :class="{'border-blue-app': !addExisting}"
              @click="addExisting = false">
              <h5 class="noselect" :class="{'bold-text': !addExisting}">
                {{ $t('model.CREATE_NEW') }}
              </h5>
            </div>
            <div class="w3-col s6 w3-bottombar w3-hover-light-grey cursor-pointer"
              :class="{'border-blue-app': addExisting}"
              @click="addExisting = true">
              <h5 class="noselect" :class="{'bold-text': addExisting}">
                {{ $t('model.ADD_EXISTING') }}
              </h5>
            </div>
          </div>

          <div :class="{'slider-container': animatingTab}">
            <transition name="slideRightLeft"
              mode="out-in"
              @before-enter="animatingTab = true"
              @after-enter="animatingTab = false"
              @before-leave="animatingTab = true"
              @after-leave="animatingTab = false">

              <div v-if="addExisting" class="">
                <app-model-section-selector
                  :sectionId="inSection ? inSection.id : ''"
                  @select="sectionSelected($event)">
                </app-model-section-selector>
                <app-error-panel
                  :show="noSectionSelectedShow"
                  message="please select one section from above">
                </app-error-panel>

                <div class="w3-row w3-container w3-margin-top">
                  <label class="">
                    <b>{{ $t('model.NEW_SCOPE_IN', { title: inSectionTitleOk }) }}:</b>
                  </label>
                  <select v-model="existingSectionNewScope"
                    class="w3-input w3-topbar" :class="selectorExistingBorderClass" name="">
                    <option value="PRIVATE">{{ $t('model.PRIVATE_MSG') }}</option>
                    <option value="SHARED">{{ $t('model.SHARED_MSG') }}</option>
                    <option value="COMMON">{{ $t('model.COMMON_MSG') }}</option>
                  </select>
                </div>

                <div class="w3-row w3-margin-top">
                  <b>{{ $t('model.LINK_STATUS_WITH_SECTION', { title: '"' + existingSectionTitle + '"' }) }}</b>
                </div>
                <div class="w3-row-padding modal-bottom-btns-row">
                  <div class="w3-col m6">
                    <button type="button" class="w3-button"
                      :class="{'app-button': !detachFlag, 'app-button-light': detachFlag}"
                      @click="detachFlag = false">
                      {{ $t('model.KEEP') }}
                    </button>
                  </div>
                  <div class="w3-col m6">
                    <button type="button" class="w3-button app-button"
                      :class="{'app-button': detachFlag, 'app-button-light': !detachFlag}"
                      @click="detachFlag = true">
                      {{ $t('model.DETACH') }}
                    </button>
                  </div>
                </div>

              </div>
              <div v-else>

                <div v-if="editing && this.inSection != null" class="w3-row w3-margin-top">
                  <label class="">
                    <b>{{ $t('model.SCOPE_IN', { title: inSectionTitleOk }) }}:</b>
                  </label>
                  <select v-model="editedSection.newScope"
                    class="w3-input w3-topbar" :class="selectorBorderClass" name="">
                    <option value="PRIVATE">{{ $t('model.PRIVATE_MSG') }}</option>
                    <option value="SHARED">{{ $t('model.SHARED_MSG') }}</option>
                    <option value="COMMON">{{ $t('model.COMMON_MSG') }}</option>
                  </select>
                </div>

                <div class="w3-row">
                  <div v-if="!editing" class="">
                    <h3><b>{{ section.title }}</b></h3>
                  </div>
                  <div v-else class="w3-margin-top">
                    <label class=""><b>{{ $t('model.TITLE') }}: <span v-if="editing" class="w3-small error-text">({{ $t('general.REQUIRED') }})</span></b></label>
                    <input ref="titleInput" type="text" class="w3-input w3-hover-light-grey" v-model="editedSection.title">
                    <app-error-panel
                      :show="titleEmptyShow"
                      :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
                    </app-error-panel>
                    <app-error-panel
                      :show="titleTooLongShow"
                      :message="$t('general.FIELD_TOO_LONG')">
                    </app-error-panel>
                  </div>
                </div>

                <div class="w3-row w3-margin-top">
                  <div v-if="!editing" class="">
                    <vue-markdown class="marked-text" :source="section.description" :anchorAttributes="{target: '_blank'}"></vue-markdown>
                  </div>
                  <div v-else class="">
                    <label class=""><b>{{ $t('general.DESCRIPTION') }}:</b></label>
                    <app-markdown-editor v-model="editedSection.description" :keepBackup="false"></app-markdown-editor>
                    <app-error-panel
                      :show="descriptionErrorShow"
                      :message="$t('general.FIELD_CANNOT_BE_EMPTY')">
                    </app-error-panel>
                  </div>
                </div>
              </div>
            </transition>
          </div>

          <div v-if="editing || addExisting" class="modal-bottom-btns-row w3-row-padding">
            <hr>
            <div class="w3-col m6">
              <button type="button" class="w3-button app-button-light" @click="closeThis()">
                {{ $t('general.CANCEL') }}<span><small>(Esc)</small></span>
              </button>
            </div>
            <div class="w3-col m6 w3-center">
              <button v-show="!sendingData" type="button" class="w3-button app-button" @click="accept()">
                {{ $t('general.ACCEPT') }}<span><small>(Ctr + Enter)</small></span>
              </button>
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
          {{ $t('model.CLOSE_WARNING') }}
          <div class="w3-row w3-margin-top">
            <button class="w3-button app-button-light" name="button"
              @click="closeIntent = false">
              {{ $t('general.CANCEL') }}
            </button>
            <button class="w3-button app-button" name="button"
              @click="closeThis()">
              {{ $t('general.CONFIRM') }}
            </button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ModelSectionSelector from '@/components/model/ModelSectionSelector.vue'

export default {

  components: {
    'app-model-section-selector': ModelSectionSelector
  },

  props: {
    isNew: {
      type: Boolean,
      default: false
    },
    sectionId: {
      type: String,
      default: ''
    },
    inInitiative: {
      type: Boolean,
      default: false
    },
    inSection: {
      type: Object,
      default: null
    },
    onlyMessages: {
      type: Boolean,
      defaul: false
    },
    onSection: {
      type: Object,
      default: null
    },
    isBefore: {
      type: Boolean,
      defaul: false
    }
  },

  data () {
    return {
      section: {
        if: '',
        title: '',
        description: '',
        newScope: 'SHARED'
      },
      inSectionTitleOk: '',
      editedSection: null,
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      descriptionEmptyError: false,
      addExisting: false,
      existingSection: null,
      existingSectionNewScope: '',
      noSectionSelectedError: false,
      animatingTab: false,
      sendingData: false,
      loading: false,
      closeIntent: false,
      detachFlag: false
    }
  },

  computed: {
    existingSectionTitle () {
      if (this.existingSection) {
        return this.existingSection ? this.existingSection.title : ''
      }
    },
    selectorExistingBorderClass () {
      let cClass = {}
      switch (this.existingSectionNewScope) {
        case 'PRIVATE':
          cClass['border-red'] = true
          break

        case 'SHARED':
          cClass['border-yellow'] = true
          break

        default:
          cClass['border-blue'] = true
          break
      }
      return cClass
    },
    selectorBorderClass () {
      let cClass = {}
      switch (this.editedSection.newScope) {
        case 'PRIVATE':
          cClass['border-red'] = true
          break

        case 'SHARED':
          cClass['border-yellow'] = true
          break

        default:
          cClass['border-blue'] = true
          break
      }
      return cClass
    },
    isLoggedAnEditor () {
      return this.$store.getters.isLoggedAnEditor
    },
    titleEmpty () {
      if (this.editedSection) {
        return this.editedSection.title === ''
      }
      return false
    },
    titleTooLong () {
      if (this.editedSection) {
        return this.editedSection.title.length > 42
      }
      return false
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
      if (this.editedSection) {
        return this.editedSection.description === ''
      }
      return false
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
      this.loading = true
      this.axios.get('/1/model/section/' + this.section.id, {
        params: {
          parentSectionId: this.inSection ? this.inSection.id : ''
        }
      }).then((response) => {
        this.loading = false
        this.section = response.data.data
        this.startEditing()
      })
    },
    updateInElement () {
      if (!this.inInitiative) {
        this.axios.get('/1/model/section/' + this.inSection.id)
          .then((response) => {
            this.inSectionTitleOk = response.data.data.title
          })
      }
    },
    startEditing () {
      this.editedSection = JSON.parse(JSON.stringify(this.section))
      this.editedSection.newScope = this.section.scope ? this.section.scope : 'COMMON'
      this.editing = true
    },
    sectionSelected (section) {
      this.existingSection = section
    },
    clickOutside () {
      if (this.enableClickOutside) {
        this.closeThisConfirm()
      }
    },
    closeThisConfirm () {
      if (this.editing && (!this.titleEmpty || !this.descriptionEmpty)) {
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

      if (!this.addExisting) {
        if (this.titleEmpty) {
          ok = false
          this.titleEmptyError = true
        } else {
          if (this.titleTooLong) {
            ok = false
          }
        }
      } else {
        if (this.noSectionSelected) {
          ok = false
          this.noSectionSelectedError = true
        }
      }

      if (ok) {
        var sectionDto = JSON.parse(JSON.stringify(this.editedSection))

        if (this.isNew) {
          if (!this.addExisting) {
            this.sendingData = true
            this.axios.post('/1/model/section/' + this.inSection.id + '/subsection', sectionDto, {
              params: {
                onSubsectionId: this.onSection != null ? this.onSection.id : null,
                isBefore: this.isBefore
              }
            }).then((response) => {
              this.sendingData = false
              if (response.data.result === 'success') {
                this.closeThis()
                this.$store.dispatch('updateSectionDataInTree', { sectionId: this.inSection.id })
                this.$store.dispatch('refreshCurrentSection')
              }
            }).catch((error) => {
              console.log(error)
            })
          } else {
            /* add existing section */
            this.sendingData = true
            this.axios.put('/1/model/section/' + this.inSection.id + '/subsection/' + this.existingSection.id, {}, {
              params: {
                onSubsectionId: this.onSection != null ? this.onSection.id : null,
                isBefore: this.isBefore,
                scope: this.existingSectionNewScope,
                detachFlag: this.detachFlag
              }
            }).then((response) => {
              this.sendingData = false
              if (response.data.result === 'success') {
                this.closeThis()
                this.$store.dispatch('updateSectionDataInTree', { sectionId: this.inSection.id })
                this.$store.dispatch('refreshCurrentSection')
              }
            }).catch((error) => {
              console.log(error)
            })
          }
        } else {
          /* edit existing section */
          this.sendingData = true
          this.axios.put('/1/model/section/' + sectionDto.id, sectionDto, {
            params: {
              parentSectionId: this.inSection ? this.inSection.id : ''
            }
          }).then((response) => {
            this.sendingData = false
            if (response.data.result === 'success') {
              this.closeThis()
              this.$store.dispatch('updateSectionDataInTree', { sectionId: sectionDto.id })
              this.$store.dispatch('refreshCurrentSection')
            }
          }).catch((error) => {
            console.log(error)
          })
        }
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
    this.inSectionTitleOk = this.inSection ? this.inSection.title : ''

    if (this.isNew) {
      this.editedSection = {
        title: '',
        description: '',
        newScope: this.inSection.scope ? this.inSection.scope : 'COMMON'
      }
      this.existingSectionNewScope = this.inSection.scope ? this.inSection.scope : 'COMMON'
      this.editing = true
      this.$nextTick(() => {
        this.$refs.titleInput.focus()
      })
      this.updateInElement()
    } else {
      this.showEditButtons = true
      this.section.id = this.sectionId
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

.w3-modal {
  cursor: auto;
}

</style>
