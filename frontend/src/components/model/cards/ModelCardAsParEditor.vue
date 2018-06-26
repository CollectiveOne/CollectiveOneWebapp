<template lang="html">
  <div class="card-container-doc"
    @mouseover="hovering = true"
    @mouseleave="hovering = false">

    <div class="w3-row">

      <div class="w3-col s12">

        <div class="card-container-padded">
          <div class="w3-row card-title">
            <input type="text" class="w3-input w3-hover-light-grey" placeholder="Enter card title"  v-model="editedCard.title">
          </div>
          <div ref="cardText" class="w3-row card-text">
            <app-markdown-editor placeholder="Enter text here" v-model="editedCard.text"></app-markdown-editor>
          </div>
          <div>
        <button v-show="!sendingData" class="w3-button app-button" name="button" @click="createCard()">{{ cardButtonText }}</button>
                <div v-show="sendingData" class="sending-accept light-grey">
          <img class="" src="../../../assets/loading.gif" alt="">
        </div>
          </div>
        </div>

<!--         <div v-if="hasImage"
          class="w3-row w3-center w3-display-container image-container">
          <img class="" :src="card.imageFile.url + '?lastUpdated=' + card.imageFile.lastUpdated" alt="">
        </div> -->

      </div>
    </div>
  </div>
</template>

<script>
export default {

  name: 'model-card-as-card',

  props: {
    isNew: {
      type: Boolean,
      default: false
    },
    cardWrapper: {
      type: Object,
      default: null
    },
    forceUpdate: {
      type: Boolean,
      default: true
    },
    inSection: {
      type: Object,
      default: null
    }
  },

  data () {
    return {
      hovering: false,
      editedCard: {
        title: '',
        text: '',
        newScope: 'SHARED'
      },
      showFull: false,
      addExisting: false,
      sendingData: false,
      loading: false
    }
  },

  computed: {
    atCardWrapper () {
      return this.cardWrapper === null ? null : this.cardWrapper.cardWrapper
    },
    newCardLocation () {
      return this.$store.state.support.createNewCardLocation === 'before'
    },
    cardButtonText () {
      return this.isNew ? 'Create new card' : 'Save Card'
    }
  },

  methods: {
    startEditing () {
      this.editedCard = JSON.parse(JSON.stringify(this.atCardWrapper.card))
      this.editedCard.newScope = this.cardWrapper.scope
    },
    hoverEnter () {
      this.showActionButton = true
      this.highlight = true
    },
    hoverLeave () {
      this.showActionButton = false
      this.highlight = false
    },
    createCard () {
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
        if (this.isNew) {
          if (!this.addExisting) {
            /* create new card */
            this.sendingData = true
            this.axios.post('/1/model/section/' + this.inSection.id + '/cardWrapper', cardDto, { params: {
              onCardWrapperId: this.atCardWrapper ? this.atCardWrapper.id : null,
              isBefore: this.newCardLocation
            }}).then((response) => {
              this.sendingData = false
              if (response.data.result === 'success') {
                this.$emit('updateCards')
              } else {
                console.log(response.data.message)
              }
            }).catch((error) => {
              console.log(error)
            })
          }
        } else {
          /* editing */
          console.log('editing')
          this.sendingData = true
          this.axios.put('/1/model/cardWrapper/' + this.atCardWrapper.id, cardDto, {
            params: {
              inSectionId: this.inSection.id
            }
          })
            .then((response) => {
              this.sendingData = false
              if (response.data.result === 'success') {
                this.closeThis()
                this.$emit('update')
              }
            }).catch((error) => {
              console.log(error)
            })
        }
      }
    }
  },

  mounted () {
    if (this.isNew) {
      console.log('eita call hauci')
      this.editedCard = {
        title: '',
        text: '',
        newScope: 'SHARED'
      }
      this.editing = true
    } else {
      this.startEditing()
    }
  }
}
</script>

<style>

</style>

<style scoped>

.card-container-padded {
}

.image-container {
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  min-height: 80px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-top: 16px;
  margin-bottom: 16px;
}

.image-container img {
  max-height: 100%;
  max-width: 100%;
}

.image-container-doc {
  height: 350px;
  margin-bottom: 16px;
}

.image-container-doc img {
  height: 100%;
}

.card-text {
  overflow-y: hidden;
}

.card-text-ascard-no-image {
  max-height: 250px;
}

.card-text p {
  margin-top: 0px;
  margin-bottom: 0px;
}

.bottom-row {
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  padding: 3px 6px;
  min-height: 30px;
}

.insection-tag-container {
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 5px;
}

.indicator-comp {
  margin-right: 6px;
  margin-left: 4px;
}

</style>
