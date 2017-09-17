<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>{{ isNew ? 'New Card' : 'Model Card'}}</h2>
        </div>

        <div class="w3-container div-modal-content">

          <app-model-modal-buttons
            v-if="isLoggedAnEditor"
            :show="showEditButtons"
            @edit="startEditing()"
            @delete="deleteCard()">
          </app-model-modal-buttons>

          <div v-if="isNew" class="w3-row">
            <label class=""><b>In section:</b></label>
            <br>
            <h4>{{ this.inSection.sectionTitle }}</h4>
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
                <keep-alive>
                  <app-model-card-selector
                    :initiativeId="initiativeId"
                    @select="cardSelected($event)">
                  </app-model-card-selector>
                </keep-alive>
              </div>
            </transition>
          </div>

          <div class="slider-container">
            <transition name="slideRightLeft">
              <div v-if="!addExisting" class="">
                <div class="w3-row w3-margin-top">
                  <label class=""><b>Title:</b></label>
                  <div v-if="!editing" class="w3-padding light-grey">
                    <div v-if="card.title !== ''" class="">
                      {{ card.title }}
                    </div>
                    <div v-else class="">
                      <i>(empty)</i>
                    </div>
                  </div>
                  <div v-else class="">
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

                <div class="w3-row w3-margin-top">
                  <label class=""><b>Text: <span v-if="editing" class="w3-small error-text">(required)</span></b></label>
                  <div v-if="!editing" class="w3-padding light-grey">
                    {{ card.text }}
                  </div>
                  <div v-else class="">
                    <textarea type="text" class="w3-input w3-border w3-round w3-hover-light-grey" v-model="editedCard.text"></textarea>
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
                      <div class="w3-col m6">
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
                            <option>NONE</option>
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
import { dateString } from '@/lib/common.js'
import Datepicker from 'vuejs-datepicker'
import ModelModalButtons from '@/components/model/modals/ModelModalButtons.vue'
import ModelCardSelector from '@/components/model/ModelCardSelector.vue'

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons,
    'datepicker': Datepicker,
    'app-model-card-selector': ModelCardSelector
  },

  props: {
    pars: {
      type: Object,
      default: null
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
      inSection: null,
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      textEmptyError: false,
      targetDateStr: '',
      isNew: false,
      addExisting: false,
      existingCard: null
    }
  },

  computed: {
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
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    updateCardData () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.cardWrapper = response.data.data
      })
    },
    closeThis () {
      this.$emit('close')
    },
    cardSelected (cardWrapper) {
      this.existingCard = cardWrapper
    },
    cancel () {
      if (this.isNew) {
        this.closeThis()
      } else {
        this.editing = false
      }
    },
    startEditing () {
      this.editedCard = JSON.parse(JSON.stringify(this.card))

      this.targetDateStr = new Date()
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
        if (!this.existingCard) {
          ok = false
        }
      }

      if (ok) {
        var cardDto = JSON.parse(JSON.stringify(this.editedCard))
        var responseF = (response) => {
          if (response.data.result === 'success') {
            this.closeThis()
            this.$store.commit('triggerUpdateModel')
          } else {
            this.showOutputMessage(response.data.message)
          }
        }

        var baseurl = '/1/initiative/' + this.initiativeId + '/model/cardWrapper'
        if (this.isNew) {
          if (!this.addExisting) {
            this.axios.post(baseurl, {cardDto}).then(responseF).catch((error) => {
              console.log(error)
            })
          } else {
            this.axios.put('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSection.sectionId + '/addCard/' + this.existingCard.id,
              {}).then(responseF).catch((error) => {
                console.log(error)
              })
          }
        } else {
          this.axios.put(baseurl + '/' + this.cardWrapper.id, cardDto).then(responseF).catch((error) => {
            console.log(error)
          })
        }
      }
    },
    deleteCard () {
      this.axios.delete('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.closeThis()
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.initiativeId = this.pars.initiativeId

    if (this.pars.new) {
      this.isNew = true

      this.inSection = {}
      this.inSection.sectionId = this.pars.sectionId
      this.inSection.sectionTitle = this.pars.sectionTitle

      this.editedCard = {
        sectionId: this.inSection.sectionId,
        stateControl: false,
        title: '',
        text: ''
      }

      this.editing = true
    } else {
      this.showEditButtons = true
      this.cardWrapper.id = this.pars.cardWrapperId
      this.updateCardData()
    }
  }
}
</script>

<style scoped>

.state-enable-button {
  width: 250px;
}

.state-tag {
  width: 100%;
}

</style>
