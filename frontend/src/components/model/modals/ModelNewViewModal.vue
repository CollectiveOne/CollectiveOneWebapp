<template lang="html">
  <div class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-card-4">

        <div class="div-close-modal w3-display-topright w3-xlarge" @click="closeThis()">
          <i class="fa fa-times fa-close-modal" aria-hidden="true"></i>
        </div>

        <div class="w3-container w3-border-bottom">
          <h2>Create new view</h2>
        </div>

        <div class="w3-container div-modal-content">

          <label class=""><b>Title <span class="w3-small error-text">(required)</span></b></label>
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
          <label class=""><b>Description <span class="w3-small error-text">(required)</span></b></label>
          <textarea v-model="description"
            class="w3-input w3-border w3-round w3-hover-light-grey"
            :class="{ 'error-input' : descriptionErrorShow }">
          </textarea>
          <app-error-panel
            :show="descriptionErrorShow"
            message="please include a description of this section">
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
    }
  },

  data () {
    return {
      section: null,
      title: '',
      description: '',
      titleEmptyError: false,
      descriptionEmptyError: false
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
    descriptionEmpty () {
      return this.description === ''
    },
    descriptionErrorShow () {
      return this.descriptionEmptyError && this.descriptionEmpty
    }
  },

  methods: {
    updateSectionData () {
      // this.axios.get('/1/secured/token/' + this.assetId).then((response) => {
      /* TODO: update backend to return section data */
      this.section = {
        id: '1010',
        title: 'Vision',
        description: 'Describes the vision of CollectiveOne in the long-term',
        nCards: 1,
        nCardsSubsections: 15,
        nSubsections: 3,
        comments: [
          {
            user: 'user 1',
            text: 'Lorem ipsum dolor sit amet, no novum viris tempor vim. Id mel omnis animal epicuri, wisi philosophia vel eu. Ius iuvaret abhorreant ad. Vix dolor civibus ex, aeque persecuti constituam sed in. Vis id alteru'
          },
          {
            user: 'user 2',
            text: 'Sapientem corrumpit forensibus usu at. Te sed dolorem maluisset, vix stet audiam neglegentur ex. Vim id minim iudico platonem. No quaeque perpetua sententiae per, id congue veniam vix. Pro erant regione ea.'
          },
          {
            user: 'user 3',
            text: 'Clita voluptua mel no. Enim ceteros ut mea, mei percipit invenire cu. Fabulas efficiantur signiferumque vel eu. Viris tacimates id sed. Ea nec eirmod volumus.'
          }
        ]
      }
      // })
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
        let sectionDto = {
          title: this.title,
          description: this.description
        }

        console.log(sectionDto)
        // this.axios.post('/1/secured/model', sectionDto).then((response) => {
        //   if (response.data.result === 'success') {
        //     this.closeThis()
        //     this.$store.dispatch('updatedModel')
        //   } else {
        //     this.showOutputMessage(response.data.message)
        //   }
        // }).catch((error) => {
        //   console.log(error)
        // })
      }
    }
  },

  mounted () {
    this.updateSectionData()
  }
}
</script>

<style scoped>

</style>
