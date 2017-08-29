<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div v-if="cardWrapper" class="">
          <div class="w3-container w3-border-bottom">
            <h2>Card details</h2>
          </div>

          <div class="w3-container div-modal-content">

            <app-model-modal-buttons
              :show="true"
              @edit="startEditing()"
              @delete="deleteCard()">
            </app-model-modal-buttons>

            <label class=""><b>Title:</b></label>
            <div v-if="!editing" class="w3-padding light-grey">
              {{ card.title }}
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

            <br>
            <label class=""><b>Text:</b></label>
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

            <br>
            <div class="w3-row-padding">
              <div class="w3-col m6 w3-margin-bottom">
                <label class=""><b>State:</b></label>
                <div v-if="!editing" class="">
                  <div class="w3-tag gray-1 w3-padding w3-round">
                    {{ cardWrapper.state }}
                  </div>
                </div>
                <div v-else class="">
                  <select class="w3-select" v-model="editedCard.state">
                    <option>NONE</option>
                    <option>PLAN</option>
                    <option>REALITY</option>
                  </select>
                </div>
              </div>
              <div class="w3-col m6">
                <label class=""><b>Target Date:</b></label>
                <div v-if="!editing" class="">
                  <input class="w3-input" :value="dateString(this.cardWrapper.targetDate)" disabled>
                </div>
                <div v-else class="">
                  <datepicker
                    :value="targetDateStr"
                    @selected="targetDateSelected($event)">
                  </datepicker>
                </div>
              </div>
            </div>

            <div v-if="editing" class="modal-bottom-btns-row w3-row-padding">
              <hr>
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button-light" @click="closeThis()">Cancel</button>
              </div>
              <div class="w3-col m6">
                <button type="button" class="w3-button app-button" @click="accept()">Accept</button>
              </div>
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

export default {

  components: {
    'app-model-modal-buttons': ModelModalButtons,
    'datepicker': Datepicker
  },

  props: {
    initiativeId: {
      type: String,
      default: ''
    },
    cardWrapperId: {
      type: String,
      default: ''
    }
  },

  data () {
    return {
      cardWrapper: null,
      editedCard: null,
      editing: false,
      titleEmptyError: false,
      textEmptyError: false,
      targetDateStr: ''
    }
  },

  computed: {
    card () {
      return this.cardWrapper.card
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
      this.axios.get('/1/secured/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapperId).then((response) => {
        this.cardWrapper = response.data.data
      })
    },
    closeThis () {
      this.$emit('close')
    },
    startEditing () {
      this.editedCard = JSON.parse(JSON.stringify(this.card))

      this.targetDateStr = new Date()
      if (this.cardWrapper.targetDate) {
        if (this.cardWrapper.targetDate > 0) {
          this.targetDateStr = new Date(this.cardWrapper.targetDate)
        }
      }

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

      if (this.titleTooLong) {
        ok = false
      }

      if (this.textEmpty) {
        ok = false
        this.textEmptyError = true
      }

      if (ok) {
        let cardDto = JSON.parse(JSON.stringify(this.editedCard))

        this.axios.put('/1/secured/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapper.id, cardDto).then((response) => {
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
    deleteCard () {
      this.axios.delete('/1/secured/initiative/' + this.initiativeId + '/model/cardWrapper/' + this.cardWrapperId).then((response) => {
        this.$store.dispatch('refreshModel')
        this.closeThis()
      }).catch((error) => {
        console.log(error)
      })
    }
  },

  mounted () {
    this.updateCardData()
  }
}
</script>

<style scoped>

</style>
