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
      textEmptyError: false
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
      this.editing = true
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
        let cardDto = {
          id: this.editedCard.id,
          sectionId: this.editedCard.sectionId,
          title: this.editedCard.title,
          text: this.editedCard.text
        }

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
