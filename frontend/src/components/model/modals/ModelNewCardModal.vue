<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Create new card</h2>
        </div>

        <div class="w3-container div-modal-content">

          <label class=""><b>Title</b></label>
          <input v-model="title"
            class="w3-input w3-hover-light-grey" type="text"
            :class="{ 'error-input' : titleErrorShow }">
          <app-error-panel
            :show="titleEmptyShow"
            message="please add a title">
          </app-error-panel>
          <app-error-panel
            :show="titleTooLongShow"
            message="name too long">
          </app-error-panel>

          <br>
          <label class=""><b>Text <span class="w3-small error-text">(required)</span></b></label>
          <textarea v-model="text"
            class="w3-input w3-border w3-round w3-hover-light-grey"
            :class="{ 'error-input' : textErrorShow }">
          </textarea>
          <app-error-panel
            :show="textErrorShow"
            message="please include the text of this card">
          </app-error-panel>
          <hr>

          <div class="modal-bottom-btns-row w3-row-padding">
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
</template>

<script>
export default {

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
      title: '',
      text: '',
      titleEmptyError: false,
      textEmptyError: false
    }
  },

  computed: {
    titleEmpty () {
      return this.title === ''
    },
    titleTooLong () {
      return this.title.length > 30
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
      return this.text === ''
    },
    textErrorShow () {
      return this.textEmptyError && this.textEmpty
    }
  },

  methods: {
    closeThis () {
      this.$emit('close')
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
          sectionId: this.sectionId,
          title: this.title,
          text: this.text
        }

        this.axios.post('/1/secured/initiative/' + this.initiativeId + '/model/card', cardDto).then((response) => {
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
    }
  },

  mounted () {
  }
}
</script>

<style scoped>

</style>
