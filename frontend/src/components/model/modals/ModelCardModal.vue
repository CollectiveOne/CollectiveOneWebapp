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
            @delete="deleteCard()"
            deleteMessage="This will delete the card from all the sections in which it is used."
            @remove="removeCard()"
            :removeMessage="'This will remove this card from the ' + inSectionTitle + ' section.'">
          </app-model-modal-buttons>


          <div v-if="isNew" class="w3-row">
            <label class=""><b>In section:</b></label>
            <br>
            <h4>{{ this.inSectionTitle }}</h4>
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

          <!-- <div class="slider-container">
            <transition name="slideLeftRight"> -->
              <div v-if="addExisting" class="">
                <app-model-card-selector
                  :initiativeId="initiativeId"
                  @select="cardSelected($event)">
                </app-model-card-selector>
                <app-error-panel
                  :show="noCardSelectedShow"
                  message="please select one card from above">
                </app-error-panel>
              </div>
            <!-- </transition>
          </div> -->

          <!-- <div :class="{'slider-container': animatingTab}">
            <transition name="slideRightLeft"
              @before-enter="animatingTab = true"
              @after-enter="animatingTab = false"
              @before-leave="animatingTab = true"
              @after-leave="animatingTab = false"> -->

              <div v-if="!addExisting">

                <!-- <div class="w3-row w3-margin-top image-container w3-center">
                  <img src="https://i.ytimg.com/vi/QX4j_zHAlw8/maxresdefault.jpg" alt="">
                </div> -->

                <div class="w3-row w3-margin-top">
                  <div v-if="!editing" class="w3-padding light-grey">
                    <div v-if="card.title !== ''" class="">
                      <b>{{ card.title }}</b>
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

                <div class="w3-row w3-margin-top">
                  <div v-if="!editing" class="w3-padding light-grey">
                    <vue-markdown class="marked-text" :source="card.text"></vue-markdown>
                  </div>
                  <div v-else class="">
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
            <!-- </transition>
          </div> -->


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
      editing: false,
      showEditButtons: false,
      titleEmptyError: false,
      textEmptyError: false,
      targetDateStr: '',
      addExisting: false,
      existingCard: null,
      noCardSelectedError: false,
      animatingTab: false
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
    },
    noCardSelected () {
      return this.existingCard === null
    },
    noCardSelectedShow () {
      return this.noCardSelectedError && this.noCardSelected
    }
  },

  methods: {
    dateString (v) {
      return dateString(v)
    },
    update () {
      this.axios.get('/1/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id).then((response) => {
        this.cardWrapper = response.data.data
      })
    },
    cardSelected (cardWrapper) {
      this.existingCard = cardWrapper
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
        if (this.noCardSelected) {
          ok = false
          this.noCardSelectedError = true
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

        if (this.isNew) {
          if (!this.addExisting) {
            /* create new card */
            this.axios.post('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSectionId + '/cardWrapper', cardDto).then(responseF).catch((error) => {
              console.log(error)
            })
          } else {
            this.axios.put('/1/initiative/' + this.initiativeId + '/model/section/' + this.inSectionId + '/cardWrapper/' + this.existingCard.id,
              {}).then(responseF).catch((error) => {
                console.log(error)
              })
          }
        } else {
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
    }
  },

  mounted () {
    if (this.isNew) {
      this.editedCard = {
        stateControl: false,
        title: '',
        text: ''
      }
      this.editing = true
    } else {
      this.showEditButtons = true
      this.cardWrapper.id = this.cardWrapperId
      this.update()
    }
  }
}
</script>

<style scoped>

.not-add-existing-container {
  overflow: visible;
}

.image-container {
  height: 250px;
  width: 100%;
  overflow: hidden;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.state-enable-button {
  width: 250px;
}

.state-tag {
  width: 100%;
}

</style>
